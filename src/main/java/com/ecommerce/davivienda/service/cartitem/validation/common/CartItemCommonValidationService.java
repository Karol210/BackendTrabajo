package com.ecommerce.davivienda.service.cartitem.validation.common;

/**
 * Servicio de validaciones genéricas para items del carrito.
 * Responsabilidad: Validaciones comunes que no pertenecen a un dominio específico.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface CartItemCommonValidationService {

    /**
     * Valida que la cantidad sea mayor a 0.
     * Regla de negocio: No se permiten cantidades nulas, negativas o cero.
     *
     * @param quantity Cantidad a validar
     * @throws com.ecommerce.davivienda.exception.CartException si la cantidad es inválida
     */
    void validateQuantity(Integer quantity);
}

