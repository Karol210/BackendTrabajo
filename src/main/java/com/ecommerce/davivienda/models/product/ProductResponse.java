package com.ecommerce.davivienda.models.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Response para operaciones sobre productos.
 * Contiene los datos completos del producto incluyendo información de categoría.
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
public class ProductResponse {

    /**
     * ID del producto.
     */
    @JsonProperty("id")
    private Integer id;

    /**
     * Nombre del producto.
     */
    @JsonProperty("name")
    private String name;

    /**
     * Descripción del producto.
     */
    @JsonProperty("description")
    private String description;

    /**
     * Valor unitario del producto (sin IVA).
     */
    @JsonProperty("unitValue")
    private BigDecimal unitValue;

    /**
     * Porcentaje de IVA aplicado.
     */
    @JsonProperty("iva")
    private BigDecimal iva;

    /**
     * Precio total con IVA incluido (calculado).
     */
    @JsonProperty("totalPrice")
    private BigDecimal totalPrice;

    /**
     * URL o ruta de la imagen del producto.
     */
    @JsonProperty("imageUrl")
    private String imageUrl;

    /**
     * ID del estado del producto.
     * 1=Activo, 2=Inactivo, 3=Descontinuado, 4=Agotado, etc.
     */
    @JsonProperty("estadoProductoId")
    private Integer estadoProductoId;

    /**
     * Nombre del estado del producto.
     */
    @JsonProperty("estadoProducto")
    private String estadoProducto;

    /**
     * Cantidad disponible en inventario (desde tabla stock).
     */
    @JsonProperty("inventory")
    private Integer inventory;

    /**
     * ID de la categoría.
     */
    @JsonProperty("categoryId")
    private Integer categoryId;

    /**
     * Nombre de la categoría.
     */
    @JsonProperty("categoryName")
    private String categoryName;

    /**
     * Fecha de creación del producto.
     */
    @JsonProperty("createdAt")
    private LocalDateTime createdAt;

    /**
     * Verifica si el producto está activo.
     *
     * @return true si estadoProductoId == 1
     */
    public boolean isActive() {
        return estadoProductoId != null && estadoProductoId == 1;
    }

    /**
     * Verifica si hay inventario disponible.
     *
     * @return true si hay al menos una unidad en inventario
     */
    public boolean hasInventory() {
        return inventory != null && inventory > 0;
    }

    /**
     * Calcula el monto del IVA aplicado.
     *
     * @return Monto del IVA en pesos
     */
    public BigDecimal getIvaAmount() {
        if (unitValue == null || iva == null || iva.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return unitValue.multiply(iva).divide(BigDecimal.valueOf(100), 2, java.math.RoundingMode.HALF_UP);
    }
}

