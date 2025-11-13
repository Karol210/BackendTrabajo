package com.ecommerce.davivienda.dto.product;

import com.ecommerce.davivienda.models.product.ProductResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * DTO auxiliar para respuestas paginadas de productos.
 * Contiene la lista de productos y metadatos de paginación.
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
public class PagedProductResponseDto {

    /**
     * Lista de productos de la página actual.
     */
    @JsonProperty("content")
    private List<ProductResponse> content;

    /**
     * Número de página actual (0-indexed).
     */
    @JsonProperty("pageNumber")
    private Integer pageNumber;

    /**
     * Tamaño de la página.
     */
    @JsonProperty("pageSize")
    private Integer pageSize;

    /**
     * Total de elementos en todas las páginas.
     */
    @JsonProperty("totalElements")
    private Long totalElements;

    /**
     * Total de páginas disponibles.
     */
    @JsonProperty("totalPages")
    private Integer totalPages;

    /**
     * Indica si es la primera página.
     */
    @JsonProperty("first")
    private Boolean first;

    /**
     * Indica si es la última página.
     */
    @JsonProperty("last")
    private Boolean last;

    /**
     * Indica si hay una página siguiente.
     */
    @JsonProperty("hasNext")
    private Boolean hasNext;

    /**
     * Indica si hay una página anterior.
     */
    @JsonProperty("hasPrevious")
    private Boolean hasPrevious;

    /**
     * Crea un PagedProductResponseDto a partir de un Page de Spring.
     *
     * @param page Página de productos
     * @return DTO paginado con metadatos
     */
    public static PagedProductResponseDto fromPage(Page<ProductResponse> page) {
        return PagedProductResponseDto.builder()
                .content(page.getContent())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .build();
    }
}

