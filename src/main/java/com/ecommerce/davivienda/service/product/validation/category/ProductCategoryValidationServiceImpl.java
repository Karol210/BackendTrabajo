package com.ecommerce.davivienda.service.product.validation.category;

import com.ecommerce.davivienda.constants.Constants;
import com.ecommerce.davivienda.entity.product.Category;
import com.ecommerce.davivienda.exception.product.ProductException;
import com.ecommerce.davivienda.service.product.transactional.category.ProductCategoryTransactionalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Implementación del servicio de validación de categorías.
 * Contiene toda la lógica de validación de negocio para categorías.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductCategoryValidationServiceImpl implements ProductCategoryValidationService {

    private final ProductCategoryTransactionalService transactionalService;

    @Override
    public Category findCategoryByIdOrThrow(Integer categoryId) {
        return transactionalService.findCategoryById(categoryId)
                .orElseThrow(() -> {
                    log.warn("Categoría no encontrada: {}", categoryId);
                    return new ProductException(Constants.ERROR_CATEGORY_NOT_FOUND, Constants.CODE_CATEGORY_NOT_FOUND);
                });
    }

    @Override
    public Category findCategoryByNameOrThrow(String categoryName) {
        return transactionalService.findCategoryByName(categoryName)
                .orElseThrow(() -> {
                    log.warn("Categoría no encontrada con nombre: {}", categoryName);
                    return new ProductException(Constants.ERROR_CATEGORY_NOT_FOUND, Constants.CODE_CATEGORY_NOT_FOUND);
                });
    }

    @Override
    public void validateCategoryActive(Category category) {
        if (!category.isActive()) {
            log.warn("Categoría inactiva: {}", category.getCategoriaId());
            throw new ProductException(Constants.ERROR_CATEGORY_INACTIVE, Constants.CODE_CATEGORY_INACTIVE);
        }
    }
}

