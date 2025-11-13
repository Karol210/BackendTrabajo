package com.ecommerce.davivienda.service.product.transactional.category;

import com.ecommerce.davivienda.entity.product.Category;

import java.util.Optional;

/**
 * Servicio transaccional para operaciones de consulta de categorías.
 * Maneja todas las operaciones de acceso a datos de categorías.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface ProductCategoryTransactionalService {

    /**
     * Busca una categoría por ID.
     *
     * @param categoryId ID de la categoría
     * @return Optional con la categoría si existe
     */
    Optional<Category> findCategoryById(Integer categoryId);

    /**
     * Busca una categoría por nombre.
     *
     * @param categoryName Nombre de la categoría
     * @return Optional con la categoría si existe
     */
    Optional<Category> findCategoryByName(String categoryName);
}

