package com.ecommerce.davivienda.service.product.validation.common;

import com.ecommerce.davivienda.models.product.ProductRequest;
import com.ecommerce.davivienda.models.product.ProductUpdateRequest;

/**
 * Servicio de validaciones comunes de productos.
 * Contiene validaciones genéricas aplicables a múltiples operaciones.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface ProductCommonValidationService {

    /**
     * Valida los precios del producto (IVA en rango válido).
     *
     * @param request Request con los datos del producto
     * @throws com.ecommerce.davivienda.exception.product.ProductException si los precios son inválidos
     */
    void validatePrices(ProductRequest request);

    /**
     * Valida los precios del producto para actualización (IVA en rango válido).
     * Solo valida los campos proporcionados (no nulos).
     *
     * @param request Request con los datos del producto a actualizar
     * @throws com.ecommerce.davivienda.exception.product.ProductException si los precios son inválidos
     */
    void validatePrices(ProductUpdateRequest request);
}

