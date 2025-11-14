package com.ecommerce.davivienda.service.document;

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
     * Busca un tipo de documento por su código.
     *
     * @param codigo Código del tipo de documento (ej: "CC", "PA", "CE")
     * @return Tipo de documento encontrado
     * @throws com.ecommerce.davivienda.exception.document.DocumentTypeException si no existe
     */
    DocumentTypeResponseDto findByCode(String codigo);

    
}

