package com.ecommerce.davivienda.service.cartitem.validation.cart;

import com.ecommerce.davivienda.entity.cart.Cart;
import com.ecommerce.davivienda.entity.cart.CartItem;

/**
 * Servicio de validación de carritos para items del carrito.
 * Responsabilidad: Validar existencia, ownership y duplicados en carritos.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface CartItemCartValidationService {

    /**
     * Valida que el carrito exista y pertenezca al usuario.
     * Verifica ownership del carrito para garantizar que el usuario solo acceda a su propio carrito.
     *
     * @param cartId ID del carrito a validar
     * @param userRoleId ID del usuario_rol propietario
     * @return Carrito validado
     * @throws com.ecommerce.davivienda.exception.CartException si el carrito no existe o no pertenece al usuario
     */
    Cart validateCartExistsAndBelongsToUser(Integer cartId, Integer userRoleId);
    
    /**
     * Valida que el carrito exista o lo crea automáticamente si se proporciona userRoleId.
     *
     * @param cartId ID del carrito (puede ser null)
     * @param userRoleId ID del usuario_rol para crear carrito si no existe
     * @return Carrito validado o creado
     * @throws com.ecommerce.davivienda.exception.CartException si hay error en validación o creación
     */
    Cart validateOrCreateCart(Integer cartId, Integer userRoleId);

    /**
     * Valida que el producto no exista ya en el carrito.
     * Evita duplicados del mismo producto en el mismo carrito.
     *
     * @param cartId ID del carrito
     * @param productId ID del producto
     * @throws com.ecommerce.davivienda.exception.CartException si el producto ya existe en el carrito
     */
    void validateProductNotInCart(Integer cartId, Integer productId);
    
    /**
     * Valida que el item pertenece al carrito del usuario autenticado.
     * Verifica ownership para operaciones de actualización/eliminación.
     *
     * @param itemId ID del item a validar
     * @param userRoleId ID del UserRole del usuario autenticado
     * @throws com.ecommerce.davivienda.exception.CartException si el item no pertenece al usuario
     */
    CartItem validateItemBelongsToUser(Integer itemId, Integer userRoleId);

    /**
     * Valida que el producto existe en el carrito del usuario autenticado.
     * Útil cuando se necesita validar por producto en lugar de por itemId.
     *
     * @param productId ID del producto
     * @param userRoleId ID del usuario_rol propietario
     * @throws com.ecommerce.davivienda.exception.CartException si el producto no está en el carrito del usuario
     */
    void validateProductBelongsToUser(Integer productId, Integer userRoleId);
}

