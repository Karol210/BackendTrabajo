package com.ecommerce.davivienda.service.payment.validation;

import com.ecommerce.davivienda.dto.payment.CardDataDto;
import com.ecommerce.davivienda.entity.cart.Cart;
import com.ecommerce.davivienda.entity.payment.PaymentStatus;
import com.ecommerce.davivienda.entity.payment.PaymentType;

/**
 * Interfaz de servicio de validación para procesamiento de pagos.
 * Define operaciones de validación de datos de tarjeta, carrito y reglas de negocio.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface PaymentValidationService {

    /**
     * Valida que el carrito exista, no esté vacío y tenga items.
     *
     * @param cartId ID del carrito a validar
     * @return Cart validado
     */
    Cart validateCart(Integer cartId);

    /**
     * Busca y valida el carrito activo del usuario autenticado por su correo.
     * Si no existe carrito, lanza excepción.
     *
     * @param correo Correo del usuario autenticado
     * @return Cart validado del usuario
     */
    Cart validateCartByUserEmail(String correo);

    /**
     * Valida los datos de la tarjeta desencriptados.
     *
     * @param cardData Datos de la tarjeta a validar
     */
    void validateCardData(CardDataDto cardData);

    /**
     * Valida el tipo de pago y retorna la entidad correspondiente.
     *
     * @param paymentTypeStr Tipo de pago ("debito" o "credito")
     * @return PaymentType encontrado
     */
    PaymentType validatePaymentType(String paymentTypeStr);

    /**
     * Busca el estado de pago "Pendiente".
     *
     * @return PaymentStatus con estado Pendiente
     */
    PaymentStatus findPendingStatus();

    /**
     * Valida el número de cuotas según el tipo de pago.
     *
     * @param installments Número de cuotas (puede ser null)
     * @param paymentType Tipo de pago
     * @return Número de cuotas validado (1 si es null para débito)
     */
    Integer validateInstallments(Integer installments, String paymentType);

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

