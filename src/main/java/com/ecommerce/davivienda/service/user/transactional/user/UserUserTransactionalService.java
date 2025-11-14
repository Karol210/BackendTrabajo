package com.ecommerce.davivienda.service.user.transactional.user;

import com.ecommerce.davivienda.entity.user.User;

import java.util.List;
import java.util.Optional;

/**
 * Servicio transaccional para operaciones de consulta y persistencia de usuarios.
 * Capacidad interna que NO debe ser expuesta como API REST.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface UserUserTransactionalService {

    /**
     * Verifica si existe un usuario con el correo especificado.
     *
     * @param email Correo a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existsByEmail(String email);

    /**
     * Busca un usuario por ID.
     *
     * @param userId ID del usuario
     * @return Optional con el User si existe
     */
    Optional<User> findUserById(Integer userId);

    /**
     * Busca un usuario por correo electrónico.
     *
     * @param email Correo del usuario
     * @return Optional con el User si existe
     */
    Optional<User> findUserByEmail(String email);

    /**
     * Busca un usuario por userRoleId.
     *
     * @param userRoleId ID del userRol
     * @return Optional con el User si existe
     */
    Optional<User> findUserByUserRoleId(Integer userRoleId);

    /**
     * Busca un usuario por tipo de documento y número.
     *
     * @param documentTypeId ID del tipo de documento
     * @param documentNumber Número de documento
     * @return Optional con el User si existe
     */
    Optional<User> findByDocumentTypeAndNumber(Integer documentTypeId, String documentNumber);

    /**
     * Obtiene todos los usuarios del sistema.
     *
     * @return Lista de todos los usuarios
     */
    List<User> findAllUsers();

    /**
     * Guarda un usuario.
     *
     * @param user Usuario a guardar
     * @return Usuario guardado
     */
    User saveUser(User user);
}

