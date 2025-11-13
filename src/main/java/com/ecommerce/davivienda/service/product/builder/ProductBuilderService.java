package com.ecommerce.davivienda.service.product.builder;

import com.ecommerce.davivienda.dto.product.ProductFilterDto;
import com.ecommerce.davivienda.entity.product.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

/**
 * Servicio para construcción y transformación de datos de productos.
 * Encapsula lógica de construcción de especificaciones, paginación y ordenamiento.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface ProductBuilderService {

    /**
     * Construye una especificación de búsqueda a partir de un FilterDto.
     *
     * @param filter Filtros de búsqueda
     * @return Specification para búsqueda dinámica
     */
    Specification<Product> buildSpecificationFromFilter(ProductFilterDto filter);

    /**
     * Construye una especificación de búsqueda a partir de parámetros individuales.
     *
     * @param categoryId ID de categoría (opcional)
     * @param minPrice Precio mínimo (opcional)
     * @param maxPrice Precio máximo (opcional)
     * @param active Estado activo/inactivo (opcional)
     * @param searchTerm Término de búsqueda (opcional)
     * @return Specification para búsqueda dinámica
     */
    Specification<Product> buildSpecificationFromParams(
            Integer categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Boolean active,
            String searchTerm);

    /**
     * Construye un objeto Pageable con configuración de paginación y ordenamiento.
     *
     * @param page Número de página (0-indexed)
     * @param size Tamaño de página
     * @param sortBy Campo para ordenar
     * @param sortDir Dirección de orden (asc/desc)
     * @return Pageable configurado
     */
    Pageable buildPageable(int page, int size, String sortBy, String sortDir);
}

