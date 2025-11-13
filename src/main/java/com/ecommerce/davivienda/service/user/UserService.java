package com.ecommerce.davivienda.service.user;

import com.ecommerce.davivienda.models.Response;
import com.ecommerce.davivienda.models.user.UserRequest;
import com.ecommerce.davivienda.models.user.UserUpdateRequest;

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
     * @return Response con mensaje de éxito
     * @throws com.ecommerce.davivienda.exception.user.UserException si la validación falla
     */
    Response<String> createUser(UserRequest request);



    /**
     * Actualiza un usuario existente de forma parcial.
     * Solo actualiza los campos que no sean null en el request.
     *
     * @param id ID del usuario a actualizar
     * @param request Datos a actualizar del usuario (solo campos no-null)
     * @return Response con mensaje de éxito
     * @throws com.ecommerce.davivienda.exception.user.UserException si la validación falla
     */
    Response<String> updateUser(Integer id, UserUpdateRequest request);


    /**
     * Recuperación de contraseña (PÚBLICO - sin autenticación).
     * Cambia la contraseña y envía correo de notificación si envioCorreo=true.
     *
     * @param email Email del usuario
     * @param newPassword Nueva contraseña (será encriptada)
     * @param envioCorreo Indica si se debe enviar correo de notificación
     * @return Response con mensaje de éxito
     * @throws com.ecommerce.davivienda.exception.user.UserException si el usuario no existe
     */
    Response<String> recoverPassword(String email, String newPassword, Boolean envioCorreo);

    /**
     * Cambio de contraseña (PRIVADO - requiere autenticación).
     * Valida que el usuario autenticado sea el dueño del email.
     *
     * @param email Email del usuario (debe coincidir con el del token)
     * @param newPassword Nueva contraseña (será encriptada)
     * @return Response con mensaje de éxito
     * @throws com.ecommerce.davivienda.exception.user.UserException si el usuario no existe o no coincide
     */
    Response<String> changePasswordAuthenticated(String email, String newPassword);
}

