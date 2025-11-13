package com.ecommerce.davivienda.service.category;

import com.ecommerce.davivienda.dto.category.CategoryResponseDto;

import java.util.List;

/**
 * Servicio para gestionar operaciones de categorías.
 * Proporciona métodos para consultar categorías del catálogo.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface CategoryService {

    /**
     * Lista todas las categorías disponibles.
     *
     * @return Lista de todas las categorías
     */
    List<CategoryResponseDto> getAllCategories();

    /**
     * Obtiene una categoría por su ID.
     *
     * @param id ID de la categoría
     * @return DTO de la categoría
     */
    CategoryResponseDto getCategoryById(Integer id);

    /**
     * Busca una categoría por su nombre.
     *
     * @param name Nombre de la categoría
     * @return DTO de la categoría
     */
    CategoryResponseDto getCategoryByName(String name);
}

