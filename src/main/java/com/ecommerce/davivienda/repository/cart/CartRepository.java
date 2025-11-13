package com.ecommerce.davivienda.repository.cart;

import com.ecommerce.davivienda.entity.cart.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para operaciones de persistencia de carritos de compras.
 * Proporciona métodos para gestionar carritos asociados a usuarios.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

    /**
     * Busca un carrito por ID de usuario_rol.
     *
     * @param usuarioRolId ID del usuario_rol
     * @return Optional con el carrito si existe
     */
    Optional<Cart> findByUsuarioRolId(Integer usuarioRolId);

    /**
     * Busca un carrito activo por ID de usuario_rol.
     * Solo retorna carritos con estado activo (id_estado_carrito = 1).
     *
     * @param usuarioRolId ID del usuario_rol
     * @param estadoCarritoId ID del estado del carrito (1 = Activo)
     * @return Optional con el carrito activo si existe
     */
    Optional<Cart> findByUsuarioRolIdAndEstadoCarritoId(Integer usuarioRolId, Integer estadoCarritoId);

    /**
     * Verifica si existe un carrito para un usuario_rol específico.
     *
     * @param usuarioRolId ID del usuario_rol
     * @return true si existe un carrito
     */
    boolean existsByUsuarioRolId(Integer usuarioRolId);

    /**
     * Busca un carrito activo por el correo del usuario.
     * Navega desde credenciales → usuarios → usuario_rol → carrito.
     *
     * @param correo Correo electrónico del usuario
     * @return Optional con el carrito del usuario si existe
     */
    @Query("""
            SELECT c FROM Cart c
            WHERE c.usuarioRolId IN (
                SELECT ur.usuarioRolId FROM UserRole ur
                WHERE ur.usuarioId IN (
                    SELECT u.usuarioId FROM User u
                    WHERE u.credenciales.correo = :correo
                )
            )
            """)
    Optional<Cart> findByUserEmail(@Param("correo") String correo);
}

