package com.ecommerce.davivienda.service.product.transactional.product;

import com.ecommerce.davivienda.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

/**
 * Servicio transaccional para operaciones de consulta y persistencia de productos.
 * Maneja todas las operaciones de acceso a datos de productos.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface ProductProductTransactionalService {

    /**
     * Busca un producto por ID.
     *
     * @param productId ID del producto
     * @return Optional con el producto si existe
     */
    Optional<Product> findProductById(Integer productId);

    /**
     * Obtiene todos los productos.
     *
     * @return Lista de todos los productos
     */
    List<Product> findAllProducts();

    /**
     * Busca productos por estado.
     *
     * @param statusId ID del estado del producto
     * @return Lista de productos con el estado especificado
     */
    List<Product> findProductsByStatus(Integer statusId);

    /**
     * Busca productos usando especificación.
     *
     * @param spec Especificación de búsqueda
     * @return Lista de productos que cumplen la especificación
     */
    List<Product> findAllProducts(Specification<Product> spec);

    /**
     * Busca productos usando especificación con paginación.
     *
     * @param spec Especificación de búsqueda
     * @param pageable Configuración de paginación
     * @return Página de productos que cumplen la especificación
     */
    Page<Product> findAllProducts(Specification<Product> spec, Pageable pageable);

    /**
     * Verifica si existe un producto con el nombre dado.
     *
     * @param name Nombre del producto
     * @return true si existe, false en caso contrario
     */
    boolean existsByName(String name);

    /**
     * Verifica si existe un producto con el nombre dado, excluyendo un ID.
     *
     * @param name Nombre del producto
     * @param productId ID del producto a excluir
     * @return true si existe, false en caso contrario
     */
    boolean existsByNameAndNotId(String name, Integer productId);

    /**
     * Guarda un producto.
     *
     * @param product Producto a guardar
     * @return Producto guardado
     */
    Product saveProduct(Product product);
}

