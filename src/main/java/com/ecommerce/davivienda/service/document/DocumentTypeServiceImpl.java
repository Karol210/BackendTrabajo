package com.ecommerce.davivienda.service.document;

import com.ecommerce.davivienda.dto.document.DocumentTypeResponseDto;
import com.ecommerce.davivienda.entity.user.DocumentType;
import com.ecommerce.davivienda.exception.document.DocumentTypeException;
import com.ecommerce.davivienda.mapper.document.DocumentTypeMapper;
import com.ecommerce.davivienda.repository.user.DocumentTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        log.debug("Consultando todos los tipos de documento");
        
        List<DocumentType> documentTypes = documentTypeRepository.findAll();
        
        log.debug("Se encontraron {} tipos de documento", documentTypes.size());
        return documentTypeMapper.toResponseDtoList(documentTypes);
    }

  
    @Override
    @Transactional(readOnly = true)
    public DocumentTypeResponseDto findByCode(String codigo) {
        log.debug("Buscando tipo de documento con código: {}", codigo);
        
        DocumentType documentType = documentTypeRepository.findByCodigo(codigo)
                .orElseThrow(() -> new DocumentTypeException(
                        ERROR_DOCUMENT_TYPE_NOT_FOUND_BY_CODE,
                        CODE_DOCUMENT_TYPE_NOT_FOUND_BY_CODE
                ));
        
        log.debug("Tipo de documento encontrado: {} ({})", documentType.getNombre(), documentType.getCodigo());
        return documentTypeMapper.toResponseDto(documentType);
    }

}

