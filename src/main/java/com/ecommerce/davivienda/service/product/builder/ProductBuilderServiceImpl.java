package com.ecommerce.davivienda.service.product.builder;

import com.ecommerce.davivienda.dto.product.ProductFilterDto;
import com.ecommerce.davivienda.entity.product.Product;
import com.ecommerce.davivienda.repository.product.ProductSpecification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Implementación del servicio de construcción de datos de productos.
 * Contiene lógica para construir especificaciones y configuraciones de paginación.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
public class ProductBuilderServiceImpl implements ProductBuilderService {

    @Override
    public Specification<Product> buildSpecificationFromFilter(ProductFilterDto filter) {
        log.debug("Construyendo especificación desde filtro: {}", filter);
        
        return ProductSpecification.withFilters(
                filter.getCategoryId(),
                filter.getMinPrice(),
                filter.getMaxPrice(),
                filter.getActive(),
                filter.getSearchTerm()
        );
    }

    @Override
    public Specification<Product> buildSpecificationFromParams(
            Integer categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Boolean active,
            String searchTerm) {
        
        log.debug("Construyendo especificación desde parámetros: categoryId={}, minPrice={}, maxPrice={}, active={}, searchTerm={}",
                categoryId, minPrice, maxPrice, active, searchTerm);
        
        return ProductSpecification.withFilters(categoryId, minPrice, maxPrice, active, searchTerm);
    }

    @Override
    public Pageable buildPageable(int page, int size, String sortBy, String sortDir) {
        Sort sort = buildSort(sortBy, sortDir);
        
        log.debug("Construyendo Pageable: page={}, size={}, sortBy={}, sortDir={}", 
                page, size, sortBy, sortDir);
        
        return PageRequest.of(page, size, sort);
    }

    /**
     * Construye un objeto Sort a partir del campo y dirección.
     *
     * @param sortBy Campo para ordenar
     * @param sortDir Dirección de orden (asc/desc)
     * @return Sort configurado
     */
    private Sort buildSort(String sortBy, String sortDir) {
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) 
                ? Sort.Direction.DESC 
                : Sort.Direction.ASC;
        
        return Sort.by(direction, sortBy);
    }
}

