package com.ecommerce.davivienda.service.user.builder;

import com.ecommerce.davivienda.dto.user.UserRequestDto;
import com.ecommerce.davivienda.entity.user.*;

/**
 * Servicio de construcción para operaciones sobre usuarios.
 * Contiene la lógica de construcción y transformación de entidades.
 * Capacidad interna que NO debe ser expuesta como API REST.
 * 
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface UserBuilderService {

    /**
     * Construye una entidad User desde un UserRequestDto.
     * Incluye la creación de credenciales, asignación de roles y estado.
     *
     * @param request Datos del usuario
     * @param documentType Tipo de documento validado
     * @param roles Lista de roles validados
     * @param userStatus Estado del usuario
     * @param hashedPassword Contraseña ya encriptada
     * @return User construido
     */
    User buildUserFromRequest(
            UserRequestDto request,
            DocumentType documentType,
            java.util.List<Role> roles,
            UserStatus userStatus,
            String hashedPassword
    );

    /**
     * Construye una lista de relaciones UserRole para un usuario.
     *
     * @param userId ID del usuario
     * @param roles Lista de roles a asignar
     * @return Lista de UserRole construidos
     */
    java.util.List<UserRole> buildUserRoles(Integer userId, java.util.List<Role> roles);

    /**
     * Construye credenciales para un usuario.
     *
     * @param email Correo electrónico
     * @param hashedPassword Contraseña encriptada
     * @return Credentials construidas
     */
    Credentials buildCredentials(String email, String hashedPassword);

    /**
     * Actualiza los campos de un usuario existente desde un request.
     * No actualiza credenciales ni ID.
     *
     * @param user Usuario a actualizar
     * @param request Datos nuevos
     * @param documentType Tipo de documento validado
     * @param userStatus Estado actualizado
     */
    void updateUserFields(
            User user,
            UserRequestDto request,
            DocumentType documentType,
            UserStatus userStatus
    );
}

