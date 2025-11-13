package com.ecommerce.davivienda.entity.product;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad que representa un producto del catálogo.
 * Mapea la tabla 'productos' en la base de datos existente.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "productos")
public class Product {

    /**
     * Identificador único del producto.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "producto_id")
    private Integer productoId;

    /**
     * Nombre del producto.
     */
    @Column(name = "nombre", nullable = false, length = 200)
    private String nombre;

    /**
     * Descripción del producto.
     */
    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    /**
     * Valor unitario del producto (campo existente en BD).
     */
    @Column(name = "valor_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorUnitario;

    /**
     * Porcentaje de IVA aplicado al producto.
     */
    @Column(name = "iva", precision = 5, scale = 2)
    private BigDecimal iva;

    /**
     * URL o ruta de la imagen del producto.
     */
    @Column(name = "imagen", length = 500)
    private String imagen;

    /**
     * Estado del producto (referencia a tabla estado_producto).
     */
    @Column(name = "estado_producto_id")
    private Integer estadoProductoId;

    /**
     * Categoría a la que pertenece el producto.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoria_id")
    private Category categoria;

    /**
     * Fecha de creación del producto.
     */
    @Column(name = "creation_date", updatable = false)
    private LocalDateTime creationDate;

    /**
     * Hook ejecutado antes de persistir la entidad.
     */
    @PrePersist
    protected void onCreate() {
        if (creationDate == null) {
            creationDate = LocalDateTime.now();
        }
        if (iva == null) {
            iva = BigDecimal.ZERO;
        }
        if (estadoProductoId == null) {
            estadoProductoId = 1; // 1 = Activo
        }
    }

    /**
     * Calcula el precio con IVA incluido.
     *
     * @return Precio total (valor unitario + IVA)
     */
    public BigDecimal getPrecioConIva() {
        if (valorUnitario == null) {
            return BigDecimal.ZERO;
        }
        if (iva == null || iva.compareTo(BigDecimal.ZERO) == 0) {
            return valorUnitario;
        }
        BigDecimal montoIva = valorUnitario.multiply(iva).divide(BigDecimal.valueOf(100), 2, java.math.RoundingMode.HALF_UP);
        return valorUnitario.add(montoIva);
    }

    /**
     * Verifica si el producto está activo.
     *
     * @return true si estadoProductoId == 1 (Activo)
     */
    public boolean isActive() {
        return estadoProductoId != null && estadoProductoId == 1;
    }

    /**
     * Activa el producto.
     */
    public void activate() {
        this.estadoProductoId = 1; // 1 = Activo
    }

    /**
     * Desactiva el producto.
     */
    public void deactivate() {
        this.estadoProductoId = 2; // 2 = Inactivo
    }
}

