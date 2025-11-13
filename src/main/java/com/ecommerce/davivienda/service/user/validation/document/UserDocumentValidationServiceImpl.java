package com.ecommerce.davivienda.service.user.validation.document;

import com.ecommerce.davivienda.entity.user.DocumentType;
import com.ecommerce.davivienda.exception.user.UserException;
import com.ecommerce.davivienda.service.user.transactional.document.UserDocumentTransactionalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.ecommerce.davivienda.constants.Constants.*;

/**
 * Implementación del servicio de validación para DocumentType.
 * Capacidad interna que NO debe ser expuesta como API REST.
 * 
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserDocumentValidationServiceImpl implements UserDocumentValidationService {

    private final UserDocumentTransactionalService transactionalService;

    @Override
    public DocumentType validateDocumentType(Integer documentTypeId) {
        return transactionalService.findDocumentTypeById(documentTypeId)
                .orElseThrow(() -> {
                    log.error("Tipo de documento no encontrado con ID: {}", documentTypeId);
                    return new UserException(ERROR_DOCUMENT_TYPE_NOT_FOUND, CODE_DOCUMENT_TYPE_NOT_FOUND);
                });
    }

    @Override
    public DocumentType validateDocumentTypeByName(String documentType) {
        return transactionalService.findDocumentTypeByNameOrCode(documentType)
                .orElseThrow(() -> {
                    log.error("Tipo de documento no encontrado: {}", documentType);
                    return new UserException(ERROR_DOCUMENT_TYPE_NOT_FOUND, CODE_DOCUMENT_TYPE_NOT_FOUND);
                });
    }

    @Override
    public DocumentType getUpdatedDocumentType(Integer requestDocumentTypeId, DocumentType currentDocumentType) {
        if (requestDocumentTypeId == null) {
            return currentDocumentType;
        }
        
        log.debug("Validando nuevo tipo de documento por ID: {}", requestDocumentTypeId);
        return validateDocumentType(requestDocumentTypeId);
    }

    @Override
    public DocumentType getUpdatedDocumentTypeByName(String requestDocumentType, DocumentType currentDocumentType) {
        if (requestDocumentType == null || requestDocumentType.trim().isEmpty()) {
            return currentDocumentType;
        }
        
        log.debug("Validando nuevo tipo de documento por nombre: {}", requestDocumentType);
        return validateDocumentTypeByName(requestDocumentType);
    }
}

