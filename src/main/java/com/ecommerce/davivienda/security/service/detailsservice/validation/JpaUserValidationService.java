package com.ecommerce.davivienda.security.service.detailsservice.validation;

import com.ecommerce.davivienda.entity.user.User;

/**
 * Servicio para validaciones de usuarios en el contexto de autenticación.
 * Responsable de validar el estado del usuario y su rol.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface JpaUserValidationService {

    /**
     * Valida el estado del usuario y su rol.
     *
     * @param user Usuario a validar
     * @param email Email del usuario (para logs y mensajes de error)
     * @throws org.springframework.security.core.userdetails.UsernameNotFoundException 
     *         si el usuario o su rol no están activos
     */
    void validateUserStatus(User user, String email);
}

