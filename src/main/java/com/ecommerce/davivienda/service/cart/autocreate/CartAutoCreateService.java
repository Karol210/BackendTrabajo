package com.ecommerce.davivienda.service.cart.autocreate;

import com.ecommerce.davivienda.entity.cart.Cart;

/**
 * Servicio para la creación automática de carritos.
 * Capacidad especializada que maneja la lógica de crear carritos cuando no existen.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface CartAutoCreateService {

    /**
     * Obtiene un carrito existente o crea uno nuevo si no existe.
     * 
     * @param cartId ID del carrito a buscar
     * @param userRoleId ID del usuario_rol para crear el carrito si no existe
     * @return Cart existente o recién creado
     */
    Cart getOrCreateCart(Integer cartId, Integer userRoleId);
}

