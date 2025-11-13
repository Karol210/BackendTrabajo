package com.ecommerce.davivienda.mapper.product;

import com.ecommerce.davivienda.entity.product.Product;
import com.ecommerce.davivienda.models.product.ProductRequest;
import com.ecommerce.davivienda.models.product.ProductResponse;
import com.ecommerce.davivienda.models.product.ProductUpdateRequest;
import org.mapstruct.*;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

/**
 * Mapper para conversiones entre Product y models.
 * MapStruct genera la implementación automáticamente en tiempo de compilación.
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
}

