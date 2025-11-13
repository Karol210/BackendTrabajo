package com.ecommerce.davivienda.service.cartitem.transactional.user;

import com.ecommerce.davivienda.entity.user.DocumentType;
import com.ecommerce.davivienda.entity.user.User;
import com.ecommerce.davivienda.entity.user.UserRole;

import java.util.Optional;

/**
 * Servicio transaccional para operaciones de consulta de User y UserRole.
 * Responsabilidad: Acceso a datos de usuarios, roles y tipos de documento.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface CartItemUserTransactionalService {

    /**
     * Verifica si existe un UserRole por ID.
     *
     * @param userRoleId ID del UserRole
     * @return true si existe, false en caso contrario
     */
    boolean existsUserRoleById(Integer userRoleId);

    /**
     * Busca un UserRole por ID.
     *
     * @param userRoleId ID del UserRole
     * @return Optional con el UserRole si existe
     */
    Optional<UserRole> findUserRoleById(Integer userRoleId);

    /**
     * Busca un tipo de documento por código.
     *
     * @param codigo Código del tipo de documento (ej: "CC", "TI", "CE")
     * @return Optional con el DocumentType si existe
     */
    Optional<DocumentType> findDocumentTypeByCodigo(String codigo);

    /**
     * Busca un usuario por tipo de documento y número.
     *
     * @param documentoId ID del tipo de documento
     * @param numeroDeDoc Número de documento
     * @return Optional con el usuario si existe
     */
    Optional<User> findUserByDocumentTypeAndNumber(Integer documentoId, String numeroDeDoc);

    /**
     * Busca un usuario por correo electrónico.
     *
     * @param email Correo electrónico del usuario
     * @return Optional con el usuario si existe
     */
    Optional<User> findUserByEmail(String email);
}

