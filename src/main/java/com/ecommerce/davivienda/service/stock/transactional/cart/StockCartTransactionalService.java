package com.ecommerce.davivienda.service.stock.transactional.cart;

import com.ecommerce.davivienda.entity.cart.Cart;
import com.ecommerce.davivienda.entity.cart.CartItem;

import java.util.List;

/**
 * Servicio transaccional para operaciones de consulta de carritos en el contexto de stock.
 * Maneja acceso a datos de carritos e items necesarios para validación de inventario.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface StockCartTransactionalService {

    /**
     * Busca el carrito de un usuario por userRoleId.
     *
     * @param userRoleId ID del rol de usuario
     * @return Cart del usuario
     */
    Cart getCartByUserRoleId(Integer userRoleId);

    /**
     * Obtiene todos los items de un carrito.
     *
     * @param cartId ID del carrito
     * @return Lista de items del carrito
     */
    List<CartItem> getCartItems(Integer cartId);
}

