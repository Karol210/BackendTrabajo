package com.ecommerce.davivienda.exception;

import lombok.Getter;

/**
 * Excepción personalizada para operaciones del carrito de compras.
 * Se lanza cuando ocurren errores en la gestión de carritos o items.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Getter
public class CartException extends RuntimeException {

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
    public CartException(String message, String errorCode) {
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
    public CartException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
}

