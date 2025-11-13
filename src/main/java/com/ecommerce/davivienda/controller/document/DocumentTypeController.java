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
     * Busca un tipo de documento por su ID.
     *
     * @param id Identificador del tipo de documento
     * @return ResponseEntity con el tipo de documento encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<Response<DocumentTypeResponseDto>> findById(@PathVariable Integer id) {
        log.info("Solicitud GET: Buscar tipo de documento con ID: {}", id);
        
        DocumentTypeResponseDto documentType = documentTypeService.findById(id);
        
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

    /**
     * Crea un nuevo tipo de documento.
     * Requiere rol de Administrador.
     *
     * @param requestDto DTO con los datos del tipo de documento a crear
     * @return ResponseEntity con el tipo de documento creado
     */
    @PostMapping
    @PreAuthorize("hasRole('Administrador')")
    public ResponseEntity<Response<DocumentTypeResponseDto>> create(
            @Valid @RequestBody DocumentTypeRequestDto requestDto) {
        log.info("Solicitud POST: Crear tipo de documento - nombre: {}, código: {}", 
                requestDto.getNombre(), requestDto.getCodigo());
        
        DocumentTypeResponseDto documentType = documentTypeService.create(requestDto);
        
        Response<DocumentTypeResponseDto> response = Response.<DocumentTypeResponseDto>builder()
                .failure(false)
                .code(HttpStatus.CREATED.value())
                .message(SUCCESS_DOCUMENT_TYPE_CREATED)
                .body(documentType)
                .timestamp(String.valueOf(System.currentTimeMillis()))
                .build();
        
        log.info("Tipo de documento creado exitosamente con ID: {}", documentType.getDocumentoId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Actualiza un tipo de documento existente.
     * Requiere rol de Administrador.
     *
     * @param id Identificador del tipo de documento a actualizar
     * @param requestDto DTO con los datos actualizados
     * @return ResponseEntity con el tipo de documento actualizado
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('Administrador')")
    public ResponseEntity<Response<DocumentTypeResponseDto>> update(
            @PathVariable Integer id,
            @Valid @RequestBody DocumentTypeRequestDto requestDto) {
        log.info("Solicitud PUT: Actualizar tipo de documento ID: {} - nombre: {}, código: {}", 
                id, requestDto.getNombre(), requestDto.getCodigo());
        
        DocumentTypeResponseDto documentType = documentTypeService.update(id, requestDto);
        
        Response<DocumentTypeResponseDto> response = Response.<DocumentTypeResponseDto>builder()
                .failure(false)
                .code(HttpStatus.OK.value())
                .message(SUCCESS_DOCUMENT_TYPE_UPDATED)
                .body(documentType)
                .timestamp(String.valueOf(System.currentTimeMillis()))
                .build();
        
        log.info("Tipo de documento actualizado exitosamente: {} ({})", 
                documentType.getNombre(), documentType.getCodigo());
        return ResponseEntity.ok(response);
    }

    /**
     * Elimina un tipo de documento por su ID.
     * Requiere rol de Administrador.
     *
     * @param id Identificador del tipo de documento a eliminar
     * @return ResponseEntity con mensaje de confirmación
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('Administrador')")
    public ResponseEntity<Response<Void>> delete(@PathVariable Integer id) {
        log.info("Solicitud DELETE: Eliminar tipo de documento con ID: {}", id);
        
        documentTypeService.delete(id);
        
        Response<Void> response = Response.<Void>builder()
                .failure(false)
                .code(HttpStatus.OK.value())
                .message(SUCCESS_DOCUMENT_TYPE_DELETED)
                .timestamp(String.valueOf(System.currentTimeMillis()))
                .build();
        
        log.info("Tipo de documento eliminado exitosamente con ID: {}", id);
        return ResponseEntity.ok(response);
    }
}

