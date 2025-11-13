package com.ecommerce.davivienda.service.cart;

import com.ecommerce.davivienda.dto.cart.CartItemBatchRequestDto;
import com.ecommerce.davivienda.dto.cart.CartItemRequestDto;
import com.ecommerce.davivienda.dto.cart.CartItemResponseDto;
import com.ecommerce.davivienda.dto.cart.CartSummaryDto;

import java.util.List;

/**
 * Servicio para gestión de items del carrito de compras.
 * Proporciona operaciones CRUD con cálculos de precios e IVA.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface CartItemService {

    /**
     * Agrega un producto al carrito.
     * Si el producto ya existe, reemplaza la cantidad con el nuevo valor.
     *
     * @param request DTO con datos del item a agregar
     * @return DTO de respuesta con cálculos
     */
    CartItemResponseDto addItemToCart(CartItemRequestDto request);

    /**
     * Agrega múltiples productos al carrito de una sola vez.
     * Si algún producto ya existe, actualiza su cantidad.
     *
     * @param request DTO con lista de items a agregar
     * @return Lista de DTOs de respuesta con cálculos
     */
    List<CartItemResponseDto> addItemsToCartBatch(CartItemBatchRequestDto request);

    /**
     * Actualiza la cantidad de un producto en el carrito.
     *
     * @param itemId ID del item a actualizar
     * @param quantity Nueva cantidad
     * @return DTO de respuesta actualizado
     */
    CartItemResponseDto updateItemQuantity(Integer itemId, Integer quantity);

    /**
     * Elimina un item del carrito.
     *
     * @param itemId ID del item a eliminar
     */
    void removeItemFromCart(Integer itemId);

    /**
     * Obtiene todos los items de un carrito con cálculos.
     *
     * @param cartId ID del carrito
     * @return Lista de items con cálculos
     */
    List<CartItemResponseDto> getCartItems(Integer cartId);

    /**
     * Obtiene un resumen completo del carrito con totales agregados.
     *
     * @param cartId ID del carrito
     * @return DTO con resumen del carrito
     */
    CartSummaryDto getCartSummary(Integer cartId);

    /**
     * Limpia todos los items de un carrito.
     *
     * @param cartId ID del carrito
     */
    void clearCart(Integer cartId);

    /**
     * Obtiene un item específico del carrito por su ID.
     *
     * @param itemId ID del item
     * @return DTO de respuesta con cálculos
     */
    CartItemResponseDto getCartItemById(Integer itemId);
}

