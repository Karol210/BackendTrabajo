package com.ecommerce.davivienda.service.user;

import com.ecommerce.davivienda.dto.user.UserRequestDto;
import com.ecommerce.davivienda.dto.user.UserResponseDto;
import com.ecommerce.davivienda.dto.user.UserUpdateRequestDto;

/**
 * Servicio principal para operaciones CRUD sobre usuarios.
 * Define las operaciones de negocio para gestión de usuarios.
 * 
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface UserService {

    /**
     * Crea un nuevo usuario en el sistema.
     * Valida datos, encripta contraseña y asigna rol y estado.
     *
     * @param request Datos del usuario a crear
     * @return UserResponseDto con datos del usuario creado
     * @throws com.ecommerce.davivienda.exception.user.UserException si la validación falla
     */
    UserResponseDto createUser(UserRequestDto request);

    /**
     * Obtiene un usuario por su ID.
     *
     * @param id ID del usuario
     * @return UserResponseDto con datos del usuario
     * @throws com.ecommerce.davivienda.exception.user.UserException si el usuario no existe
     */
    UserResponseDto getUserById(Integer id);

    /**
     * Actualiza un usuario existente de forma parcial.
     * Solo actualiza los campos que no sean null en el request.
     *
     * @param id ID del usuario a actualizar
     * @param request Datos a actualizar del usuario (solo campos no-null)
     * @return UserResponseDto con datos del usuario actualizado
     * @throws com.ecommerce.davivienda.exception.user.UserException si la validación falla
     */
    UserResponseDto updateUser(Integer id, UserUpdateRequestDto request);

    /**
     * Elimina un usuario (soft delete - marca como inactivo).
     *
     * @param id ID del usuario a eliminar
     * @return UserResponseDto con datos del usuario eliminado
     * @throws com.ecommerce.davivienda.exception.user.UserException si el usuario no existe
     */
    UserResponseDto deleteUser(Integer id);

    /**
     * Activa un usuario previamente desactivado.
     *
     * @param id ID del usuario a activar
     * @return UserResponseDto con datos del usuario activado
     * @throws com.ecommerce.davivienda.exception.user.UserException si el usuario no existe
     */
    UserResponseDto activateUser(Integer id);

    /**
     * Cambia la contraseña de un usuario identificado por su email.
     *
     * @param email Email del usuario
     * @param newPassword Nueva contraseña (será encriptada)
     * @return UserResponseDto con datos del usuario
     * @throws com.ecommerce.davivienda.exception.user.UserException si el usuario no existe
     */
    UserResponseDto changePassword(String email, String newPassword);
}

