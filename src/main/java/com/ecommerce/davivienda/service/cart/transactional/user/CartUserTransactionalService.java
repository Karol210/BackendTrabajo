package com.ecommerce.davivienda.service.cart.transactional.user;

import com.ecommerce.davivienda.entity.user.User;
import com.ecommerce.davivienda.entity.user.UserRole;

import java.util.List;
import java.util.Optional;

/**
 * Servicio transaccional para operaciones de consulta de User relacionadas con Cart.
 * Maneja operaciones de acceso a datos de usuarios necesarias para gestión de carritos.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface CartUserTransactionalService {

    /**
     * Busca un usuario por email.
     *
     * @param email Email del usuario
     * @return Optional con el usuario si existe
     */
    Optional<User> findUserByEmail(String email);

    /**
     * Busca los roles de un usuario.
     *
     * @param usuarioId ID del usuario
     * @return Lista de UserRole del usuario
     */
    List<UserRole> findUserRolesByUserId(Integer usuarioId);

    /**
     * Verifica si existe un UserRole por ID.
     *
     * @param userRoleId ID del UserRole
     * @return true si existe, false en caso contrario
     */
    boolean existsUserRoleById(Integer userRoleId);
}

