package com.ecommerce.davivienda.service.payment.validation.common;

import com.ecommerce.davivienda.dto.payment.CardDataDto;

/**
 * Interfaz de servicio de validación común para procesamiento de pagos.
 * Define operaciones de validación genéricas relacionadas con datos de tarjeta.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface PaymentCommonValidationService {

    /**
     * Valida los datos de la tarjeta desencriptados.
     *
     * @param cardData Datos de la tarjeta a validar
     */
    void validateCardData(CardDataDto cardData);

    /**
     * Valida el formato de la fecha de vencimiento (MM/YY).
     *
     * @param expirationDate Fecha de vencimiento a validar
     */
    void validateExpirationDate(String expirationDate);

    /**
     * Valida que el número de tarjeta tenga formato correcto (16 dígitos).
     *
     * @param cardNumber Número de tarjeta a validar
     */
    void validateCardNumber(String cardNumber);
}

