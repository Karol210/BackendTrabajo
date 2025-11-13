package com.ecommerce.davivienda.service.cart;

/**
 * Servicio para gestión de carritos de compras.
 * Proporciona operaciones CRUD para carritos de usuarios autenticados.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface CartService {

    /**
     * Crea un carrito para el usuario autenticado actualmente.
     * Extrae el email del usuario desde el token JWT en el SecurityContext,
     * busca el usuario, obtiene su UserRole principal y crea el carrito asociado.
     */
    void createCart();
}

