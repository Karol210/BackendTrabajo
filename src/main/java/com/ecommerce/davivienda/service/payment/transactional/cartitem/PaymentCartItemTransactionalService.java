package com.ecommerce.davivienda.service.payment.transactional.cartitem;

import com.ecommerce.davivienda.entity.cart.CartItem;

import java.util.List;

/**
 * Servicio transaccional para operaciones de consulta de CartItem.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface PaymentCartItemTransactionalService {

    /**
     * Obtiene todos los items de un carrito.
     *
     * @param cartId ID del carrito
     * @return Lista de CartItem del carrito
     */
    List<CartItem> findByCartId(Integer cartId);
}

