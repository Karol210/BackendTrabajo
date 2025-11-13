package com.ecommerce.davivienda.mapper.cart;

import com.ecommerce.davivienda.dto.cart.item.CartItemCalculationDto;
import com.ecommerce.davivienda.dto.cart.summary.CartSummaryDto;
import com.ecommerce.davivienda.entity.cart.Cart;
import com.ecommerce.davivienda.models.cart.CartItemRequest;
import com.ecommerce.davivienda.models.cart.CartItemResponse;
import com.ecommerce.davivienda.entity.cart.CartItem;
import com.ecommerce.davivienda.entity.product.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.math.BigDecimal;
import java.util.List;

/**
 * Mapper MapStruct para conversiones entre CartItem y DTOs.
 * Incluye lógica de construcción y transformación de entidades.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Mapper(componentModel = "spring")
public interface CartItemMapper {

    /**
     * Construye un CartItem desde un DTO de request con entidades asociadas.
     * Este método reemplaza el BuilderService eliminado.
     *
     * @param request DTO con datos del item
     * @param cart Carrito asociado
     * @param product Producto asociado
     * @return CartItem construido
     */
    @Mapping(target = "productosCarritoId", ignore = true)
    @Mapping(target = "cart", source = "cart")
    @Mapping(target = "product", source = "product")
    @Mapping(target = "cantidad", source = "request.quantity")
    CartItem toEntity(CartItemRequest request, Cart cart, Product product);

    /**
     * Actualiza la cantidad de un CartItem existente.
     * Este método reemplaza el BuilderService eliminado.
     *
     * @param cartItem Item a actualizar (modificado in-place)
     * @param newQuantity Nueva cantidad
     */
    @Mapping(target = "productosCarritoId", ignore = true)
    @Mapping(target = "cart", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "cantidad", source = "newQuantity")
    void updateQuantity(@MappingTarget CartItem cartItem, Integer newQuantity);

    /**
     * Convierte CartItem a CartItemResponse con cálculos.
     *
     * @param cartItem Entidad CartItem
     * @return DTO de respuesta con cálculos
     */
    @Mapping(target = "id", source = "productosCarritoId")
    @Mapping(target = "cartId", source = "cart.carritoId")
    @Mapping(target = "productId", source = "product.productoId")
    @Mapping(target = "productName", source = "product.nombre")
    @Mapping(target = "productDescription", source = "product.descripcion")
    @Mapping(target = "imageUrl", source = "product.imagen")
    @Mapping(target = "calculation", expression = "java(buildCalculationDto(cartItem))")
    CartItemResponse toResponseDto(CartItem cartItem);

    /**
     * Convierte una lista de CartItem a lista de CartItemResponse.
     *
     * @param cartItems Lista de entidades CartItem
     * @return Lista de DTOs de respuesta
     */
    List<CartItemResponse> toResponseDtoList(List<CartItem> cartItems);

    /**
     * Construye un resumen completo del carrito con totales agregados.
     *
     * @param cartItems Lista de items del carrito
     * @return DTO con resumen del carrito
     */
    default CartSummaryDto toCartSummaryDto(List<CartItem> cartItems) {
        List<CartItemResponse> itemDtos = toResponseDtoList(cartItems);
        
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
                .items(itemDtos)
                .totalItems(totalItems)
                .totalSubtotal(totalSubtotal)
                .totalIva(totalIva)
                .totalPrice(totalPrice)
                .build();
    }

    /**
     * Construye el DTO de cálculos detallados.
     * Método auxiliar para mapeo de calculation.
     *
     * @param cartItem Entidad CartItem
     * @return DTO con cálculos de precio e IVA
     */
    default CartItemCalculationDto buildCalculationDto(CartItem cartItem) {
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
}

