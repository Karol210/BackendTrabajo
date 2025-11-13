package com.ecommerce.davivienda.service.user.validation.role;

import java.util.List;

import com.ecommerce.davivienda.entity.user.Role;
import com.ecommerce.davivienda.entity.user.User;
import com.ecommerce.davivienda.entity.user.UserRole;
import com.ecommerce.davivienda.mapper.user.UserMapper;

/**
 * Servicio de validación para operaciones sobre Role.
 * Capacidad interna que NO debe ser expuesta como API REST.
 * 
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface UserRoleValidationService {

    /**
     * Busca un rol por ID y valida que exista.
     *
     * @param roleId ID del rol
     * @return Role encontrado
     * @throws com.ecommerce.davivienda.exception.user.UserException si no existe
     */
    Role findRoleById(Integer roleId);

    /**
     * Busca un rol por nombre y valida que exista.
     *
     * @param nombreRol Nombre del rol (ej: "Cliente", "Administrador")
     * @return Role encontrado
     * @throws com.ecommerce.davivienda.exception.user.UserException si no existe
     */
    Role findRoleByName(String nombreRol);

    /**
     * Valida y obtiene una lista de roles por sus IDs.
     * También valida que no haya roles duplicados.
     *
     * @param roleIds Lista de IDs de roles
     * @return Lista de roles encontrados
     * @throws UserException si algún rol no existe o hay duplicados
     */
    List<Role> validateAndFindRolesByIds(List<Integer> roleIds);

    /**
     * Valida y obtiene una lista de roles por sus nombres.
     * También valida que no haya roles duplicados.
     *
     * @param roleNames Lista de nombres de roles
     * @return Lista de roles encontrados
     * @throws UserException si algún rol no existe o hay duplicados
     */
    List<Role> validateAndFindRolesByNames(List<String> roleNames);

    /**
     * Valida y procesa la actualización de roles de un usuario usando IDs.
     * Maneja validación, eliminación de roles antiguos y asignación de nuevos roles.
     *
     * @param userId ID del usuario
     * @param currentUserRoles Roles actuales del usuario
     * @param newRoleIds IDs de los nuevos roles a asignar
     * @return Lista de UserRole guardados
     * @throws UserException si hay error de validación
     */
    List<UserRole> validateAndUpdateUserRoles(
            Integer userId,
            List<UserRole> currentUserRoles,
            List<Integer> newRoleIds
    );

    /**
     * Valida y procesa la actualización de roles de un usuario usando nombres.
     * Maneja validación, eliminación de roles antiguos y asignación de nuevos roles.
     *
     * @param userId ID del usuario
     * @param currentUserRoles Roles actuales del usuario
     * @param newRoleNames Nombres de los nuevos roles a asignar
     * @return Lista de UserRole guardados
     * @throws UserException si hay error de validación
     */
    List<UserRole> validateAndUpdateUserRolesByNames(
            Integer userId,
            List<UserRole> currentUserRoles,
            List<String> newRoleNames
    );

    /**
     * Procesa la actualización completa de roles de un usuario si el request tiene roles (IDs).
     * Actualiza roles del usuario y asigna rol primario automáticamente.
     *
     * @param user Usuario a actualizar
     * @param requestRoleIds IDs de roles del request (puede ser null o vacío)
     * @param mapper Mapper para asignar el rol primario
     * @return true si se actualizaron roles, false si no había roles en el request
     * @throws UserException si hay error de validación
     */
    boolean processUserRolesUpdate(
            User user,
            List<Integer> requestRoleIds,
            UserMapper mapper
    );

    /**
     * Procesa la actualización completa de roles de un usuario si el request tiene roles (nombres).
     * Actualiza roles del usuario y asigna rol primario automáticamente.
     *
     * @param user Usuario a actualizar
     * @param requestRoleNames Nombres de roles del request (puede ser null o vacío)
     * @param mapper Mapper para asignar el rol primario
     * @return true si se actualizaron roles, false si no había roles en el request
     * @throws UserException si hay error de validación
     */
    boolean processUserRolesUpdateByNames(
            User user,
            List<String> requestRoleNames,
            UserMapper mapper
    );

}

