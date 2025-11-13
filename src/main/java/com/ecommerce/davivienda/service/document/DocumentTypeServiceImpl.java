package com.ecommerce.davivienda.service.document;

import com.ecommerce.davivienda.dto.document.DocumentTypeRequestDto;
import com.ecommerce.davivienda.dto.document.DocumentTypeResponseDto;
import com.ecommerce.davivienda.entity.user.DocumentType;
import com.ecommerce.davivienda.exception.document.DocumentTypeException;
import com.ecommerce.davivienda.mapper.document.DocumentTypeMapper;
import com.ecommerce.davivienda.repository.user.DocumentTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ecommerce.davivienda.constants.Constants.*;

/**
 * Implementación del servicio para operaciones CRUD sobre tipos de documento.
 * Gestiona la lógica de negocio para tipos de documento.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentTypeServiceImpl implements DocumentTypeService {

    private final DocumentTypeRepository documentTypeRepository;
    private final DocumentTypeMapper documentTypeMapper;

    @Override
    @Transactional(readOnly = true)
    public List<DocumentTypeResponseDto> findAll() {
        log.info("Consultando todos los tipos de documento");
        
        List<DocumentType> documentTypes = documentTypeRepository.findAll();
        
        log.info("Se encontraron {} tipos de documento", documentTypes.size());
        return documentTypeMapper.toResponseDtoList(documentTypes);
    }

    @Override
    @Transactional(readOnly = true)
    public DocumentTypeResponseDto findById(Integer id) {
        log.info("Buscando tipo de documento con ID: {}", id);
        
        DocumentType documentType = documentTypeRepository.findById(id)
                .orElseThrow(() -> new DocumentTypeException(
                        ERROR_DOCUMENT_TYPE_NOT_FOUND_BY_ID,
                        CODE_DOCUMENT_TYPE_NOT_FOUND_BY_ID
                ));
        
        log.info("Tipo de documento encontrado: {} ({})", documentType.getNombre(), documentType.getCodigo());
        return documentTypeMapper.toResponseDto(documentType);
    }

    @Override
    @Transactional(readOnly = true)
    public DocumentTypeResponseDto findByCode(String codigo) {
        log.info("Buscando tipo de documento con código: {}", codigo);
        
        DocumentType documentType = documentTypeRepository.findByCodigo(codigo)
                .orElseThrow(() -> new DocumentTypeException(
                        ERROR_DOCUMENT_TYPE_NOT_FOUND_BY_CODE,
                        CODE_DOCUMENT_TYPE_NOT_FOUND_BY_CODE
                ));
        
        log.info("Tipo de documento encontrado: {} ({})", documentType.getNombre(), documentType.getCodigo());
        return documentTypeMapper.toResponseDto(documentType);
    }

    @Override
    @Transactional
    public DocumentTypeResponseDto create(DocumentTypeRequestDto requestDto) {
        log.info("Creando nuevo tipo de documento: {} ({})", requestDto.getNombre(), requestDto.getCodigo());
        
        validateCodeNotExists(requestDto.getCodigo());
        validateNameNotExists(requestDto.getNombre());
        
        try {
            DocumentType documentType = documentTypeMapper.toEntity(requestDto);
            DocumentType savedDocumentType = documentTypeRepository.save(documentType);
            
            log.info("Tipo de documento creado exitosamente con ID: {}", savedDocumentType.getDocumentoId());
            return documentTypeMapper.toResponseDto(savedDocumentType);
            
        } catch (DataIntegrityViolationException e) {
            log.error("Error de integridad al crear tipo de documento: {}", e.getMessage());
            throw new DocumentTypeException(
                    ERROR_DOCUMENT_TYPE_CODE_EXISTS,
                    CODE_DOCUMENT_TYPE_CODE_EXISTS,
                    e
            );
        }
    }

    @Override
    @Transactional
    public DocumentTypeResponseDto update(Integer id, DocumentTypeRequestDto requestDto) {
        log.info("Actualizando tipo de documento con ID: {}", id);
        
        DocumentType existingDocumentType = documentTypeRepository.findById(id)
                .orElseThrow(() -> new DocumentTypeException(
                        ERROR_DOCUMENT_TYPE_NOT_FOUND_BY_ID,
                        CODE_DOCUMENT_TYPE_NOT_FOUND_BY_ID
                ));
        
        validateCodeNotExistsForUpdate(requestDto.getCodigo(), id);
        validateNameNotExistsForUpdate(requestDto.getNombre(), id);
        
        try {
            documentTypeMapper.updateEntityFromDto(requestDto, existingDocumentType);
            DocumentType updatedDocumentType = documentTypeRepository.save(existingDocumentType);
            
            log.info("Tipo de documento actualizado exitosamente: {} ({})", 
                    updatedDocumentType.getNombre(), updatedDocumentType.getCodigo());
            return documentTypeMapper.toResponseDto(updatedDocumentType);
            
        } catch (DataIntegrityViolationException e) {
            log.error("Error de integridad al actualizar tipo de documento: {}", e.getMessage());
            throw new DocumentTypeException(
                    ERROR_DOCUMENT_TYPE_CODE_EXISTS,
                    CODE_DOCUMENT_TYPE_CODE_EXISTS,
                    e
            );
        }
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        log.info("Eliminando tipo de documento con ID: {}", id);
        
        DocumentType documentType = documentTypeRepository.findById(id)
                .orElseThrow(() -> new DocumentTypeException(
                        ERROR_DOCUMENT_TYPE_NOT_FOUND_BY_ID,
                        CODE_DOCUMENT_TYPE_NOT_FOUND_BY_ID
                ));
        
        try {
            documentTypeRepository.delete(documentType);
            log.info("Tipo de documento eliminado exitosamente: {} ({})", 
                    documentType.getNombre(), documentType.getCodigo());
                    
        } catch (DataIntegrityViolationException e) {
            log.error("Error de integridad al eliminar tipo de documento: {}", e.getMessage());
            throw new DocumentTypeException(
                    "No se puede eliminar el tipo de documento porque está siendo usado por otros registros",
                    CODE_DATA_INTEGRITY_VIOLATION,
                    e
            );
        }
    }

    private void validateCodeNotExists(String codigo) {
        if (documentTypeRepository.existsByCodigo(codigo)) {
            throw new DocumentTypeException(
                    ERROR_DOCUMENT_TYPE_CODE_EXISTS,
                    CODE_DOCUMENT_TYPE_CODE_EXISTS
            );
        }
    }

    private void validateNameNotExists(String nombre) {
        if (documentTypeRepository.findByNombre(nombre).isPresent()) {
            throw new DocumentTypeException(
                    ERROR_DOCUMENT_TYPE_NAME_EXISTS,
                    CODE_DOCUMENT_TYPE_NAME_EXISTS
            );
        }
    }

    private void validateCodeNotExistsForUpdate(String codigo, Integer currentId) {
        documentTypeRepository.findByCodigo(codigo).ifPresent(existing -> {
            if (!existing.getDocumentoId().equals(currentId)) {
                throw new DocumentTypeException(
                        ERROR_DOCUMENT_TYPE_CODE_EXISTS,
                        CODE_DOCUMENT_TYPE_CODE_EXISTS
                );
            }
        });
    }

    private void validateNameNotExistsForUpdate(String nombre, Integer currentId) {
        documentTypeRepository.findByNombre(nombre).ifPresent(existing -> {
            if (!existing.getDocumentoId().equals(currentId)) {
                throw new DocumentTypeException(
                        ERROR_DOCUMENT_TYPE_NAME_EXISTS,
                        CODE_DOCUMENT_TYPE_NAME_EXISTS
                );
            }
        });
    }
}

