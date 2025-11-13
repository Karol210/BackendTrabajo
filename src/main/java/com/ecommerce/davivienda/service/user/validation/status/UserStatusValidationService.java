package com.ecommerce.davivienda.service.user.validation.status;

import com.ecommerce.davivienda.entity.user.UserStatus;

/**
 * Servicio de validación para operaciones sobre UserStatus.
 * Capacidad interna que NO debe ser expuesta como API REST.
 * 
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface UserStatusValidationService {

    /**
     * Busca un estado de usuario por nombre.
     *
     * @param statusName Nombre del estado (ej: "Activo", "Inactivo")
     * @return UserStatus encontrado
     * @throws com.ecommerce.davivienda.exception.user.UserException si no existe
     */
    UserStatus findUserStatusByName(String statusName);

    /**
     * Busca un estado de usuario por ID y valida que exista.
     *
     * @param statusId ID del estado
     * @return UserStatus encontrado
     * @throws com.ecommerce.davivienda.exception.user.UserException si no existe
     */
    UserStatus findUserStatusById(Integer statusId);

    /**
     * Obtiene el UserStatus actualizado por ID o mantiene el actual si no hay cambios.
     *
     * @param requestStatusId ID del estado del request (puede ser null)
     * @param currentUserStatus Estado actual del usuario
     * @return UserStatus validado o el actual si no hay cambios
     * @throws com.ecommerce.davivienda.exception.user.UserException si el nuevo estado no existe
     */
    UserStatus getUpdatedUserStatus(Integer requestStatusId, UserStatus currentUserStatus);

    /**
     * Obtiene el UserStatus actualizado por nombre o mantiene el actual si no hay cambios.
     *
     * @param requestStatusName Nombre del estado del request (puede ser null)
     * @param currentUserStatus Estado actual del usuario
     * @return UserStatus validado o el actual si no hay cambios
     * @throws com.ecommerce.davivienda.exception.user.UserException si el nuevo estado no existe
     */
    UserStatus getUpdatedUserStatusByName(String requestStatusName, UserStatus currentUserStatus);
}

