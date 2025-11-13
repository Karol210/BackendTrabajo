package com.ecommerce.davivienda.service.product.validation.category;

import com.ecommerce.davivienda.entity.product.Category;

/**
 * Servicio de validación de categorías.
 * Contiene validaciones específicas de categorías de productos.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface ProductCategoryValidationService {

    /**
     * Busca una categoría por ID o lanza excepción si no existe.
     *
     * @param categoryId ID de la categoría
     * @return Categoría encontrada
     * @throws com.ecommerce.davivienda.exception.product.ProductException si no se encuentra
     */
    Category findCategoryByIdOrThrow(Integer categoryId);

    /**
     * Busca una categoría por nombre o lanza excepción si no existe.
     *
     * @param categoryName Nombre de la categoría
     * @return Categoría encontrada
     * @throws com.ecommerce.davivienda.exception.product.ProductException si no se encuentra
     */
    Category findCategoryByNameOrThrow(String categoryName);

    /**
     * Valida que la categoría esté activa.
     *
     * @param category Categoría a validar
     * @throws com.ecommerce.davivienda.exception.product.ProductException si la categoría está inactiva
     */
    void validateCategoryActive(Category category);
}

