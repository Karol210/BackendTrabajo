package com.ecommerce.davivienda.service.payment.validation.common;

import com.ecommerce.davivienda.dto.payment.CardDataDto;
import com.ecommerce.davivienda.exception.payment.PaymentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

import static com.ecommerce.davivienda.constants.Constants.*;

/**
 * Implementación del servicio de validación común para procesamiento de pagos.
 * Contiene toda la lógica de validación genérica relacionada con datos de tarjeta.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentCommonValidationServiceImpl implements PaymentCommonValidationService {

    private static final Pattern EXPIRATION_DATE_PATTERN = Pattern.compile("^(0[1-9]|1[0-2])/\\d{2}$");
    private static final Pattern CARD_NUMBER_PATTERN = Pattern.compile("^\\d{16}$");

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

