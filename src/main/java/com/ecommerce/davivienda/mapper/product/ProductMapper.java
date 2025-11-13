package com.ecommerce.davivienda.mapper.product;

import com.ecommerce.davivienda.dto.product.ProductRequestDto;
import com.ecommerce.davivienda.dto.product.ProductResponseDto;
import com.ecommerce.davivienda.entity.product.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper para conversiones entre Product y DTOs.
 * MapStruct genera la implementación automáticamente en tiempo de compilación.
 * 
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Mapper(componentModel = "spring")
public interface ProductMapper {

    /**
     * Convierte ProductRequestDto a entidad Product.
     * Ignora campos que se asignan después (ID, fechas, categoría).
     *
     * @param requestDto DTO con datos del producto
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
    Product toEntity(ProductRequestDto requestDto);

    /**
     * Convierte entidad Product a ProductResponseDto.
     * Mapea campos anidados de la categoría y calcula precio con IVA.
     *
     * @param product Entidad Product
     * @return ProductResponseDto con datos del producto
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
    ProductResponseDto toResponseDto(Product product);

    /**
     * Actualiza campos de Product desde ProductRequestDto.
     * Ignora ID, fechas y categoría (se actualizan por separado).
     *
     * @param requestDto DTO con datos actualizados
     * @param product Entidad Product a actualizar
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
    void updateEntityFromDto(ProductRequestDto requestDto, @MappingTarget Product product);
}

