package com.ecommerce.davivienda.service.product.validation;

import com.ecommerce.davivienda.dto.product.ProductRequestDto;
import com.ecommerce.davivienda.entity.product.Category;
import com.ecommerce.davivienda.entity.product.Product;

/**
 * Servicio para validaciones de negocio de productos.
 * Encapsula todas las validaciones relacionadas con operaciones CRUD de productos.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface ProductValidationService {

    /**
     * Valida que no exista un producto con el mismo nombre al crear.
     *
     * @param name Nombre del producto
     * @throws com.ecommerce.davivienda.exception.product.ProductException si el nombre ya existe
     */
    void validateProductNameNotExists(String name);

    /**
     * Valida que no exista otro producto con el mismo nombre al actualizar.
     *
     * @param name Nombre del producto
     * @param productId ID del producto a actualizar
     * @throws com.ecommerce.davivienda.exception.product.ProductException si el nombre ya existe
     */
    void validateProductNameNotExistsOnUpdate(String name, Integer productId);

    /**
     * Valida que los precios sean coherentes (precio final <= precio unitario).
     *
     * @param request Request con los precios
     * @throws com.ecommerce.davivienda.exception.product.ProductException si los precios son inválidos
     */
    void validatePrices(ProductRequestDto request);

    /**
     * Busca una categoría por ID y lanza excepción si no existe.
     *
     * @param categoryId ID de la categoría
     * @return Categoría encontrada
     * @throws com.ecommerce.davivienda.exception.product.ProductException si no existe
     */
    Category findCategoryByIdOrThrow(Integer categoryId);

    /**
     * Valida que la categoría esté activa.
     *
     * @param category Categoría a validar
     * @throws com.ecommerce.davivienda.exception.product.ProductException si está inactiva
     */
    void validateCategoryActive(Category category);

    /**
     * Busca un producto por ID y lanza excepción si no existe.
     *
     * @param productId ID del producto
     * @return Producto encontrado
     * @throws com.ecommerce.davivienda.exception.product.ProductException si no existe
     */
    Product findProductByIdOrThrow(Integer productId);

    /**
     * Valida que la cantidad de inventario sea mayor a 0.
     *
     * @param quantity Cantidad a validar
     * @throws com.ecommerce.davivienda.exception.product.ProductException si es inválida
     */
    void validateInventoryQuantity(Integer quantity);

    /**
     * Valida el request completo de creación de producto.
     *
     * @param request Request a validar
     * @throws com.ecommerce.davivienda.exception.product.ProductException si hay errores
     */
    void validateCreateRequest(ProductRequestDto request);

    /**
     * Valida el request completo de actualización de producto.
     *
     * @param productId ID del producto a actualizar
     * @param request Request a validar
     * @throws com.ecommerce.davivienda.exception.product.ProductException si hay errores
     */
    void validateUpdateRequest(Integer productId, ProductRequestDto request);
}

