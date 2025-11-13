package com.ecommerce.davivienda.service.payment;

import com.ecommerce.davivienda.dto.payment.CardDataDto;
import com.ecommerce.davivienda.dto.payment.PaymentProcessRequestDto;
import com.ecommerce.davivienda.dto.payment.PaymentProcessResponseDto;
import com.ecommerce.davivienda.entity.cart.Cart;
import com.ecommerce.davivienda.entity.payment.*;
import com.ecommerce.davivienda.exception.payment.PaymentException;
import com.ecommerce.davivienda.repository.payment.PaymentCreditRepository;
import com.ecommerce.davivienda.repository.payment.PaymentDebitRepository;
import com.ecommerce.davivienda.repository.payment.PaymentRepository;
import com.ecommerce.davivienda.service.payment.builder.PaymentBuilderService;
import com.ecommerce.davivienda.service.payment.validation.PaymentValidationService;
import com.ecommerce.davivienda.util.AuthenticatedUserUtil;
import com.ecommerce.davivienda.util.Base64DecryptionService;
import com.ecommerce.davivienda.util.JsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ecommerce.davivienda.constants.Constants.*;

/**
 * Implementación del servicio principal de procesamiento de pagos.
 * Coordina el flujo completo delegando validaciones y construcción a servicios especializados.
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

    private final PaymentValidationService validationService;
    private final PaymentBuilderService builderService;
    private final Base64DecryptionService base64DecryptionService;
    private final JsonUtils jsonUtils;
    private final PaymentRepository paymentRepository;
    private final PaymentDebitRepository paymentDebitRepository;
    private final PaymentCreditRepository paymentCreditRepository;
    private final AuthenticatedUserUtil authenticatedUserUtil; // ✅ NUEVO

    @Override
    @Transactional
    public PaymentProcessResponseDto processPayment(PaymentProcessRequestDto request) {
        // ✅ Obtener username del usuario autenticado
        String userEmail = authenticatedUserUtil.getCurrentUsername();
        log.info("Iniciando procesamiento de pago para usuario: {}", userEmail);

        try {
            CardDataDto cardData = decryptAndParseCardData(request.getEncryptedCardData());
            
            // ✅ Si no se proporciona cartId, buscar por email del usuario autenticado
            Cart cart;
            if (request.getCartId() != null) {
                log.info("Usando carrito especificado - ID: {}", request.getCartId());
                cart = validationService.validateCart(request.getCartId());
            } else {
                log.info("CartId no proporcionado, buscando carrito del usuario autenticado: {}", userEmail);
                cart = validationService.validateCartByUserEmail(userEmail);
            }
            
            validationService.validateCardData(cardData);
            
            PaymentType paymentType = validationService.validatePaymentType(cardData.getPaymentType());
            Integer installments = validationService.validateInstallments(
                    cardData.getInstallments(), 
                    cardData.getPaymentType()
            );
            PaymentStatus pendingStatus = validationService.findPendingStatus();
            
            PaymentReference reference = builderService.generatePaymentReference();
            Payment payment = builderService.buildPayment(cart, paymentType, reference, pendingStatus);
            Payment savedPayment = paymentRepository.save(payment);
            
            savePaymentDetails(savedPayment, cardData, installments);
            
            PaymentProcessResponseDto response = builderService.buildPaymentResponse(
                    savedPayment, 
                    cardData, 
                    installments
            );

            log.info("Pago procesado exitosamente - ID: {}, Referencia: {}, Tipo: {}", 
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
     *
     * @param payment Pago guardado
     * @param cardData Datos de la tarjeta
     * @param installments Número de cuotas validado
     */
    private void savePaymentDetails(Payment payment, CardDataDto cardData, Integer installments) {
        String paymentType = cardData.getPaymentType().trim().toLowerCase();

        if (PAYMENT_TYPE_DEBIT.equals(paymentType)) {
            log.debug("Guardando detalles de pago débito");
            PaymentDebit paymentDebit = builderService.buildPaymentDebit(payment, cardData);
            paymentDebitRepository.save(paymentDebit);
            log.info("Detalles de pago débito guardados exitosamente");

        } else if (PAYMENT_TYPE_CREDIT.equals(paymentType)) {
            log.debug("Guardando detalles de pago crédito con {} cuotas", installments);
            PaymentCredit paymentCredit = builderService.buildPaymentCredit(payment, cardData, installments);
            paymentCreditRepository.save(paymentCredit);
            log.info("Detalles de pago crédito guardados exitosamente - Cuotas: {}", installments);
        }
    }
}

