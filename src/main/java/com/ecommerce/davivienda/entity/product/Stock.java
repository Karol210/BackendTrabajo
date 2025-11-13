package com.ecommerce.davivienda.entity.product;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa el inventario/stock de productos.
 * Mapea la tabla 'stock' en la base de datos.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "stock")
public class Stock {

    /**
     * Identificador único del registro de stock.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_id")
    private Integer stockId;

    /**
     * ID del producto asociado (único).
     */
    @Column(name = "producto_id", nullable = false, unique = true)
    private Integer productoId;

    /**
     * Cantidad disponible en inventario.
     */
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    /**
     * Relación con el producto.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", insertable = false, updatable = false)
    private Product product;

    /**
     * Verifica si hay suficiente stock para la cantidad solicitada.
     *
     * @param requestedQuantity Cantidad solicitada
     * @return true si hay suficiente stock
     */
    public boolean hasEnoughStock(Integer requestedQuantity) {
        return cantidad != null && requestedQuantity != null && cantidad >= requestedQuantity;
    }

    /**
     * Verifica si el stock está disponible (cantidad > 0).
     *
     * @return true si hay stock disponible
     */
    public boolean isAvailable() {
        return cantidad != null && cantidad > 0;
    }
}

