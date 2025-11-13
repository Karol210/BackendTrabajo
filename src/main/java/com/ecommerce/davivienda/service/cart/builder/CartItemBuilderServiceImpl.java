package com.ecommerce.davivienda.service.cart.builder;

import com.ecommerce.davivienda.dto.cart.CartItemRequestDto;
import com.ecommerce.davivienda.entity.cart.Cart;
import com.ecommerce.davivienda.entity.cart.CartItem;
import com.ecommerce.davivienda.entity.product.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Implementación del servicio de construcción para items del carrito.
 * Maneja la creación y actualización de entidades CartItem.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
public class CartItemBuilderServiceImpl implements CartItemBuilderService {

    @Override
    public CartItem buildCartItem(CartItemRequestDto request, Cart cart, Product product) {
        log.debug("Construyendo CartItem para producto {} en carrito {}", 
                product.getProductoId(), cart.getCarritoId());
        
        return CartItem.builder()
                .cart(cart)
                .product(product)
                .cantidad(request.getQuantity())
                .build();
    }

    @Override
    public void updateQuantity(CartItem cartItem, Integer newQuantity) {
        log.debug("Actualizando cantidad del item {} de {} a {}", 
                cartItem.getProductosCarritoId(), cartItem.getCantidad(), newQuantity);
        
        cartItem.setCantidad(newQuantity);
    }
}

