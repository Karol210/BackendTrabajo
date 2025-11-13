package com.ecommerce.davivienda.mapper.cart;

import com.ecommerce.davivienda.dto.cart.CartItemCalculationDto;
import com.ecommerce.davivienda.dto.cart.CartItemResponseDto;
import com.ecommerce.davivienda.dto.cart.CartSummaryDto;
import com.ecommerce.davivienda.entity.cart.CartItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para conversiones entre CartItem y DTOs.
 * Incluye lógica de cálculo de precios e IVA.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Component
public class CartItemMapper {

    /**
     * Convierte CartItem a CartItemResponseDto con cálculos.
     *
     * @param cartItem Entidad CartItem
     * @return DTO de respuesta con cálculos
     */
    public CartItemResponseDto toResponseDto(CartItem cartItem) {
        if (cartItem == null) {
            return null;
        }

        return CartItemResponseDto.builder()
                .id(cartItem.getProductosCarritoId())
                .cartId(cartItem.getCart() != null ? cartItem.getCart().getCarritoId() : null)
                .productId(cartItem.getProduct() != null ? cartItem.getProduct().getProductoId() : null)
                .productName(cartItem.getProduct() != null ? cartItem.getProduct().getNombre() : null)
                .productDescription(cartItem.getProduct() != null ? cartItem.getProduct().getDescripcion() : null)
                .imageUrl(cartItem.getProduct() != null ? cartItem.getProduct().getImagen() : null)
                .calculation(buildCalculationDto(cartItem))
                .build();
    }

    /**
     * Construye el DTO de cálculos detallados.
     *
     * @param cartItem Entidad CartItem
     * @return DTO con cálculos de precio e IVA
     */
    private CartItemCalculationDto buildCalculationDto(CartItem cartItem) {
        if (cartItem == null || cartItem.getProduct() == null) {
            return null;
        }

        return CartItemCalculationDto.builder()
                .unitValue(cartItem.getProduct().getValorUnitario())
                .ivaPercentage(cartItem.getProduct().getIva())
                .quantity(cartItem.getCantidad())
                .subtotal(cartItem.calculateSubtotal())
                .ivaAmount(cartItem.calculateIvaAmount())
                .totalPrice(cartItem.calculateTotal())
                .build();
    }

    /**
     * Convierte una lista de CartItem a lista de CartItemResponseDto.
     *
     * @param cartItems Lista de entidades CartItem
     * @return Lista de DTOs de respuesta
     */
    public List<CartItemResponseDto> toResponseDtoList(List<CartItem> cartItems) {
        if (cartItems == null) {
            return null;
        }
        return cartItems.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Construye un resumen completo del carrito con totales agregados.
     *
     * @param cartId ID del carrito
     * @param cartItems Lista de items del carrito
     * @return DTO con resumen del carrito
     */
    public CartSummaryDto toCartSummaryDto(Integer cartId, List<CartItem> cartItems) {
        List<CartItemResponseDto> itemDtos = toResponseDtoList(cartItems);
        
        BigDecimal totalSubtotal = BigDecimal.ZERO;
        BigDecimal totalIva = BigDecimal.ZERO;
        BigDecimal totalPrice = BigDecimal.ZERO;
        int totalItems = 0;

        if (cartItems != null) {
            for (CartItem item : cartItems) {
                totalSubtotal = totalSubtotal.add(item.calculateSubtotal());
                totalIva = totalIva.add(item.calculateIvaAmount());
                totalPrice = totalPrice.add(item.calculateTotal());
                totalItems += item.getCantidad();
            }
        }

        return CartSummaryDto.builder()
                .cartId(cartId)
                .items(itemDtos)
                .totalItems(totalItems)
                .totalSubtotal(totalSubtotal)
                .totalIva(totalIva)
                .totalPrice(totalPrice)
                .build();
    }
}

