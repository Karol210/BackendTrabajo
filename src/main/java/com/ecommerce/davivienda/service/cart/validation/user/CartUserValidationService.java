package com.ecommerce.davivienda.service.cart.validation.user;

import com.ecommerce.davivienda.entity.user.User;
import com.ecommerce.davivienda.entity.user.UserRole;

/**
 * Servicio de validación para operaciones relacionadas con usuarios en contexto de carritos.
 * Proporciona métodos de validación de negocio para usuarios.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface CartUserValidationService {

    /**
     * Valida que el usuario exista por su email.
     *
     * @param email Email del usuario a validar
     * @return Usuario encontrado
     * @throws com.ecommerce.davivienda.exception.cart.CartException si el usuario no existe
     */
    User validateUserExists(String email);

    /**
     * Obtiene el UserRole principal del usuario.
     *
     * @param usuarioId ID del usuario
     * @return UserRole encontrado
     * @throws com.ecommerce.davivienda.exception.cart.CartException si no tiene roles
     */
    UserRole getUserPrimaryRole(Integer usuarioId);

}

