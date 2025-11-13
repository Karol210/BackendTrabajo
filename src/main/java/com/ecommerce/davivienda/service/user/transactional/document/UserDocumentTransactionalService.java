package com.ecommerce.davivienda.service.user.transactional.document;

import com.ecommerce.davivienda.entity.user.DocumentType;

import java.util.Optional;

/**
 * Servicio transaccional para operaciones de consulta de DocumentType.
 * Capacidad interna que NO debe ser expuesta como API REST.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface UserDocumentTransactionalService {

    /**
     * Busca un tipo de documento por ID.
     *
     * @param documentTypeId ID del tipo de documento
     * @return Optional con el DocumentType si existe
     */
    Optional<DocumentType> findDocumentTypeById(Integer documentTypeId);

    /**
     * Busca un tipo de documento por nombre.
     *
     * @param nombre Nombre del tipo de documento
     * @return Optional con el DocumentType si existe
     */
    Optional<DocumentType> findDocumentTypeByNombre(String nombre);

    /**
     * Busca un tipo de documento por código.
     *
     * @param codigo Código del tipo de documento (ej: "CC", "PA")
     * @return Optional con el DocumentType si existe
     */
    Optional<DocumentType> findDocumentTypeByCodigo(String codigo);

    /**
     * Busca un tipo de documento por nombre o código.
     * Intenta primero por código (más rápido), luego por nombre.
     *
     * @param documentType Nombre o código del tipo de documento
     * @return Optional con el DocumentType si existe
     */
    Optional<DocumentType> findDocumentTypeByNameOrCode(String documentType);
}

