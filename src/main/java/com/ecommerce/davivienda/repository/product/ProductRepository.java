package com.ecommerce.davivienda.repository.product;

import com.ecommerce.davivienda.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para operaciones sobre la entidad Product.
 * Incluye soporte para consultas dinámicas con Specifications.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {

    /**
     * Lista todos los productos activos (estadoProductoId = 1).
     *
     * @return Lista de productos activos
     */
    List<Product> findByEstadoProductoId(Integer estadoProductoId);


    /**
     * Verifica si existe un producto con el nombre dado.
     *
     * @param nombre Nombre del producto
     * @return true si existe, false en caso contrario
     */
    boolean existsByNombre(String nombre);

    /**
     * Verifica si existe un producto con el nombre dado, excluyendo un ID.
     * Útil para validación en actualizaciones.
     *
     * @param nombre Nombre del producto
     * @param productoId ID del producto a excluir
     * @return true si existe otro producto con ese nombre
     */
    boolean existsByNombreAndProductoIdNot(String nombre, Integer productoId);
}

