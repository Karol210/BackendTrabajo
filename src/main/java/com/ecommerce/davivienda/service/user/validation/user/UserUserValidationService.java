package com.ecommerce.davivienda.service.user.validation.user;

import com.ecommerce.davivienda.entity.user.User;

/**
 * Servicio de validación para operaciones sobre entidad User.
 * Capacidad interna que NO debe ser expuesta como API REST.
 * 
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface UserUserValidationService {

    /**
     * Valida que el correo electrónico no exista en el sistema.
     *
     * @param email Correo a validar
     * @throws com.ecommerce.davivienda.exception.user.UserException si el correo ya existe
     */
    void validateEmailNotExists(String email);

    /**
     * Busca un usuario por ID y valida que exista.
     *
     * @param userId ID del usuario
     * @return User encontrado
     * @throws com.ecommerce.davivienda.exception.user.UserException si no existe
     */
    User findUserByIdOrThrow(Integer userId);

    /**
     * Busca un usuario por correo electrónico y valida que exista.
     *
     * @param email Correo del usuario
     * @return User encontrado
     * @throws com.ecommerce.davivienda.exception.user.UserException si no existe
     */
    User findUserByEmailOrThrow(String email);

    /**
     * Busca un usuario por userRoleId y valida que exista.
     *
     * @param userRoleId ID del userRol
     * @return User encontrado
     * @throws com.ecommerce.davivienda.exception.user.UserException si no existe
     */
    User findUserByUserRoleId(Integer userRoleId);

    /**
     * Valida que la combinación de tipo de documento y número no exista.
     *
     * @param documentTypeId ID del tipo de documento
     * @param documentNumber Número de documento
     * @param excludeUserId ID del usuario a excluir (null para creación, userId para actualización)
     * @throws com.ecommerce.davivienda.exception.user.UserException si ya existe
     */
    void validateDocumentCombination(Integer documentTypeId, String documentNumber, Integer excludeUserId);

    /**
     * Valida el email durante actualización solo si cambió.
     *
     * @param newEmail Nuevo email del request
     * @param currentEmail Email actual del usuario
     * @throws com.ecommerce.davivienda.exception.user.UserException si el email ya existe
     */
    void validateEmailUpdate(String newEmail, String currentEmail);

    /**
     * Valida y prepara la combinación de tipo de documento y número para actualización.
     * Maneja los casos donde solo uno de los campos se actualiza.
     *
     * @param requestDocumentTypeId ID del tipo de documento del request (puede ser null)
     * @param requestDocumentNumber Número de documento del request (puede ser null)
     * @param currentDocumentTypeId ID del tipo de documento actual
     * @param currentDocumentNumber Número de documento actual
     * @param userId ID del usuario que se está actualizando
     * @throws com.ecommerce.davivienda.exception.user.UserException si la combinación ya existe
     */
    void validateDocumentUpdateCombination(
            Integer requestDocumentTypeId,
            String requestDocumentNumber,
            Integer currentDocumentTypeId,
            String currentDocumentNumber,
            Integer userId
    );

    /**
     * Valida que el usuario autenticado sea el dueño del recurso.
     * Verifica que el usuarioRolId del usuario autenticado coincida con el del usuario objetivo.
     *
     * @param userId ID del usuario objetivo
     * @param authenticatedUserRoleId ID del usuarioRol del usuario autenticado
     * @throws com.ecommerce.davivienda.exception.user.UserException si no coinciden
     */
    void validateUserOwnership(Integer userId, Integer authenticatedUserRoleId);

    /**
     * Valida que el usuario autenticado sea el dueño del email.
     * Verifica que el email del usuario autenticado coincida con el email proporcionado.
     *
     * @param email Email a validar
     * @param authenticatedUserRoleId ID del usuarioRol del usuario autenticado
     * @throws com.ecommerce.davivienda.exception.user.UserException si no coinciden
     */
    void validateEmailOwnership(String email, Integer authenticatedUserRoleId);

}

