package com.ecommerce.davivienda.service.user.validation;

import com.ecommerce.davivienda.dto.user.UserRequestDto;
import com.ecommerce.davivienda.entity.user.DocumentType;
import com.ecommerce.davivienda.entity.user.Role;
import com.ecommerce.davivienda.entity.user.User;
import com.ecommerce.davivienda.entity.user.UserStatus;

/**
 * Servicio de validación para operaciones sobre usuarios.
 * Contiene la lógica de validación de negocio y consultas a base de datos.
 * Capacidad interna que NO debe ser expuesta como API REST.
 * 
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface UserValidationService {

    /**
     * Valida que el correo electrónico no exista en el sistema.
     *
     * @param email Correo a validar
     * @throws com.ecommerce.davivienda.exception.user.UserException si el correo ya existe
     */
    void validateEmailNotExists(String email);

    /**
     * Valida que el tipo de documento exista.
     *
     * @param documentTypeId ID del tipo de documento
     * @return DocumentType encontrado
     * @throws com.ecommerce.davivienda.exception.user.UserException si no existe
     */
    DocumentType validateDocumentType(Integer documentTypeId);

    /**
     * Valida que la contraseña no esté vacía.
     *
     * @param password Contraseña a validar
     * @throws com.ecommerce.davivienda.exception.user.UserException si está vacía
     */
    void validatePasswordNotEmpty(String password);

    /**
     * Busca un rol por ID y valida que exista.
     *
     * @param roleId ID del rol
     * @return Role encontrado
     * @throws com.ecommerce.davivienda.exception.user.UserException si no existe
     */
    Role findRoleById(Integer roleId);

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
     * Valida los datos de actualización de un usuario.
     * Verifica que el ID no sea nulo y que el usuario exista.
     *
     * @param request Request con datos de actualización
     * @throws com.ecommerce.davivienda.exception.user.UserException si la validación falla
     */
    void validateUpdateRequest(UserRequestDto request);

    /**
     * Busca un estado de usuario por nombre.
     *
     * @param statusName Nombre del estado (ej: "Activo", "Inactivo")
     * @return UserStatus encontrado
     * @throws com.ecommerce.davivienda.exception.user.UserException si no existe
     */
    UserStatus findUserStatusByName(String statusName);

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
     * Valida y obtiene una lista de roles por sus IDs.
     * También valida que no haya roles duplicados.
     *
     * @param roleIds Lista de IDs de roles
     * @return Lista de roles encontrados
     * @throws com.ecommerce.davivienda.exception.user.UserException si algún rol no existe o hay duplicados
     */
    java.util.List<com.ecommerce.davivienda.entity.user.Role> validateAndFindRolesByIds(java.util.List<Integer> roleIds);

    /**
     * Valida las reglas de negocio sobre combinaciones de roles.
     * Regla: Un usuario con rol "Cliente" no puede tener rol "Administrador".
     *
     * @param roles Lista de roles a validar
     * @throws com.ecommerce.davivienda.exception.user.UserException si hay conflicto de roles
     */
    void validateRolesCombination(java.util.List<com.ecommerce.davivienda.entity.user.Role> roles);
}

