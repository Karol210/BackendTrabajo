package com.ecommerce.davivienda.service.user.transactional.status;

import com.ecommerce.davivienda.entity.user.UserStatus;

import java.util.Optional;

/**
 * Servicio transaccional para operaciones de consulta de UserStatus.
 * Capacidad interna que NO debe ser expuesta como API REST.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface UserStatusTransactionalService {

    /**
     * Busca un estado de usuario por nombre.
     *
     * @param statusName Nombre del estado
     * @return Optional con el UserStatus si existe
     */
    Optional<UserStatus> findUserStatusByName(String statusName);

    /**
     * Busca un estado de usuario por ID.
     *
     * @param statusId ID del estado
     * @return Optional con el UserStatus si existe
     */
    Optional<UserStatus> findUserStatusById(Integer statusId);
}

