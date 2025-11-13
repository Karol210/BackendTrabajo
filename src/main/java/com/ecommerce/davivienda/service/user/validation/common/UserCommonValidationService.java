package com.ecommerce.davivienda.service.user.validation.common;

/**
 * Servicio de validación genérica para usuarios.
 * Capacidad interna que NO debe ser expuesta como API REST.
 * 
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface UserCommonValidationService {

    /**
     * Valida que la contraseña no esté vacía.
     *
     * @param password Contraseña a validar
     * @throws com.ecommerce.davivienda.exception.user.UserException si está vacía
     */
    void validatePasswordNotEmpty(String password);
}

