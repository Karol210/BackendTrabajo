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
     * Busca todos los items de un carrito específico.
     *
     * @param carritoId ID del carrito
     * @return Lista de items del carrito
     */
    @Query("SELECT ci FROM CartItem ci JOIN FETCH ci.product WHERE ci.cart.carritoId = :carritoId")
    List<CartItem> findByCartCarritoId(@Param("carritoId") Integer carritoId);

    /**
     * Busca un item específico de un carrito por producto.
     *
     * @param carritoId ID del carrito
     * @param productoId ID del producto
     * @return Optional con el item si existe
     */
    @Query("SELECT ci FROM CartItem ci JOIN FETCH ci.product WHERE ci.cart.carritoId = :carritoId AND ci.product.productoId = :productoId")
    Optional<CartItem> findByCartAndProduct(@Param("carritoId") Integer carritoId, 
                                            @Param("productoId") Integer productoId);

    /**
     * Verifica si existe un item en el carrito para un producto específico.
     *
     * @param carritoId ID del carrito
     * @param productoId ID del producto
     * @return true si el producto ya está en el carrito
     */
    @Query("SELECT CASE WHEN COUNT(ci) > 0 THEN true ELSE false END FROM CartItem ci WHERE ci.cart.carritoId = :carritoId AND ci.product.productoId = :productoId")
    boolean existsByCartAndProduct(@Param("carritoId") Integer carritoId, 
                                   @Param("productoId") Integer productoId);

    /**
     * Elimina todos los items de un carrito específico.
     *
     * @param carritoId ID del carrito
     */
    void deleteByCartCarritoId(Integer carritoId);

    /**
     * Cuenta la cantidad de items en un carrito.
     *
     * @param carritoId ID del carrito
     * @return Número de items
     */
    long countByCartCarritoId(Integer carritoId);

    /**
     * Busca un item por ID del producto validando que pertenece al usuario.
     * Realiza JOIN con carrito para validar ownership en una sola query.
     *
     * @param productId ID del producto
     * @param userRoleId ID del usuario_rol propietario del carrito
     * @return Optional con el CartItem si existe y pertenece al usuario
     */
    @Query("SELECT ci FROM CartItem ci JOIN FETCH ci.product WHERE ci.product.productoId = :productId AND ci.cart.usuarioRolId = :userRoleId")
    Optional<CartItem> findByProductIdAndUserRole(@Param("productId") Integer productId, 
                                                  @Param("userRoleId") Integer userRoleId);
}

