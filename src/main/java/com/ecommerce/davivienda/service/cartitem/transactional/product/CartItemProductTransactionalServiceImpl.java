package com.ecommerce.davivienda.service.cartitem.transactional.product;

import com.ecommerce.davivienda.entity.product.Product;
import com.ecommerce.davivienda.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Implementación del servicio transaccional para operaciones de Product.
 * Centraliza el acceso a datos de productos.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CartItemProductTransactionalServiceImpl implements CartItemProductTransactionalService {

    private final ProductRepository productRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> findProductById(Integer productId) {
        log.debug("Buscando producto con ID: {}", productId);
        return productRepository.findById(productId);
    }
}

