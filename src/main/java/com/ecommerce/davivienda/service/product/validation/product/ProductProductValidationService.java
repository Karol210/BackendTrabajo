package com.ecommerce.davivienda.service.product.validation.product;

import com.ecommerce.davivienda.entity.product.Product;

/**
 * Servicio de validación de productos.
 * Contiene validaciones específicas de productos.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface ProductProductValidationService {

    /**
     * Valida que no exista un producto con el nombre dado.
     *
     * @param name Nombre del producto
     * @throws com.ecommerce.davivienda.exception.product.ProductException si el nombre ya existe
     */
    void validateProductNameNotExists(String name);

    /**
     * Valida que no exista un producto con el nombre dado, excluyendo un ID específico.
     *
     * @param name Nombre del producto
     * @param productId ID del producto a excluir
     * @throws com.ecommerce.davivienda.exception.product.ProductException si el nombre ya existe
     */
    void validateProductNameNotExistsOnUpdate(String name, Integer productId);

    /**
     * Busca un producto por ID o lanza excepción si no existe.
     *
     * @param productId ID del producto
     * @return Producto encontrado
     * @throws com.ecommerce.davivienda.exception.product.ProductException si no se encuentra
     */
    Product findProductByIdOrThrow(Integer productId);

    /**
     * Valida que la cantidad de inventario sea válida.
     *
     * @param quantity Cantidad de inventario
     * @throws com.ecommerce.davivienda.exception.product.ProductException si la cantidad es inválida
     */
    void validateInventoryQuantity(Integer quantity);
}

