package com.ecommerce.davivienda.service.user.transactional.role;

import com.ecommerce.davivienda.entity.user.Role;
import com.ecommerce.davivienda.entity.user.UserRole;

import java.util.List;
import java.util.Optional;

/**
 * Servicio transaccional para operaciones de consulta y persistencia de Role y UserRole.
 * Capacidad interna que NO debe ser expuesta como API REST.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface UserRoleTransactionalService {

    /**
     * Busca un rol por ID.
     *
     * @param roleId ID del rol
     * @return Optional con el Role si existe
     */
    Optional<Role> findRoleById(Integer roleId);

    /**
     * Busca un rol por nombre.
     *
     * @param nombreRol Nombre del rol (ej: "Cliente", "Administrador")
     * @return Optional con el Role si existe
     */
    Optional<Role> findRoleByNombre(String nombreRol);

    /**
     * Guarda una lista de UserRole.
     *
     * @param userRoles Lista de UserRole a guardar
     * @return Lista de UserRole guardados
     */
    List<UserRole> saveAllUserRoles(List<UserRole> userRoles);

    /**
     * Elimina todos los UserRole de un usuario por su userId.
     * Este método elimina TODOS los roles del usuario de la BD.
     *
     * @param userId ID del usuario
     */
    void deleteAllUserRolesByUserId(Integer userId);

}

