package com.ecommerce.davivienda.service.product.validation;

import com.ecommerce.davivienda.constants.Constants;
import com.ecommerce.davivienda.dto.product.ProductRequestDto;
import com.ecommerce.davivienda.entity.product.Category;
import com.ecommerce.davivienda.entity.product.Product;
import com.ecommerce.davivienda.exception.product.ProductException;
import com.ecommerce.davivienda.repository.product.CategoryRepository;
import com.ecommerce.davivienda.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Implementación del servicio de validaciones de productos.
 * Contiene toda la lógica de validación de negocio para operaciones CRUD.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductValidationServiceImpl implements ProductValidationService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public void validateProductNameNotExists(String name) {
        if (productRepository.existsByNombre(name)) {
            log.warn("Intento de crear producto con nombre duplicado: {}", name);
            throw new ProductException(Constants.ERROR_PRODUCT_NAME_EXISTS, Constants.CODE_PRODUCT_NAME_EXISTS);
        }
    }

    @Override
    public void validateProductNameNotExistsOnUpdate(String name, Integer productId) {
        if (productRepository.existsByNombreAndProductoIdNot(name, productId)) {
            log.warn("Intento de actualizar producto {} con nombre duplicado: {}", productId, name);
            throw new ProductException(Constants.ERROR_PRODUCT_NAME_EXISTS, Constants.CODE_PRODUCT_NAME_EXISTS);
        }
    }

    @Override
    public void validatePrices(ProductRequestDto request) {
        // Validación de IVA (opcional, ya está en Bean Validation)
        if (request.getIva() != null) {
            if (request.getIva().compareTo(BigDecimal.ZERO) < 0 || 
                request.getIva().compareTo(BigDecimal.valueOf(100)) > 0) {
                log.warn("IVA inválido: {}", request.getIva());
                throw new ProductException(Constants.ERROR_PRICE_INVALID, Constants.CODE_PRICE_INVALID);
            }
        }
    }

    @Override
    public Category findCategoryByIdOrThrow(Integer categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    log.warn("Categoría no encontrada: {}", categoryId);
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

    @Override
    public Product findProductByIdOrThrow(Integer productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> {
                    log.warn("Producto no encontrado: {}", productId);
                    String message = String.format("[%s] %s con ID: %s", 
                            Constants.CODE_PRODUCT_NOT_FOUND,
                            Constants.ERROR_PRODUCT_NOT_FOUND,
                            productId);
                    return new ProductException(message, Constants.CODE_PRODUCT_NOT_FOUND);
                });
    }

    @Override
    public void validateInventoryQuantity(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            log.warn("Cantidad de inventario inválida: {}", quantity);
            throw new ProductException(
                    Constants.ERROR_INVALID_INVENTORY_QUANTITY, 
                    Constants.CODE_INVALID_INVENTORY_QUANTITY);
        }
    }

    @Override
    public void validateCreateRequest(ProductRequestDto request) {
        validateProductNameNotExists(request.getName());
        validatePrices(request);
        Category category = findCategoryByIdOrThrow(request.getCategoryId());
        validateCategoryActive(category);
    }

    @Override
    public void validateUpdateRequest(Integer productId, ProductRequestDto request) {
        validateProductNameNotExistsOnUpdate(request.getName(), productId);
        validatePrices(request);
        Category category = findCategoryByIdOrThrow(request.getCategoryId());
        validateCategoryActive(category);
    }
}

