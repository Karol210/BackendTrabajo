package com.ecommerce.davivienda.dto.stock;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa el detalle de un producto con stock insuficiente.
 * Contiene información del producto, cantidad solicitada y disponible.
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
public class ProductStockDetailDto {

    /**
     * ID del producto.
     */
    @JsonProperty("productId")
    private Integer productId;

    /**
     * Nombre del producto.
     */
    @JsonProperty("productName")
    private String productName;

    /**
     * Cantidad solicitada en el carrito.
     */
    @JsonProperty("requestedQuantity")
    private Integer requestedQuantity;

    /**
     * Cantidad disponible en inventario.
     */
    @JsonProperty("availableQuantity")
    private Integer availableQuantity;

    /**
     * Cantidad faltante (requestedQuantity - availableQuantity).
     */
    @JsonProperty("missingQuantity")
    private Integer missingQuantity;
}

