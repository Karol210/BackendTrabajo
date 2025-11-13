package com.ecommerce.davivienda.exception.document;

import lombok.Getter;

/**
 * Excepción personalizada para operaciones relacionadas con tipos de documento.
 * Se lanza cuando ocurren errores en las operaciones CRUD de tipos de documento,
 * tales como tipo de documento no encontrado, código duplicado, etc.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Getter
public class DocumentTypeException extends RuntimeException {

    /**
     * Código de error específico de la excepción.
     */
    private final String errorCode;

    /**
     * Constructor para crear una excepción con mensaje y código de error.
     *
     * @param message Mensaje descriptivo del error
     * @param errorCode Código de error específico (ver Constants.CODE_*)
     */
    public DocumentTypeException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Constructor para crear una excepción con mensaje, código de error y causa.
     *
     * @param message Mensaje descriptivo del error
     * @param errorCode Código de error específico (ver Constants.CODE_*)
     * @param cause Excepción original que causó este error
     */
    public DocumentTypeException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
}

