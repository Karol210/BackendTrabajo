package com.ecommerce.davivienda.mapper.stock;

import com.ecommerce.davivienda.dto.stock.ProductStockDetailDto;
import com.ecommerce.davivienda.dto.stock.StockValidationResponseDto;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;

import static com.ecommerce.davivienda.constants.Constants.SUCCESS_STOCK_AVAILABLE;

/**
 * Mapper para construcción de DTOs relacionados con stock.
 * Construye respuestas y objetos de detalle de inventario.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Mapper(componentModel = "spring")
public interface StockMapper {

    /**
     * Construye el DTO con detalles de un producto con stock insuficiente.
     *
     * @param productId ID del producto
     * @param productName Nombre del producto
     * @param requestedQuantity Cantidad solicitada
     * @param availableQuantity Cantidad disponible
     * @return DTO con detalles del stock del producto
     */
    default ProductStockDetailDto buildProductStockDetail(Integer productId, String productName,
                                                           Integer requestedQuantity, Integer availableQuantity) {
        Integer missingQuantity = Math.max(0, requestedQuantity - availableQuantity);

        return ProductStockDetailDto.builder()
                .productId(productId)
                .productName(productName)
                .requestedQuantity(requestedQuantity)
                .availableQuantity(availableQuantity)
                .missingQuantity(missingQuantity)
                .build();
    }

    /**
     * Construye la respuesta exitosa de validación de stock.
     *
     * @param totalProducts Total de productos validados
     * @return Respuesta exitosa de validación
     */
    default StockValidationResponseDto buildSuccessResponse(Integer totalProducts) {
        return StockValidationResponseDto.builder()
                .available(true)
                .message(SUCCESS_STOCK_AVAILABLE)
                .insufficientStockProducts(new ArrayList<>())
                .totalProductsInCart(totalProducts)
                .productsWithIssues(0)
                .build();
    }

    /**
     * Construye la respuesta cuando hay stock insuficiente.
     *
     * @param totalProducts Total de productos en el carrito
     * @param insufficientStockProducts Lista de productos con stock insuficiente
     * @return Respuesta con detalles de productos sin stock suficiente
     */
    default StockValidationResponseDto buildInsufficientStockResponse(
            Integer totalProducts,
            List<ProductStockDetailDto> insufficientStockProducts) {
        
        int productsWithIssues = insufficientStockProducts.size();
        String message = String.format("Stock insuficiente para %d producto(s)", productsWithIssues);
        
        return StockValidationResponseDto.builder()
                .available(false)
                .message(message)
                .insufficientStockProducts(insufficientStockProducts)
                .totalProductsInCart(totalProducts)
                .productsWithIssues(productsWithIssues)
                .build();
    }
}

