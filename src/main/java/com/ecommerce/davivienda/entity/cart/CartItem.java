package com.ecommerce.davivienda.entity.cart;

import com.ecommerce.davivienda.entity.product.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Entidad que representa un producto dentro de un carrito de compras.
 * Mapea la tabla 'productos_carrito' en la base de datos.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "productos_carrito", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"carrito_id", "producto_id"})
})
public class CartItem {

    /**
     * Identificador único del item del carrito.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productos_carrito_id")
    private Integer productosCarritoId;

    /**
     * Carrito al que pertenece este item.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrito_id", nullable = false)
    private Cart cart;

    /**
     * Producto asociado a este item.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "producto_id", nullable = false)
    private Product product;

    /**
     * Cantidad del producto en el carrito.
     */
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    /**
     * Calcula el monto del IVA para la cantidad especificada.
     *
     * @return Monto del IVA
     */
    public BigDecimal calculateIvaAmount() {
        if (product == null || product.getValorUnitario() == null || cantidad == null) {
            return BigDecimal.ZERO;
        }
        BigDecimal iva = product.getIva() != null ? product.getIva() : BigDecimal.ZERO;
        if (iva.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal montoIvaPorUnidad = product.getValorUnitario()
                .multiply(iva)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        return montoIvaPorUnidad.multiply(BigDecimal.valueOf(cantidad))
                .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calcula el subtotal sin IVA (valor unitario * cantidad).
     *
     * @return Subtotal sin IVA
     */
    public BigDecimal calculateSubtotal() {
        if (product == null || product.getValorUnitario() == null || cantidad == null) {
            return BigDecimal.ZERO;
        }
        return product.getValorUnitario()
                .multiply(BigDecimal.valueOf(cantidad))
                .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calcula el precio total con IVA incluido (subtotal + IVA).
     *
     * @return Precio total con IVA
     */
    public BigDecimal calculateTotal() {
        return calculateSubtotal().add(calculateIvaAmount());
    }

    /**
     * Verifica si la cantidad es válida.
     *
     * @return true si cantidad es mayor a 0
     */
    public boolean isValidQuantity() {
        return cantidad != null && cantidad > 0;
    }

    /**
     * Hook ejecutado antes de persistir o actualizar.
     */
    @PrePersist
    @PreUpdate
    protected void validateQuantity() {
        if (cantidad == null || cantidad <= 0) {
            throw new IllegalStateException("La cantidad debe ser mayor a 0");
        }
    }
}

