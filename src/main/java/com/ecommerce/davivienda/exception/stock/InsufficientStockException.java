package com.ecommerce.davivienda.exception.stock;

import com.ecommerce.davivienda.dto.stock.ProductStockDetailDto;
import lombok.Getter;

import java.util.List;

/**
 * Excepción personalizada para operaciones de validación de stock.
 * Se lanza cuando no hay stock suficiente para uno o más productos del carrito.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Getter
public class InsufficientStockException extends RuntimeException {

    /**
     * Código de error específico de la excepción.
     */
    private final String errorCode;

    /**
     * Lista de productos con stock insuficiente.
     */
    private final List<ProductStockDetailDto> insufficientStockProducts;

    /**
     * Constructor para crear una excepción con mensaje y código de error.
     *
     * @param message Mensaje descriptivo del error
     * @param errorCode Código de error específico (ver Constants.CODE_*)
     * @param insufficientStockProducts Lista de productos con stock insuficiente
     */
    public InsufficientStockException(String message, String errorCode, 
                                     List<ProductStockDetailDto> insufficientStockProducts) {
        super(message);
        this.errorCode = errorCode;
        this.insufficientStockProducts = insufficientStockProducts;
    }

    /**
     * Constructor para crear una excepción con mensaje, código de error y causa.
     *
     * @param message Mensaje descriptivo del error
     * @param errorCode Código de error específico (ver Constants.CODE_*)
     * @param insufficientStockProducts Lista de productos con stock insuficiente
     * @param cause Excepción original que causó este error
     */
    public InsufficientStockException(String message, String errorCode, 
                                     List<ProductStockDetailDto> insufficientStockProducts, 
                                     Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.insufficientStockProducts = insufficientStockProducts;
    }
}

