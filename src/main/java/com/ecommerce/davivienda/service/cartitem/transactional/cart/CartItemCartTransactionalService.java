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
     * Busca un carrito existente del usuario o crea uno nuevo.
     *
     * @param userRoleId ID del rol de usuario
     * @return Cart existente o nuevo carrito creado
     */
    Cart findOrCreateCart(Integer userRoleId);

    /**
     * Busca un carrito por ID validando que pertenece al usuario.
     *
     * @param cartId ID del carrito
     * @param userRoleId ID del rol de usuario propietario
     * @return Optional con el carrito si existe y pertenece al usuario
     */
    Optional<Cart> findCartByIdAndUser(Integer cartId, Integer userRoleId);

    /**
     * Busca un CartItem por carrito y producto.
     *
     * @param cartId ID del carrito
     * @param productId ID del producto
     * @return Optional con el CartItem si existe
     */
    Optional<CartItem> findCartItemByCartAndProduct(Integer cartId, Integer productId);

    /**
     * Busca un CartItem por ID o lanza excepción.
     *
     * @param itemId ID del item
     * @return CartItem encontrado
     * @throws com.ecommerce.davivienda.exception.CartException si el item no existe
     */
    CartItem findCartItemById(Integer itemId);

    /**
     * Verifica si existe un CartItem para el carrito y producto.
     *
     * @param cartId ID del carrito
     * @param productId ID del producto
     * @return true si existe, false en caso contrario
     */
    boolean existsCartItemByCartAndProduct(Integer cartId, Integer productId);

    /**
     * Obtiene todos los items de un carrito.
     *
     * @param cartId ID del carrito
     * @return Lista de CartItem del carrito
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
     * Elimina todos los items de un carrito.
     *
     * @param cartId ID del carrito
     */
    void deleteCartItemsByCartId(Integer cartId);

    /**
     * Busca un CartItem por producto validando ownership del usuario.
     * Valida que el producto pertenece al carrito del usuario autenticado.
     *
     * @param productId ID del producto
     * @param userRoleId ID del usuario_rol propietario
     * @return Optional con el CartItem si existe y pertenece al usuario
     */
    Optional<CartItem> findCartItemByProductAndUser(Integer productId, Integer userRoleId);

    /**
     * Busca un CartItem por ID validando ownership del usuario.
     * Valida en una sola query que el item existe y pertenece al carrito del usuario.
     *
     * @param itemId ID del CartItem
     * @param userRoleId ID del usuario_rol propietario
     * @return Optional con el CartItem si existe y pertenece al usuario
     */
    Optional<CartItem> findCartItemByIdAndUser(Integer itemId, Integer userRoleId);
}

