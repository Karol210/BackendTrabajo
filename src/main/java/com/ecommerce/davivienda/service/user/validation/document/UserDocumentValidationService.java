package com.ecommerce.davivienda.service.user.validation.document;

import com.ecommerce.davivienda.entity.user.DocumentType;

/**
 * Servicio de validación para operaciones sobre DocumentType.
 * Capacidad interna que NO debe ser expuesta como API REST.
 * 
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface UserDocumentValidationService {

    /**
     * Valida que el tipo de documento exista por ID.
     *
     * @param documentTypeId ID del tipo de documento
     * @return DocumentType encontrado
     * @throws UserException si no existe
     */
    DocumentType validateDocumentType(Integer documentTypeId);

    /**
     * Valida que el tipo de documento exista por nombre o código.
     *
     * @param documentType Nombre o código del tipo de documento (ej: "CC", "Cédula de Ciudadanía")
     * @return DocumentType encontrado
     * @throws UserException si no existe
     */
    DocumentType validateDocumentTypeByName(String documentType);

    /**
     * Obtiene el DocumentType actualizado o mantiene el actual si no hay cambios.
     *
     * @param requestDocumentTypeId ID del tipo de documento del request (puede ser null)
     * @param currentDocumentType Tipo de documento actual del usuario
     * @return DocumentType validado o el actual si no hay cambios
     * @throws UserException si el nuevo tipo no existe
     */
    DocumentType getUpdatedDocumentType(Integer requestDocumentTypeId, DocumentType currentDocumentType);

    /**
     * Obtiene el DocumentType actualizado por nombre o mantiene el actual si no hay cambios.
     *
     * @param requestDocumentType Nombre o código del tipo de documento del request (puede ser null)
     * @param currentDocumentType Tipo de documento actual del usuario
     * @return DocumentType validado o el actual si no hay cambios
     * @throws UserException si el nuevo tipo no existe
     */
    DocumentType getUpdatedDocumentTypeByName(String requestDocumentType, DocumentType currentDocumentType);
}

