package com.ecommerce.davivienda.service.auth;

/**
 * Servicio genérico para gestión de autenticación de usuarios.
 * Responsable de extraer y validar el usuario autenticado desde el token JWT.
 * Reutilizable por todos los módulos del sistema.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface AuthUserService {

    /**
     * Obtiene y valida el userRoleId del usuario autenticado.
     * Extrae el email del token JWT, obtiene el userRoleId y valida que tenga rol de cliente.
     *
     * @return userRoleId del usuario autenticado con rol de cliente
     */
    Integer getAuthenticatedUserRoleId();
}

