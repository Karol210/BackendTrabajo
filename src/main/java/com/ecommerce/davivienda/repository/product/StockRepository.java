package com.ecommerce.davivienda.repository.product;

import com.ecommerce.davivienda.entity.product.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para operaciones de persistencia de stock/inventario.
 * Proporciona métodos para consultar y gestionar el inventario de productos.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Repository
public interface StockRepository extends JpaRepository<Stock, Integer> {

    /**
     * Busca el stock de un producto específico.
     *
     * @param productoId ID del producto
     * @return Optional con el stock si existe
     */
    Optional<Stock> findByProductoId(Integer productoId);

    /**
     * Verifica si existe stock para un producto.
     *
     * @param productoId ID del producto
     * @return true si existe registro de stock
     */
    boolean existsByProductoId(Integer productoId);
}

