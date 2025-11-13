package com.ecommerce.davivienda.entity.cart;

import com.ecommerce.davivienda.entity.user.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa un carrito de compras.
 * Mapea la tabla 'carrito' en la base de datos.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "carrito")
public class Cart {

    /**
     * Identificador único del carrito.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "carrito_id")
    private Integer carritoId;

    /**
     * ID del UserRole asociado al carrito.
     */
    @Column(name = "usuario_rol_id", nullable = false)
    private Integer usuarioRolId;

    /**
     * ID del estado del carrito.
     * Estados: 1=Activo, 2=Procesando, 3=Completado, 4=Abandonado, 5=Expirado, 6=Cancelado
     */
    @Column(name = "estado_carrito_id", nullable = false)
    @Builder.Default
    private Integer estadoCarritoId = 1;

    /**
     * Relación opcional con UserRole (si se necesita carga eager).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_rol_id", insertable = false, updatable = false)
    private UserRole userRole;

    /**
     * Lista de items en el carrito.
     */
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<CartItem> items = new ArrayList<>();

    /**
     * Agrega un item al carrito.
     *
     * @param item Item a agregar
     */
    public void addItem(CartItem item) {
        items.add(item);
        item.setCart(this);
    }

    /**
     * Remueve un item del carrito.
     *
     * @param item Item a remover
     */
    public void removeItem(CartItem item) {
        items.remove(item);
        item.setCart(null);
    }

    /**
     * Limpia todos los items del carrito.
     */
    public void clearItems() {
        items.clear();
    }
}

