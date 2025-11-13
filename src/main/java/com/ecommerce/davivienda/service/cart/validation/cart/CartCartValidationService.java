package com.ecommerce.davivienda.service.cart.validation.cart;

/**
 * Servicio de validación para operaciones relacionadas con carritos.
 * Proporciona métodos de validación de negocio para carritos.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface CartCartValidationService {

    /**
     * Valida que el usuario no tenga un carrito existente.
     *
     * @param usuarioRolId ID del usuarioRol a verificar
     * @throws com.ecommerce.davivienda.exception.cart.CartException si ya existe un carrito
     */
    void validateUserHasNoCart(Integer usuarioRolId);
}

