package com.ecommerce.davivienda.service.cartitem.transactional.product;

import com.ecommerce.davivienda.entity.product.Product;

import java.util.Optional;

/**
 * Servicio transaccional para operaciones de consulta de Product.
 * Responsabilidad: Acceso a datos de productos.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface CartItemProductTransactionalService {

    /**
     * Busca un producto por ID.
     *
     * @param productId ID del producto
     * @return Optional con el producto si existe
     */
    Optional<Product> findProductById(Integer productId);
}

