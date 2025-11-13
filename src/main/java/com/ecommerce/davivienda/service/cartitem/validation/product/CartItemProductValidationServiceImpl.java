package com.ecommerce.davivienda.service.cartitem.validation.product;

import com.ecommerce.davivienda.entity.product.Product;
import com.ecommerce.davivienda.exception.product.ProductException;
import com.ecommerce.davivienda.service.cartitem.transactional.product.CartItemProductTransactionalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.ecommerce.davivienda.constants.Constants.*;

/**
 * Implementación del servicio de validación de productos para items del carrito.
 * Aplica reglas de negocio relacionadas con la validez de productos.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CartItemProductValidationServiceImpl implements CartItemProductValidationService {

    private final CartItemProductTransactionalService transactionalService;

    @Override
    public Product validateProductExists(Integer productId) {
        log.debug("Validando existencia del producto con ID: {}", productId);
        
        if (productId == null) {
            throw new ProductException(ERROR_PRODUCT_NOT_FOUND, CODE_PRODUCT_NOT_FOUND);
        }
        
        return transactionalService.findProductById(productId)
                .orElseThrow(() -> {
                    log.warn("Producto no encontrado con ID: {}", productId);
                    String message = String.format("[%s] %s con ID: %s", 
                            CODE_PRODUCT_NOT_FOUND,
                            ERROR_PRODUCT_NOT_FOUND,
                            productId);
                    return new ProductException(message, CODE_PRODUCT_NOT_FOUND);
                });
    }

    @Override
    public void validateProductActive(Product product) {
        if (product == null || !product.isActive()) {
            log.warn("Producto inactivo: {}", product != null ? product.getProductoId() : "null");
            throw new ProductException(ERROR_PRODUCT_INACTIVE, CODE_PRODUCT_INACTIVE);
        }
    }
}

