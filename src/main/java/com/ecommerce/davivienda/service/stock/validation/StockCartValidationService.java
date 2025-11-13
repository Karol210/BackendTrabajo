package com.ecommerce.davivienda.service.stock.validation;

import com.ecommerce.davivienda.entity.cart.CartItem;

import java.util.List;

/**
 * Servicio de validación para operaciones de carrito en contexto de stock.
 * Valida reglas de negocio relacionadas con carritos e inventario.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface StockCartValidationService {

    /**
     * Valida que el carrito tenga al menos un producto.
     *
     * @param cartItems Lista de items del carrito
     * @throws com.ecommerce.davivienda.exception.stock.StockException si el carrito está vacío
     */
    void validateCartHasItems(List<CartItem> cartItems);
}

