package com.ecommerce.davivienda.service.product.validation.common;

import com.ecommerce.davivienda.constants.Constants;
import com.ecommerce.davivienda.exception.product.ProductException;
import com.ecommerce.davivienda.models.product.ProductRequest;
import com.ecommerce.davivienda.models.product.ProductUpdateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Implementación del servicio de validaciones comunes de productos.
 * Contiene validaciones genéricas aplicables a múltiples operaciones.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
public class ProductCommonValidationServiceImpl implements ProductCommonValidationService {

    @Override
    public void validatePrices(ProductRequest request) {
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
    public void validatePrices(ProductUpdateRequest request) {
        // Validación de IVA (solo si se proporciona)
        if (request.getIva() != null) {
            if (request.getIva().compareTo(BigDecimal.ZERO) < 0 || 
                request.getIva().compareTo(BigDecimal.valueOf(100)) > 0) {
                log.warn("IVA inválido: {}", request.getIva());
                throw new ProductException(Constants.ERROR_PRICE_INVALID, Constants.CODE_PRICE_INVALID);
            }
        }
    }
}

