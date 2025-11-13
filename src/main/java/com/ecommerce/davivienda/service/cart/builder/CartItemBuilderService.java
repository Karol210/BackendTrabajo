package com.ecommerce.davivienda.service.cart.builder;

import com.ecommerce.davivienda.dto.cart.CartItemRequestDto;
import com.ecommerce.davivienda.entity.cart.Cart;
import com.ecommerce.davivienda.entity.cart.CartItem;
import com.ecommerce.davivienda.entity.product.Product;

/**
 * Servicio de construcción para items del carrito.
 * Contiene lógica para construir y actualizar entidades CartItem.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface CartItemBuilderService {

    /**
     * Construye un CartItem desde un DTO de request.
     *
     * @param request DTO con datos del item
     * @param cart Carrito asociado
     * @param product Producto asociado
     * @return CartItem construido
     */
    CartItem buildCartItem(CartItemRequestDto request, Cart cart, Product product);

    /**
     * Actualiza la cantidad de un CartItem existente.
     *
     * @param cartItem Item a actualizar
     * @param newQuantity Nueva cantidad
     */
    void updateQuantity(CartItem cartItem, Integer newQuantity);
}

