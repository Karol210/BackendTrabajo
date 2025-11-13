package com.ecommerce.davivienda.service.payment;

import com.ecommerce.davivienda.dto.payment.CardDataDto;
import com.ecommerce.davivienda.dto.payment.PaymentProcessRequestDto;
import com.ecommerce.davivienda.dto.payment.PaymentProcessResponseDto;
import com.ecommerce.davivienda.entity.cart.Cart;
import com.ecommerce.davivienda.entity.cart.CartItem;
import com.ecommerce.davivienda.entity.payment.*;
import com.ecommerce.davivienda.exception.payment.PaymentException;
import com.ecommerce.davivienda.mapper.payment.PaymentMapper;
import com.ecommerce.davivienda.service.payment.reference.PaymentReferenceService;
import com.ecommerce.davivienda.service.payment.transactional.cart.PaymentCartTransactionalService;
import com.ecommerce.davivienda.service.payment.transactional.cartitem.PaymentCartItemTransactionalService;
import com.ecommerce.davivienda.service.payment.transactional.payment.PaymentPaymentTransactionalService;
import com.ecommerce.davivienda.service.payment.validation.cart.PaymentCartValidationService;
import com.ecommerce.davivienda.service.payment.validation.common.PaymentCommonValidationService;
import com.ecommerce.davivienda.service.payment.validation.payment.PaymentPaymentValidationService;
import com.ecommerce.davivienda.service.stock.transactional.stock.StockStockTransactionalService;
import com.ecommerce.davivienda.util.AuthenticatedUserUtil;
import com.ecommerce.davivienda.util.Base64DecryptionService;
import com.ecommerce.davivienda.util.JsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ecommerce.davivienda.constants.Constants.*;

/**
 * Implementación del servicio principal de procesamiento de pagos.
 * Coordina el flujo completo delegando validaciones y mapeo a servicios especializados.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private static final String PAYMENT_TYPE_DEBIT = "debito";
    private static final String PAYMENT_TYPE_CREDIT = "credito";

    // Validation subcapacidades por dominio
    private final PaymentCartValidationService cartValidationService;
    private final PaymentPaymentValidationService paymentValidationService;
    private final PaymentCommonValidationService commonValidationService;

    // Transactional subcapacidades
    private final PaymentPaymentTransactionalService paymentTransactionalService;
    private final PaymentCartTransactionalService cartTransactionalService;
    private final PaymentCartItemTransactionalService cartItemTransactionalService;
    private final StockStockTransactionalService stockTransactionalService;

    // Reference service (lógica de negocio)
    private final PaymentReferenceService paymentReferenceService;

    // Mapper (mapeo DTO ↔ Entity)
    private final PaymentMapper paymentMapper;

    // Utilities
    private final Base64DecryptionService base64DecryptionService; // Usado para desencriptar request y encriptar BD
    private final JsonUtils jsonUtils;
    private final AuthenticatedUserUtil authenticatedUserUtil;

    @Override
    @Transactional
    public PaymentProcessResponseDto processPayment(PaymentProcessRequestDto request) {
        String userEmail = authenticatedUserUtil.getCurrentUsername();
        log.info("Iniciando procesamiento de pago para usuario: {}", userEmail);

        try {
            CardDataDto cardData = decryptAndParseCardData(request.getEncryptedCardData());
            
            Cart cart = resolveCart(request.getCartId(), userEmail);
            
            commonValidationService.validateCardData(cardData);
            
            PaymentType paymentType = paymentValidationService.validatePaymentType(cardData.getPaymentType());
            Integer installments = paymentValidationService.validateInstallments(
                    cardData.getInstallments(), 
                    cardData.getPaymentType()
            );
            PaymentStatus pendingStatus = paymentValidationService.findPendingStatus();
            
            PaymentReference reference = paymentReferenceService.generateUniqueReference();
            Payment payment = paymentMapper.toPayment(cart, paymentType, reference, pendingStatus);
            Payment savedPayment = paymentTransactionalService.savePayment(payment);
            
            savePaymentDetails(savedPayment, cardData, installments);
            
            decreaseProductsStock(cart.getCarritoId());
            
            cartTransactionalService.updateCartStatusToProcessing(cart.getCarritoId());
            
            PaymentProcessResponseDto response = paymentMapper.toPaymentProcessResponseDto(savedPayment);

            log.info("Pago procesado exitosamente - ID: {}, Referencia: {}, Tipo: {}, Carrito actualizado a 'Procesando'", 
                    savedPayment.getPaymentId(), 
                    reference.getReferenceNumber(),
                    paymentType.getPaymentType());

            return response;

        } catch (PaymentException e) {
            log.error("Error de negocio al procesar pago: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al procesar pago: {}", e.getMessage(), e);
            throw new PaymentException(
                    ERROR_PAYMENT_PROCESSING_FAILED + ": " + e.getMessage(),
                    CODE_PAYMENT_PROCESSING_FAILED,
                    e
            );
        }
    }

    /**
     * Resuelve el carrito a utilizar: por ID específico o por email del usuario.
     *
     * @param cartId ID del carrito (puede ser null)
     * @param userEmail Email del usuario autenticado
     * @return Cart validado
     */
    private Cart resolveCart(Integer cartId, String userEmail) {
        if (cartId != null) {
            log.info("Usando carrito especificado - ID: {}", cartId);
            return cartValidationService.validateCart(cartId);
        } else {
            log.info("CartId no proporcionado, buscando carrito del usuario autenticado: {}", userEmail);
            return cartValidationService.validateCartByUserEmail(userEmail);
        }
    }

    /**
     * Desencripta y parsea los datos de la tarjeta desde Base64.
     *
     * @param encryptedData Datos encriptados en Base64
     * @return CardDataDto deserializado
     */
    private CardDataDto decryptAndParseCardData(String encryptedData) {
        log.debug("Desencriptando datos de tarjeta");

        try {
            String decryptedJson = base64DecryptionService.decrypt(encryptedData);
            log.debug("Datos desencriptados, parseando JSON");

            CardDataDto cardData = jsonUtils.deserializeFromJson(decryptedJson, CardDataDto.class);

            if (cardData == null) {
                log.error("Datos de tarjeta nulos después de deserialización");
                throw new PaymentException(
                        ERROR_INVALID_CARD_DATA_FORMAT,
                        CODE_INVALID_CARD_DATA_FORMAT
                );
            }

            log.info("Datos de tarjeta desencriptados y parseados exitosamente");
            return cardData;

        } catch (IllegalArgumentException e) {
            log.error("Error al desencriptar Base64: {}", e.getMessage());
            throw new PaymentException(
                    ERROR_INVALID_ENCRYPTED_DATA,
                    CODE_INVALID_ENCRYPTED_DATA,
                    e
            );
        } catch (JsonProcessingException e) {
            log.error("Error al parsear JSON de tarjeta: {}", e.getMessage());
            throw new PaymentException(
                    ERROR_INVALID_CARD_DATA_FORMAT,
                    CODE_INVALID_CARD_DATA_FORMAT,
                    e
            );
        }
    }

    /**
     * Guarda los detalles específicos del pago según el tipo (débito o crédito).
     * Los datos sensibles (nombre titular y número tarjeta) se encriptan antes de guardar.
     *
     * @param payment Pago guardado
     * @param cardData Datos de la tarjeta
     * @param installments Número de cuotas validado
     */
    private void savePaymentDetails(Payment payment, CardDataDto cardData, Integer installments) {
        String paymentType = cardData.getPaymentType().trim().toLowerCase();

        if (PAYMENT_TYPE_DEBIT.equals(paymentType)) {
            log.debug("Guardando detalles de pago débito (datos encriptados)");
            PaymentDebit paymentDebit = paymentMapper.toPaymentDebit(payment, cardData, base64DecryptionService);
            paymentTransactionalService.savePaymentDebit(paymentDebit);
            log.info("Detalles de pago débito guardados exitosamente (encriptado)");

        } else if (PAYMENT_TYPE_CREDIT.equals(paymentType)) {
            log.debug("Guardando detalles de pago crédito con {} cuotas (datos encriptados)", installments);
            PaymentCredit paymentCredit = paymentMapper.toPaymentCredit(payment, cardData, installments, base64DecryptionService);
            paymentTransactionalService.savePaymentCredit(paymentCredit);
            log.info("Detalles de pago crédito guardados exitosamente (encriptado) - Cuotas: {}", installments);
        }
    }

    /**
     * Disminuye el stock de cada producto en el carrito según la cantidad comprada.
     *
     * @param cartId ID del carrito
     */
    private void decreaseProductsStock(Integer cartId) {
        log.debug("Disminuyendo stock de productos del carrito: {}", cartId);

        List<CartItem> cartItems = cartItemTransactionalService.findByCartId(cartId);

        if (cartItems.isEmpty()) {
            log.warn("No se encontraron items en el carrito: {}", cartId);
            return;
        }

        int productsProcessed = 0;
        for (CartItem item : cartItems) {
            Integer productId = item.getProduct().getProductoId();
            Integer quantity = item.getCantidad();
            String productName = item.getProduct().getNombre();

            try {
                stockTransactionalService.decreaseStock(productId, quantity);
                productsProcessed++;
                log.debug("Stock disminuido para producto '{}' (ID: {}) - Cantidad: {}",
                        productName, productId, quantity);

            } catch (IllegalStateException e) {
                log.error("Error al disminuir stock para producto '{}' (ID: {}): {}",
                        productName, productId, e.getMessage());
                throw new PaymentException(
                        "Error al disminuir stock del producto: " + productName,
                        CODE_PAYMENT_PROCESSING_FAILED,
                        e
                );
            }
        }

        log.info("Stock actualizado para {} productos del carrito {}", productsProcessed, cartId);
    }
}

