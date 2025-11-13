package com.ecommerce.davivienda.service.cartitem.transactional.cart;

import com.ecommerce.davivienda.entity.cart.Cart;
import com.ecommerce.davivienda.entity.cart.CartItem;

import java.util.List;
import java.util.Optional;

/**
 * Servicio transaccional para operaciones de consulta y persistencia de Cart y CartItem.
 * Responsabilidad: Acceso a datos de carritos y sus items.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface CartItemCartTransactionalService {

    /**
     * Busca un carrito ACTIVO existente del usuario o crea uno nuevo.
     * Solo busca carritos con estado activo (id_estado_carrito = 1).
     * Si no existe carrito activo, crea uno nuevo con estado activo.
     *
     * @param userRoleId ID del rol de usuario
     * @return Cart activo existente o nuevo carrito creado con estado activo
     */
    Cart findOrCreateCart(Integer userRoleId);

    /**
     * Busca un CartItem por carrito y producto en carrito ACTIVO.
     * Solo retorna items de carritos con estado activo (id_estado_carrito = 1).
     *
     * @param cartId ID del carrito
     * @param productId ID del producto
     * @return Optional con el CartItem si existe en carrito activo
     */
    Optional<CartItem> findCartItemByCartAndProduct(Integer cartId, Integer productId);

    /**
     * Obtiene todos los items de un carrito ACTIVO.
     * Solo retorna items de carritos con estado activo (id_estado_carrito = 1).
     *
     * @param cartId ID del carrito
     * @return Lista de CartItem del carrito activo
     */
    List<CartItem> findCartItemsByCartId(Integer cartId);

    /**
     * Guarda un CartItem (crear o actualizar).
     *
     * @param cartItem Item a guardar
     * @return CartItem guardado
     */
    CartItem saveCartItem(CartItem cartItem);

    /**
     * Elimina un CartItem específico.
     *
     * @param cartItem Item a eliminar
     */
    void deleteCartItem(CartItem cartItem);

    /**
     * Busca un CartItem por ID validando ownership del usuario y carrito ACTIVO.
     * Valida en una sola query que el item existe, pertenece al carrito del usuario
     * y que el carrito está activo (id_estado_carrito = 1).
     *
     * @param itemId ID del CartItem
     * @param userRoleId ID del usuario_rol propietario
     * @return Optional con el CartItem si existe, pertenece al usuario y carrito está activo
     */
    Optional<CartItem> findCartItemByIdAndUser(Integer itemId, Integer userRoleId);
}

