package com.ecommerce.davivienda.service.cart.transactional.cart;

import com.ecommerce.davivienda.entity.cart.Cart;

import java.util.Optional;

/**
 * Servicio transaccional para operaciones de consulta y persistencia de Cart.
 * Maneja todas las operaciones de acceso a datos de carritos.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface CartCartTransactionalService {

    /**
     * Busca un carrito por ID.
     *
     * @param cartId ID del carrito
     * @return Optional con el carrito si existe
     */
    Optional<Cart> findCartById(Integer cartId);

    /**
     * Verifica si existe un carrito para el usuario_rol.
     *
     * @param usuarioRolId ID del usuario_rol
     * @return true si existe, false en caso contrario
     */
    boolean existsByUsuarioRolId(Integer usuarioRolId);

    /**
     * Busca un carrito por usuario_rol.
     *
     * @param usuarioRolId ID del usuario_rol
     * @return Optional con el carrito si existe
     */
    Optional<Cart> findCartByUsuarioRolId(Integer usuarioRolId);

    /**
     * Guarda un carrito.
     *
     * @param cart Carrito a guardar
     * @return Carrito guardado
     */
    Cart saveCart(Cart cart);
}

