package com.ecommerce.davivienda.service.cartitem.transactional.user;

import com.ecommerce.davivienda.entity.user.DocumentType;
import com.ecommerce.davivienda.entity.user.User;

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

}

