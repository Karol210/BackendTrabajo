package com.ecommerce.davivienda.service.payment.validation;

import com.ecommerce.davivienda.dto.payment.CardDataDto;
import com.ecommerce.davivienda.entity.cart.Cart;
import com.ecommerce.davivienda.entity.payment.PaymentStatus;
import com.ecommerce.davivienda.entity.payment.PaymentType;
import com.ecommerce.davivienda.exception.payment.PaymentException;
import com.ecommerce.davivienda.repository.cart.CartRepository;
import com.ecommerce.davivienda.repository.payment.PaymentStatusRepository;
import com.ecommerce.davivienda.repository.payment.PaymentTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

import static com.ecommerce.davivienda.constants.Constants.*;

/**
 * Implementación del servicio de validación para procesamiento de pagos.
 * Contiene toda la lógica de validación de datos y reglas de negocio.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentValidationServiceImpl implements PaymentValidationService {

    private static final String PAYMENT_TYPE_DEBIT = "debito";
    private static final String PAYMENT_TYPE_CREDIT = "credito";
    private static final String PAYMENT_STATUS_PENDING = "Pendiente";
    private static final Pattern EXPIRATION_DATE_PATTERN = Pattern.compile("^(0[1-9]|1[0-2])/\\d{2}$");
    private static final Pattern CARD_NUMBER_PATTERN = Pattern.compile("^\\d{16}$");

    private final CartRepository cartRepository;
    private final PaymentTypeRepository paymentTypeRepository;
    private final PaymentStatusRepository paymentStatusRepository;

    @Override
    public Cart validateCart(Integer cartId) {
        log.debug("Validando carrito con ID: {}", cartId);

        if (cartId == null) {
            log.error("ID de carrito es nulo");
            throw new PaymentException(ERROR_CART_NOT_FOUND, CODE_CART_NOT_FOUND);
        }

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> {
                    log.error("Carrito no encontrado con ID: {}", cartId);
                    return new PaymentException(ERROR_CART_NOT_FOUND, CODE_CART_NOT_FOUND);
                });

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            log.error("Carrito vacío con ID: {}", cartId);
            throw new PaymentException(ERROR_CART_EMPTY_FOR_PAYMENT, CODE_CART_EMPTY_FOR_PAYMENT);
        }

        log.info("Carrito validado exitosamente - ID: {}, Items: {}", cartId, cart.getItems().size());
        return cart;
    }

    @Override
    public Cart validateCartByUserEmail(String correo) {
        log.debug("Validando carrito del usuario con correo: {}", correo);

        if (correo == null || correo.trim().isEmpty()) {
            log.error("Correo de usuario es nulo o vacío");
            throw new PaymentException(ERROR_CART_NOT_FOUND, CODE_CART_NOT_FOUND);
        }

        Cart cart = cartRepository.findByUserEmail(correo)
                .orElseThrow(() -> {
                    log.error("Carrito no encontrado para el usuario: {}", correo);
                    return new PaymentException(ERROR_CART_NOT_FOUND, CODE_CART_NOT_FOUND);
                });

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            log.error("Carrito vacío para el usuario: {}", correo);
            throw new PaymentException(ERROR_CART_EMPTY_FOR_PAYMENT, CODE_CART_EMPTY_FOR_PAYMENT);
        }

        log.info("Carrito validado exitosamente para usuario {} - ID: {}, Items: {}", 
                correo, cart.getCarritoId(), cart.getItems().size());
        return cart;
    }

    @Override
    public void validateCardData(CardDataDto cardData) {
        log.debug("Validando datos de tarjeta");

        if (cardData.getCardNumber() == null || cardData.getCardNumber().trim().isEmpty()) {
            log.error("Número de tarjeta vacío");
            throw new PaymentException(ERROR_INVALID_CARD_NUMBER, CODE_INVALID_CARD_NUMBER);
        }

        if (cardData.getCardHolderName() == null || cardData.getCardHolderName().trim().isEmpty()) {
            log.error("Nombre del titular vacío");
            throw new PaymentException(ERROR_INVALID_CARD_DATA_FORMAT, CODE_INVALID_CARD_DATA_FORMAT);
        }

        if (cardData.getPaymentType() == null || cardData.getPaymentType().trim().isEmpty()) {
            log.error("Tipo de pago vacío");
            throw new PaymentException(ERROR_INVALID_PAYMENT_TYPE, CODE_INVALID_PAYMENT_TYPE);
        }

        validateCardNumber(cardData.getCardNumber());

        if (cardData.getExpirationDate() != null && !cardData.getExpirationDate().trim().isEmpty()) {
            validateExpirationDate(cardData.getExpirationDate());
        }

        log.info("Datos de tarjeta validados exitosamente");
    }

    @Override
    public PaymentType validatePaymentType(String paymentTypeStr) {
        log.debug("Validando tipo de pago: {}", paymentTypeStr);

        if (paymentTypeStr == null || paymentTypeStr.trim().isEmpty()) {
            log.error("Tipo de pago vacío");
            throw new PaymentException(ERROR_INVALID_PAYMENT_TYPE, CODE_INVALID_PAYMENT_TYPE);
        }

        String normalizedType = paymentTypeStr.trim().toLowerCase();

        if (!PAYMENT_TYPE_DEBIT.equals(normalizedType) && !PAYMENT_TYPE_CREDIT.equals(normalizedType)) {
            log.error("Tipo de pago inválido: {}", paymentTypeStr);
            throw new PaymentException(ERROR_INVALID_PAYMENT_TYPE, CODE_INVALID_PAYMENT_TYPE);
        }

        PaymentType paymentType = paymentTypeRepository.findByPaymentType(normalizedType)
                .orElseThrow(() -> {
                    log.error("Tipo de pago no encontrado en BD: {}", normalizedType);
                    return new PaymentException(ERROR_INVALID_PAYMENT_TYPE, CODE_INVALID_PAYMENT_TYPE);
                });

        log.info("Tipo de pago validado: {}", normalizedType);
        return paymentType;
    }

    @Override
    public PaymentStatus findPendingStatus() {
        log.debug("Buscando estado de pago 'Pendiente'");

        PaymentStatus status = paymentStatusRepository.findByName(PAYMENT_STATUS_PENDING)
                .orElseThrow(() -> {
                    log.error("Estado de pago 'Pendiente' no encontrado en BD");
                    return new PaymentException(ERROR_PAYMENT_STATUS_NOT_FOUND, CODE_PAYMENT_STATUS_NOT_FOUND);
                });

        log.debug("Estado 'Pendiente' encontrado con ID: {}", status.getPaymentStatusId());
        return status;
    }

    @Override
    public Integer validateInstallments(Integer installments, String paymentType) {
        log.debug("Validando cuotas: {} para tipo: {}", installments, paymentType);

        String normalizedType = paymentType.trim().toLowerCase();

        if (PAYMENT_TYPE_DEBIT.equals(normalizedType)) {
            if (installments != null && installments > 1) {
                log.warn("Pagos débito solo permiten 1 cuota, ignorando valor: {}", installments);
            }
            return 1;
        }

        if (PAYMENT_TYPE_CREDIT.equals(normalizedType)) {
            if (installments == null) {
                log.debug("No se especificaron cuotas para crédito, usando 1 por defecto");
                return 1;
            }

            if (installments <= 0) {
                log.error("Número de cuotas inválido: {}", installments);
                throw new PaymentException(ERROR_INVALID_INSTALLMENTS, CODE_INVALID_INSTALLMENTS);
            }

            log.info("Cuotas validadas para crédito: {}", installments);
            return installments;
        }

        return 1;
    }

    @Override
    public void validateExpirationDate(String expirationDate) {
        log.debug("Validando fecha de vencimiento: {}", expirationDate);

        if (expirationDate == null || expirationDate.trim().isEmpty()) {
            return;
        }

        if (!EXPIRATION_DATE_PATTERN.matcher(expirationDate.trim()).matches()) {
            log.error("Formato de fecha de vencimiento inválido: {}", expirationDate);
            throw new PaymentException(ERROR_INVALID_EXPIRATION_DATE, CODE_INVALID_EXPIRATION_DATE);
        }

        log.debug("Fecha de vencimiento validada: {}", expirationDate);
    }

    @Override
    public void validateCardNumber(String cardNumber) {
        log.debug("Validando número de tarjeta");

        String cleanCardNumber = cardNumber.replaceAll("\\s", "");

        if (!CARD_NUMBER_PATTERN.matcher(cleanCardNumber).matches()) {
            log.error("Número de tarjeta inválido (debe tener 16 dígitos)");
            throw new PaymentException(ERROR_INVALID_CARD_NUMBER, CODE_INVALID_CARD_NUMBER);
        }

        log.debug("Número de tarjeta validado exitosamente");
    }
}

