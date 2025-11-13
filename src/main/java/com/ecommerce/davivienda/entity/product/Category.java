package com.ecommerce.davivienda.entity.product;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa una categoría de productos.
 * Mapea la tabla 'categorias' en la base de datos.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categorias")
public class Category {

    /**
     * Identificador único de la categoría.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categoria_id")
    private Integer categoriaId;

    /**
     * Nombre de la categoría.
     */
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    /**
     * Descripción de la categoría.
     */
    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    /**
     * Verifica si la categoría está activa.
     * Por defecto, todas las categorías existentes se consideran activas.
     *
     * @return true siempre (las categorías en el sistema actual están activas)
     */
    public boolean isActive() {
        return true;
    }
}

