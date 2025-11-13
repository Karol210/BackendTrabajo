package com.ecommerce.davivienda.repository.cart;

import com.ecommerce.davivienda.entity.cart.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para operaciones de persistencia de items del carrito.
 * Proporciona métodos para gestionar productos dentro de un carrito.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

    /**
     * Busca todos los items de un carrito específico ACTIVO.
     * Solo retorna items de carritos con estado activo (id_estado_carrito = 1).
     *
     * @param carritoId ID del carrito
     * @return Lista de items del carrito activo
     */
    @Query("SELECT ci FROM CartItem ci JOIN FETCH ci.product WHERE ci.cart.carritoId = :carritoId AND ci.cart.estadoCarritoId = 1")
    List<CartItem> findByCartCarritoId(@Param("carritoId") Integer carritoId);

    /**
     * Busca un item específico de un carrito ACTIVO por producto.
     * Solo retorna items de carritos con estado activo (id_estado_carrito = 1).
     *
     * @param carritoId ID del carrito
     * @param productoId ID del producto
     * @return Optional con el item si existe en carrito activo
     */
    @Query("SELECT ci FROM CartItem ci JOIN FETCH ci.product WHERE ci.cart.carritoId = :carritoId AND ci.product.productoId = :productoId AND ci.cart.estadoCarritoId = 1")
    Optional<CartItem> findByCartAndProduct(@Param("carritoId") Integer carritoId, 
                                            @Param("productoId") Integer productoId);

    /**
     * Busca un item por ID del producto validando que pertenece al usuario y carrito ACTIVO.
     * Realiza JOIN con carrito para validar ownership y estado activo en una sola query.
     * Solo retorna items de carritos con estado activo (id_estado_carrito = 1).
     *
     * @param productId ID del producto
     * @param userRoleId ID del usuario_rol propietario del carrito
     * @return Optional con el CartItem si existe, pertenece al usuario y carrito está activo
     */
    @Query("SELECT ci FROM CartItem ci JOIN FETCH ci.product WHERE ci.product.productoId = :productId AND ci.cart.usuarioRolId = :userRoleId AND ci.cart.estadoCarritoId = 1")
    Optional<CartItem> findByProductIdAndUserRole(@Param("productId") Integer productId, 
                                                  @Param("userRoleId") Integer userRoleId);
}

