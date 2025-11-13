package com.ecommerce.davivienda.service.product.validation.product;

import com.ecommerce.davivienda.constants.Constants;
import com.ecommerce.davivienda.entity.product.Product;
import com.ecommerce.davivienda.exception.product.ProductException;
import com.ecommerce.davivienda.service.product.transactional.product.ProductProductTransactionalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Implementación del servicio de validación de productos.
 * Contiene toda la lógica de validación de negocio para productos.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductProductValidationServiceImpl implements ProductProductValidationService {

    private final ProductProductTransactionalService transactionalService;

    @Override
    public void validateProductNameNotExists(String name) {
        if (transactionalService.existsByName(name)) {
            log.warn("Intento de crear producto con nombre duplicado: {}", name);
            throw new ProductException(Constants.ERROR_PRODUCT_NAME_EXISTS, Constants.CODE_PRODUCT_NAME_EXISTS);
        }
    }

    @Override
    public void validateProductNameNotExistsOnUpdate(String name, Integer productId) {
        if (transactionalService.existsByNameAndNotId(name, productId)) {
            log.warn("Intento de actualizar producto {} con nombre duplicado: {}", productId, name);
            throw new ProductException(Constants.ERROR_PRODUCT_NAME_EXISTS, Constants.CODE_PRODUCT_NAME_EXISTS);
        }
    }

    @Override
    public Product findProductByIdOrThrow(Integer productId) {
        return transactionalService.findProductById(productId)
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
}

