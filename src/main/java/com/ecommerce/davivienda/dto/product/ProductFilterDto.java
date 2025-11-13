package com.ecommerce.davivienda.dto.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para filtros de búsqueda de productos.
 * Permite filtrar por categoría, rango de precios, estado y término de búsqueda.
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
public class ProductFilterDto {

    /**
     * ID de la categoría para filtrar.
     */
    @JsonProperty("categoryId")
    private Integer categoryId;

    /**
     * Precio mínimo para el filtro de rango.
     */
    @JsonProperty("minPrice")
    private BigDecimal minPrice;

    /**
     * Precio máximo para el filtro de rango.
     */
    @JsonProperty("maxPrice")
    private BigDecimal maxPrice;

    /**
     * Filtrar por estado activo/inactivo.
     */
    @JsonProperty("active")
    private Boolean active;

    /**
     * Término de búsqueda para nombre o descripción.
     */
    @JsonProperty("searchTerm")
    private String searchTerm;

    /**
     * Verifica si hay algún filtro aplicado.
     *
     * @return true si hay al menos un filtro, false si todos son null
     */
    public boolean hasFilters() {
        return categoryId != null || minPrice != null || maxPrice != null 
                || active != null || (searchTerm != null && !searchTerm.trim().isEmpty());
    }
}

