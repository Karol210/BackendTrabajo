package com.ecommerce.davivienda.service.user.transactional.document;

import com.ecommerce.davivienda.entity.user.DocumentType;
import com.ecommerce.davivienda.repository.user.DocumentTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Implementación del servicio transaccional para DocumentType.
 * Centraliza operaciones de acceso a datos de tipos de documento.
 * Capacidad interna que NO debe ser expuesta como API REST.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserDocumentTransactionalServiceImpl implements UserDocumentTransactionalService {

    private final DocumentTypeRepository documentTypeRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<DocumentType> findDocumentTypeById(Integer documentTypeId) {
        log.debug("Buscando tipo de documento por ID: {}", documentTypeId);
        return documentTypeRepository.findById(documentTypeId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DocumentType> findDocumentTypeByNameOrCode(String documentType) {
        log.debug("Buscando tipo de documento por nombre o código: {}", documentType);
        
        Optional<DocumentType> result = documentTypeRepository.findByCodigo(documentType);
        if (result.isPresent()) {
            log.debug("Tipo de documento encontrado por código: {}", documentType);
            return result;
        }
        
        result = documentTypeRepository.findByNombre(documentType);
        if (result.isPresent()) {
            log.debug("Tipo de documento encontrado por nombre: {}", documentType);
        }
        
        return result;
    }
}

