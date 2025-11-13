package com.ecommerce.davivienda.mapper.product;

import com.ecommerce.davivienda.dto.product.ProductFilterDto;
import com.ecommerce.davivienda.entity.product.Product;
import com.ecommerce.davivienda.models.product.ProductRequest;
import com.ecommerce.davivienda.models.product.ProductResponse;
import com.ecommerce.davivienda.models.product.ProductUpdateRequest;
import com.ecommerce.davivienda.repository.product.ProductSpecification;
import org.mapstruct.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

/**
 * Mapper para conversiones entre Product y models.
 * MapStruct genera la implementación automáticamente en tiempo de compilación.
 * Incluye métodos auxiliares para construcción de especificaciones y paginación.
 * 
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Mapper(componentModel = "spring")
public interface ProductMapper {

    /**
     * Convierte ProductRequest a entidad Product.
     * Ignora campos que se asignan después (ID, fechas, categoría).
     *
     * @param request Request con datos del producto
     * @return Entidad Product
     */
    @Mapping(target = "productoId", ignore = true)
    @Mapping(target = "nombre", source = "name")
    @Mapping(target = "descripcion", source = "description")
    @Mapping(target = "valorUnitario", source = "unitValue")
    @Mapping(target = "iva", source = "iva")
    @Mapping(target = "imagen", source = "imageUrl")
    @Mapping(target = "estadoProductoId", source = "estadoProductoId")
    @Mapping(target = "categoria", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    Product toEntity(ProductRequest request);

    /**
     * Convierte entidad Product a ProductResponse.
     * Mapea campos anidados de la categoría y calcula precio con IVA.
     *
     * @param product Entidad Product
     * @return ProductResponse con datos del producto
     */
    @Mapping(target = "id", source = "productoId")
    @Mapping(target = "name", source = "nombre")
    @Mapping(target = "description", source = "descripcion")
    @Mapping(target = "unitValue", source = "valorUnitario")
    @Mapping(target = "iva", source = "iva")
    @Mapping(target = "totalPrice", expression = "java(product.getPrecioConIva())")
    @Mapping(target = "imageUrl", source = "imagen")
    @Mapping(target = "estadoProductoId", source = "estadoProductoId")
    @Mapping(target = "estadoProducto", ignore = true)
    @Mapping(target = "inventory", ignore = true)
    @Mapping(target = "categoryId", source = "categoria.categoriaId")
    @Mapping(target = "categoryName", source = "categoria.nombre")
    @Mapping(target = "createdAt", source = "creationDate")
    ProductResponse toResponseDto(Product product);

    /**
     * Actualiza campos de Product desde ProductUpdateRequest.
     * Solo actualiza campos no nulos (actualización parcial).
     * Ignora ID, fechas y categoría (se actualizan por separado).
     *
     * @param request Request con datos actualizados
     * @param product Entidad Product a actualizar
     */
    @Mapping(target = "productoId", ignore = true)
    @Mapping(target = "nombre", source = "name", nullValuePropertyMappingStrategy = IGNORE)
    @Mapping(target = "descripcion", source = "description", nullValuePropertyMappingStrategy = IGNORE)
    @Mapping(target = "valorUnitario", source = "unitValue", nullValuePropertyMappingStrategy = IGNORE)
    @Mapping(target = "iva", source = "iva", nullValuePropertyMappingStrategy = IGNORE)
    @Mapping(target = "imagen", source = "imageUrl", nullValuePropertyMappingStrategy = IGNORE)
    @Mapping(target = "estadoProductoId", source = "estadoProductoId", nullValuePropertyMappingStrategy = IGNORE)
    @Mapping(target = "categoria", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    void updateEntityFromDto(ProductUpdateRequest request, @MappingTarget Product product);

    /**
     * Construye una especificación de búsqueda a partir de un FilterDto.
     *
     * @param filter Filtros de búsqueda
     * @return Specification para búsqueda dinámica
     */
    default Specification<Product> buildSpecificationFromFilter(ProductFilterDto filter) {
        return ProductSpecification.withFilters(
                filter.getCategoryId(),
                filter.getMinPrice(),
                filter.getMaxPrice(),
                filter.getActive(),
                filter.getSearchTerm()
        );
    }

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
    default Specification<Product> buildSpecificationFromParams(
            Integer categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Boolean active,
            String searchTerm) {
        
        return ProductSpecification.withFilters(categoryId, minPrice, maxPrice, active, searchTerm);
    }

    /**
     * Construye un objeto Pageable con configuración de paginación y ordenamiento.
     *
     * @param page Número de página (0-indexed)
     * @param size Tamaño de página
     * @param sortBy Campo para ordenar
     * @param sortDir Dirección de orden (asc/desc)
     * @return Pageable configurado
     */
    default Pageable buildPageable(int page, int size, String sortBy, String sortDir) {
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) 
                ? Sort.Direction.DESC 
                : Sort.Direction.ASC;
        
        Sort sort = Sort.by(direction, sortBy);
        return PageRequest.of(page, size, sort);
    }
}

