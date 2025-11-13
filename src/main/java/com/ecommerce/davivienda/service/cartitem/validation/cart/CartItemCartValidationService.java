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
     * Valida que el item pertenece al carrito del usuario autenticado.
     * Verifica ownership para operaciones de actualización/eliminación.
     *
     * @param itemId ID del item a validar
     * @param userRoleId ID del UserRole del usuario autenticado
     * @throws com.ecommerce.davivienda.exception.CartException si el item no pertenece al usuario
     */
    CartItem validateItemBelongsToUser(Integer itemId, Integer userRoleId);

}

