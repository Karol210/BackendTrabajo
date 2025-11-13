package com.ecommerce.davivienda.service.product.transactional.category;

import com.ecommerce.davivienda.entity.product.Category;
import com.ecommerce.davivienda.repository.product.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Implementación del servicio transaccional para operaciones de consulta de categorías.
 * Centraliza todas las operaciones de acceso a datos de categorías.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductCategoryTransactionalServiceImpl implements ProductCategoryTransactionalService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<Category> findCategoryById(Integer categoryId) {
        log.debug("Buscando categoría con ID: {}", categoryId);
        return categoryRepository.findById(categoryId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Category> findCategoryByName(String categoryName) {
        log.debug("Buscando categoría con nombre: {}", categoryName);
        return categoryRepository.findByNombreIgnoreCase(categoryName);
    }
}

