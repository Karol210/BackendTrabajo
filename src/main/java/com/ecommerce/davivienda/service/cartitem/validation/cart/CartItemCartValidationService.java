package com.ecommerce.davivienda.service.cartitem.validation.cart;

import com.ecommerce.davivienda.entity.cart.CartItem;

/**
 * Servicio de validación de carritos para items del carrito.
 * Responsabilidad: Validar existencia, ownership y duplicados en carritos.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface CartItemCartValidationService {

    /**
     * Valida que el item pertenece al carrito ACTIVO del usuario autenticado.
     * Verifica ownership y estado del carrito para operaciones de actualización/eliminación.
     * Solo permite operaciones en carritos con estado activo (id_estado_carrito = 1).
     *
     * @param itemId ID del item a validar
     * @param userRoleId ID del UserRole del usuario autenticado
     * @return CartItem validado que pertenece al usuario en carrito activo
     * @throws com.ecommerce.davivienda.exception.CartException si el item no pertenece al usuario o carrito no está activo
     */
    CartItem validateItemBelongsToUser(Integer itemId, Integer userRoleId);

}

