package com.ecommerce.davivienda.service.payment.validation.cart;

import com.ecommerce.davivienda.entity.cart.Cart;

/**
 * Interfaz de servicio de validación de carritos para procesamiento de pagos.
 * Define operaciones de validación relacionadas con carritos.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface PaymentCartValidationService {

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
}

