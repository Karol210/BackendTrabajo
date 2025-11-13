package com.ecommerce.davivienda.service.cartitem.validation.product;

import com.ecommerce.davivienda.entity.product.Product;

/**
 * Servicio de validación de productos para items del carrito.
 * Responsabilidad: Validar existencia y estado de productos.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface CartItemProductValidationService {

    /**
     * Valida que el producto exista en el sistema.
     *
     * @param productId ID del producto a validar
     * @return Producto validado
     * @throws com.ecommerce.davivienda.exception.product.ProductException si el producto no existe
     */
    Product validateProductExists(Integer productId);

    /**
     * Valida que el producto esté activo y disponible para agregar al carrito.
     *
     * @param product Producto a validar
     * @throws com.ecommerce.davivienda.exception.product.ProductException si el producto está inactivo
     */
    void validateProductActive(Product product);
}

