package com.ecommerce.davivienda.service.payment.builder;

import com.ecommerce.davivienda.dto.payment.CardDataDto;
import com.ecommerce.davivienda.dto.payment.PaymentProcessResponseDto;
import com.ecommerce.davivienda.entity.cart.Cart;
import com.ecommerce.davivienda.entity.payment.*;

/**
 * Interfaz de servicio de construcción para procesamiento de pagos.
 * Define operaciones para construir entidades y respuestas de pago.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface PaymentBuilderService {

    /**
     * Genera un número de referencia único para el pago usando UUID.
     *
     * @return PaymentReference con número único generado
     */
    PaymentReference generatePaymentReference();

    /**
     * Construye la entidad Payment principal.
     *
     * @param cart Carrito asociado al pago
     * @param paymentType Tipo de pago
     * @param reference Referencia del pago
     * @param status Estado del pago
     * @return Payment construido
     */
    Payment buildPayment(Cart cart, PaymentType paymentType, PaymentReference reference, PaymentStatus status);

    /**
     * Construye la entidad PaymentDebit.
     *
     * @param payment Pago asociado
     * @param cardData Datos de la tarjeta
     * @return PaymentDebit construido
     */
    PaymentDebit buildPaymentDebit(Payment payment, CardDataDto cardData);

    /**
     * Construye la entidad PaymentCredit.
     *
     * @param payment Pago asociado
     * @param cardData Datos de la tarjeta
     * @param installments Número de cuotas
     * @return PaymentCredit construido
     */
    PaymentCredit buildPaymentCredit(Payment payment, CardDataDto cardData, Integer installments);

    /**
     * Construye el DTO de respuesta del pago procesado.
     *
     * @param payment Pago creado
     * @param cardData Datos de la tarjeta (para obtener últimos 4 dígitos)
     * @param installments Número de cuotas (opcional)
     * @return PaymentProcessResponseDto
     */
    PaymentProcessResponseDto buildPaymentResponse(Payment payment, CardDataDto cardData, Integer installments);

    /**
     * Extrae los últimos 4 dígitos del número de tarjeta por seguridad.
     *
     * @param cardNumber Número completo de la tarjeta
     * @return Últimos 4 dígitos
     */
    String maskCardNumber(String cardNumber);
}

