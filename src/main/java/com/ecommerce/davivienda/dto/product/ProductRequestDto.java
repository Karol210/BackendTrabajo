package com.ecommerce.davivienda.dto.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para la solicitud de creación y actualización de productos.
 * Contiene los datos del producto incluyendo precios, inventario y categoría.
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
public class ProductRequestDto {

    /**
     * Nombre del producto.
     */
    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(min = 3, max = 200, message = "El nombre debe tener entre 3 y 200 caracteres")
    @JsonProperty("name")
    private String name;

    /**
     * Descripción del producto.
     */
    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    @JsonProperty("description")
    private String description;

    /**
     * Valor unitario del producto (sin IVA).
     */
    @NotNull(message = "El valor unitario es obligatorio")
    @DecimalMin(value = "0.01", message = "El valor unitario debe ser mayor a 0")
    @Digits(integer = 8, fraction = 2, message = "El valor unitario debe tener máximo 8 dígitos enteros y 2 decimales")
    @JsonProperty("unitValue")
    private BigDecimal unitValue;

    /**
     * Porcentaje de IVA (0-100).
     * Opcional, default: 0%
     */
    @DecimalMin(value = "0.00", message = "El IVA no puede ser negativo")
    @DecimalMax(value = "100.00", message = "El IVA no puede ser mayor a 100%")
    @Digits(integer = 3, fraction = 2, message = "El IVA debe tener máximo 3 dígitos enteros y 2 decimales")
    @JsonProperty("iva")
    private BigDecimal iva;

    /**
     * URL o ruta de la imagen del producto.
     * Opcional.
     */
    @Size(max = 500, message = "La URL de la imagen no puede exceder 500 caracteres")
    @JsonProperty("imageUrl")
    private String imageUrl;

    /**
     * Cantidad inicial de inventario/stock.
     * Opcional en creación, se manejará en tabla separada.
     */
    @Min(value = 0, message = "El inventario no puede ser negativo")
    @JsonProperty("inventory")
    private Integer inventory;

    /**
     * ID de la categoría del producto.
     */
    @NotNull(message = "La categoría es obligatoria")
    @JsonProperty("categoryId")
    private Integer categoryId;

    /**
     * ID del estado del producto.
     * Opcional en creación (se asigna 1=Activo por defecto).
     * Valores: 1=Activo, 2=Inactivo, 3=Descontinuado, 4=Agotado, etc.
     */
    @JsonProperty("estadoProductoId")
    private Integer estadoProductoId;
}

