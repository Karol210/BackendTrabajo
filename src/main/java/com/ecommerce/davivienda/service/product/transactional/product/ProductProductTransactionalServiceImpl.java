package com.ecommerce.davivienda.service.product.transactional.product;

import com.ecommerce.davivienda.entity.product.Product;
import com.ecommerce.davivienda.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio transaccional para operaciones de consulta y persistencia de productos.
 * Centraliza todas las operaciones de acceso a datos de productos.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductProductTransactionalServiceImpl implements ProductProductTransactionalService {

    private final ProductRepository productRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> findProductById(Integer productId) {
        log.debug("Buscando producto con ID: {}", productId);
        return productRepository.findById(productId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAllProducts() {
        log.debug("Obteniendo todos los productos");
        return productRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findProductsByStatus(Integer statusId) {
        log.debug("Buscando productos con estado: {}", statusId);
        return productRepository.findByEstadoProductoId(statusId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAllProducts(Specification<Product> spec) {
        log.debug("Buscando productos con especificación");
        return productRepository.findAll(spec);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> findAllProducts(Specification<Product> spec, Pageable pageable) {
        log.debug("Buscando productos paginados con especificación");
        return productRepository.findAll(spec, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        log.debug("Verificando existencia de producto con nombre: {}", name);
        return productRepository.existsByNombre(name);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByNameAndNotId(String name, Integer productId) {
        log.debug("Verificando existencia de producto con nombre {} excluyendo ID {}", name, productId);
        return productRepository.existsByNombreAndProductoIdNot(name, productId);
    }

    @Override
    @Transactional
    public Product saveProduct(Product product) {
        log.debug("Guardando producto: {}", product.getNombre());
        return productRepository.save(product);
    }
}

