package com.ecommerce.davivienda.controller.document;

import com.ecommerce.davivienda.dto.document.DocumentTypeRequestDto;
import com.ecommerce.davivienda.dto.document.DocumentTypeResponseDto;
import com.ecommerce.davivienda.models.Response;
import com.ecommerce.davivienda.service.document.DocumentTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ecommerce.davivienda.constants.Constants.*;

/**
 * Controlador REST para gestionar tipos de documento.
 * Proporciona endpoints para operaciones CRUD sobre tipos de documento.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/document-types")
@RequiredArgsConstructor
public class DocumentTypeController {

    private final DocumentTypeService documentTypeService;

    /**
     * Obtiene todos los tipos de documento disponibles en el sistema.
     *
     * @return ResponseEntity con lista de tipos de documento
     */
    @GetMapping
    public ResponseEntity<Response<List<DocumentTypeResponseDto>>> findAll() {
        log.info("Solicitud GET: Listar todos los tipos de documento");
        
        List<DocumentTypeResponseDto> documentTypes = documentTypeService.findAll();
        
        Response<List<DocumentTypeResponseDto>> response = Response.<List<DocumentTypeResponseDto>>builder()
                .failure(false)
                .code(HttpStatus.OK.value())
                .message(SUCCESS_DOCUMENT_TYPES_LISTED)
                .body(documentTypes)
                .timestamp(String.valueOf(System.currentTimeMillis()))
                .build();
        
        log.info("Respuesta exitosa: {} tipos de documento encontrados", documentTypes.size());
        return ResponseEntity.ok(response);
    }


    /**
     * Busca un tipo de documento por su código.
     *
     * @param codigo Código del tipo de documento (ej: "CC", "PA", "CE")
     * @return ResponseEntity con el tipo de documento encontrado
     */
    @GetMapping("/code/{codigo}")
    public ResponseEntity<Response<DocumentTypeResponseDto>> findByCode(@PathVariable String codigo) {
        log.info("Solicitud GET: Buscar tipo de documento con código: {}", codigo);
        
        DocumentTypeResponseDto documentType = documentTypeService.findByCode(codigo);
        
        Response<DocumentTypeResponseDto> response = Response.<DocumentTypeResponseDto>builder()
                .failure(false)
                .code(HttpStatus.OK.value())
                .message(SUCCESS_DOCUMENT_TYPE_FOUND)
                .body(documentType)
                .timestamp(String.valueOf(System.currentTimeMillis()))
                .build();
        
        log.info("Tipo de documento encontrado: {} ({})", documentType.getNombre(), documentType.getCodigo());
        return ResponseEntity.ok(response);
    }


}

