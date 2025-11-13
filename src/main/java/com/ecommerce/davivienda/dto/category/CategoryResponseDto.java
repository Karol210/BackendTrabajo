package com.ecommerce.davivienda.dto.category;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la respuesta de categorías.
 * Contiene la información básica de una categoría.
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
public class CategoryResponseDto {

    /**
     * ID de la categoría.
     */
    @JsonProperty("id")
    private Integer id;

    /**
     * Nombre de la categoría.
     */
    @JsonProperty("name")
    private String name;

    /**
     * Descripción de la categoría.
     */
    @JsonProperty("description")
    private String description;
}

