package com.ecommerce.davivienda.mapper.category;

import com.ecommerce.davivienda.dto.category.CategoryResponseDto;
import com.ecommerce.davivienda.entity.product.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper para conversiones entre Category y DTOs.
 * MapStruct genera la implementación automáticamente en tiempo de compilación.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Mapper(componentModel = "spring")
public interface CategoryMapper {

    /**
     * Convierte entidad Category a CategoryResponseDto.
     *
     * @param category Entidad de categoría
     * @return DTO de respuesta
     */
    @Mapping(source = "categoriaId", target = "id")
    @Mapping(source = "nombre", target = "name")
    @Mapping(source = "descripcion", target = "description")
    CategoryResponseDto toResponseDto(Category category);

    /**
     * Convierte lista de entidades Category a lista de DTOs.
     *
     * @param categories Lista de entidades
     * @return Lista de DTOs
     */
    List<CategoryResponseDto> toResponseDtoList(List<Category> categories);
}

