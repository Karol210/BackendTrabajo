package com.ecommerce.davivienda.dto.stock;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO para la respuesta de validación de stock de un carrito.
 * Indica si hay stock suficiente y lista los productos con problemas.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StockValidationResponseDto {

    /**
     * Indica si hay stock suficiente para todos los productos del carrito.
     * true = stock suficiente, false = stock insuficiente.
     */
    @JsonProperty("available")
    private Boolean available;

    /**
     * Mensaje descriptivo del resultado de la validación.
     */
    @JsonProperty("message")
    private String message;

    /**
     * Lista de productos con stock insuficiente.
     * Vacía si available = true.
     */
    @JsonProperty("insufficientStockProducts")
    private List<ProductStockDetailDto> insufficientStockProducts;

    /**
     * Cantidad total de productos en el carrito.
     */
    @JsonProperty("totalProductsInCart")
    private Integer totalProductsInCart;

    /**
     * Cantidad de productos con stock insuficiente.
     */
    @JsonProperty("productsWithIssues")
    private Integer productsWithIssues;
}

