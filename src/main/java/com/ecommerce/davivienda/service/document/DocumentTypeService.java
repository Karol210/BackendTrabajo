package com.ecommerce.davivienda.service.document;

import com.ecommerce.davivienda.dto.document.DocumentTypeRequestDto;
import com.ecommerce.davivienda.dto.document.DocumentTypeResponseDto;

import java.util.List;

/**
 * Interface del servicio para operaciones CRUD sobre tipos de documento.
 * Define los contratos de negocio para la gestión de tipos de documento.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface DocumentTypeService {

    /**
     * Obtiene todos los tipos de documento disponibles en el sistema.
     *
     * @return Lista de tipos de documento
     */
    List<DocumentTypeResponseDto> findAll();

    /**
     * Busca un tipo de documento por su ID.
     *
     * @param id Identificador del tipo de documento
     * @return Tipo de documento encontrado
     * @throws com.ecommerce.davivienda.exception.document.DocumentTypeException si no existe
     */
    DocumentTypeResponseDto findById(Integer id);

    /**
     * Busca un tipo de documento por su código.
     *
     * @param codigo Código del tipo de documento (ej: "CC", "PA", "CE")
     * @return Tipo de documento encontrado
     * @throws com.ecommerce.davivienda.exception.document.DocumentTypeException si no existe
     */
    DocumentTypeResponseDto findByCode(String codigo);

    /**
     * Crea un nuevo tipo de documento.
     *
     * @param requestDto DTO con los datos del tipo de documento a crear
     * @return Tipo de documento creado
     * @throws com.ecommerce.davivienda.exception.document.DocumentTypeException si el código o nombre ya existen
     */
    DocumentTypeResponseDto create(DocumentTypeRequestDto requestDto);

    /**
     * Actualiza un tipo de documento existente.
     *
     * @param id Identificador del tipo de documento a actualizar
     * @param requestDto DTO con los datos actualizados
     * @return Tipo de documento actualizado
     * @throws com.ecommerce.davivienda.exception.document.DocumentTypeException si no existe o hay duplicados
     */
    DocumentTypeResponseDto update(Integer id, DocumentTypeRequestDto requestDto);

    /**
     * Elimina un tipo de documento por su ID.
     *
     * @param id Identificador del tipo de documento a eliminar
     * @throws com.ecommerce.davivienda.exception.document.DocumentTypeException si no existe
     */
    void delete(Integer id);
}

