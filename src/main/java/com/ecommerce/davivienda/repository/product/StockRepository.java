package com.ecommerce.davivienda.repository.product;

import com.ecommerce.davivienda.entity.product.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
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
     * Busca el stock de múltiples productos.
     *
     * @param productIds Lista de IDs de productos
     * @return Lista de registros de stock
     */
    @Query("SELECT s FROM Stock s WHERE s.productoId IN :productIds")
    List<Stock> findByProductoIdIn(@Param("productIds") List<Integer> productIds);

    /**
     * Verifica si existe stock para un producto.
     *
     * @param productoId ID del producto
     * @return true si existe registro de stock
     */
    boolean existsByProductoId(Integer productoId);
}

