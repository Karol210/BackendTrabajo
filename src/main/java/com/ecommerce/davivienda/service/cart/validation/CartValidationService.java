package com.ecommerce.davivienda.service.cart.validation;

import com.ecommerce.davivienda.entity.user.User;
import com.ecommerce.davivienda.entity.user.UserRole;

/**
 * Servicio de validación para operaciones de carritos de compras.
 * Proporciona métodos de validación de negocio para usuarios y carritos.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface CartValidationService {

    /**
     * Valida que el usuario exista por su email.
     *
     * @param email Email del usuario a validar
     * @return Usuario encontrado
     * @throws com.ecommerce.davivienda.exception.cart.CartException si el usuario no existe
     */
    User validateUserExists(String email);

    /**
     * Valida que el usuario no tenga un carrito existente.
     *
     * @param usuarioRolId ID del usuarioRol a verificar
     * @throws com.ecommerce.davivienda.exception.cart.CartException si ya existe un carrito
     */
    void validateUserHasNoCart(Integer usuarioRolId);

    /**
     * Obtiene el UserRole principal del usuario.
     *
     * @param usuarioId ID del usuario
     * @return UserRole encontrado
     * @throws com.ecommerce.davivienda.exception.cart.CartException si no tiene roles
     */
    UserRole getUserPrimaryRole(Integer usuarioId);
}

