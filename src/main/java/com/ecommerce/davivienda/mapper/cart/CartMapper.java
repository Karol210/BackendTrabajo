package com.ecommerce.davivienda.mapper.cart;

import com.ecommerce.davivienda.entity.cart.Cart;
import com.ecommerce.davivienda.entity.user.UserRole;
import com.ecommerce.davivienda.models.cart.CartCreateResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static com.ecommerce.davivienda.constants.Constants.SUCCESS_CART_CREATED;

/**
 * Mapper para transformaciones entre entidades Cart y DTOs.
 * Utiliza MapStruct para mapeo compile-time.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Mapper(componentModel = "spring")
public interface CartMapper {

    /**
     * Construye una entidad Cart a partir del UserRole.
     *
     * @param userRole UserRole del usuario
     * @return Cart construido
     */
    @Mapping(target = "carritoId", ignore = true)
    @Mapping(target = "usuarioRolId", source = "usuarioRolId")
    Cart toEntity(UserRole userRole);

    /**
     * Construye una entidad Cart con ID específico y userRoleId.
     *
     * @param cartId ID del carrito
     * @param userRoleId ID del usuario_rol
     * @return Cart construido
     */
    default Cart toEntity(Integer cartId, Integer userRoleId) {
        return Cart.builder()
                .carritoId(cartId)
                .usuarioRolId(userRoleId)
                .build();
    }

    /**
     * Construye el DTO de respuesta con la información del carrito creado.
     *
     * @param cart Carrito creado
     * @param userEmail Email del usuario
     * @return DTO de respuesta
     */
    default CartCreateResponse toResponseDto(Cart cart, String userEmail) {
        return CartCreateResponse.builder()
                .cartId(cart.getCarritoId())
                .userEmail(userEmail)
                .message(SUCCESS_CART_CREATED)
                .build();
    }
}

