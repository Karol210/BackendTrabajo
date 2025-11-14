package com.ecommerce.davivienda.service.category;

import com.ecommerce.davivienda.constants.Constants;
import com.ecommerce.davivienda.dto.category.CategoryResponseDto;
import com.ecommerce.davivienda.entity.product.Category;
import com.ecommerce.davivienda.exception.product.ProductException;
import com.ecommerce.davivienda.mapper.category.CategoryMapper;
import com.ecommerce.davivienda.repository.product.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación del servicio de categorías.
 * Gestiona operaciones de consulta sobre categorías de productos.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDto> getAllCategories() {
        log.debug("Listando todas las categorías");

        List<Category> categories = categoryRepository.findAll();
        log.debug("Se encontraron {} categorías", categories.size());

        return categoryMapper.toResponseDtoList(categories);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponseDto getCategoryById(Integer id) {
        log.debug("Obteniendo categoría por ID: {}", id);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Categoría no encontrada con ID: {}", id);
                    return new ProductException(
                            Constants.ERROR_CATEGORY_NOT_FOUND,
                            Constants.CODE_CATEGORY_NOT_FOUND
                    );
                });

        log.debug("Categoría encontrada: {}", category.getNombre());
        return categoryMapper.toResponseDto(category);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponseDto getCategoryByName(String name) {
        log.debug("Buscando categoría por nombre: {}", name);

        Category category = categoryRepository.findByNombre(name)
                .orElseThrow(() -> {
                    log.error("Categoría no encontrada con nombre: {}", name);
                    return new ProductException(
                            Constants.ERROR_CATEGORY_NOT_FOUND,
                            Constants.CODE_CATEGORY_NOT_FOUND
                    );
                });

        log.debug("Categoría encontrada: {}", category.getNombre());
        return categoryMapper.toResponseDto(category);
    }
}

