package com.ecommerce.davivienda.service.payment.transactional.cart;

/**
 * Servicio transaccional para operaciones de actualización de estado del carrito.
 * Maneja la transición del carrito a estado "Procesando" al procesar el pago.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface PaymentCartTransactionalService {

    /**
     * Actualiza el estado del carrito a "Procesando".
     *
     * @param cartId ID del carrito a actualizar
     */
    void updateCartStatusToProcessing(Integer cartId);
}

