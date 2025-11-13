package com.ecommerce.davivienda.service.product;

import com.ecommerce.davivienda.dto.product.ProductFilterDto;
import com.ecommerce.davivienda.entity.product.Category;
import com.ecommerce.davivienda.entity.product.Product;
import com.ecommerce.davivienda.mapper.product.ProductMapper;
import com.ecommerce.davivienda.models.product.ProductRequest;
import com.ecommerce.davivienda.models.product.ProductResponse;
import com.ecommerce.davivienda.models.product.ProductUpdateRequest;
import com.ecommerce.davivienda.service.product.builder.ProductBuilderService;
import com.ecommerce.davivienda.service.product.transactional.product.ProductProductTransactionalService;
import com.ecommerce.davivienda.service.product.validation.category.ProductCategoryValidationService;
import com.ecommerce.davivienda.service.product.validation.common.ProductCommonValidationService;
import com.ecommerce.davivienda.service.product.validation.product.ProductProductValidationService;
import com.ecommerce.davivienda.service.stock.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del servicio principal de productos.
 * Coordina operaciones CRUD delegando a subcapacidades organizadas por dominios.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    // Validation subcapacidades
    private final ProductProductValidationService productValidationService;
    private final ProductCategoryValidationService categoryValidationService;
    private final ProductCommonValidationService commonValidationService;

    // Transactional subcapacidad
    private final ProductProductTransactionalService transactionalService;

    private final ProductMapper productMapper;
    private final ProductBuilderService builderService;
    private final StockService stockService;

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        log.info("Creando producto: {}", request.getName());

        validateCreateRequest(request);
        Category category = categoryValidationService.findCategoryByNameOrThrow(request.getCategoryName());

        Product product = productMapper.toEntity(request);
        product.setCategoria(category);

        Product savedProduct = transactionalService.saveProduct(product);

        // Actualizar stock si se proporcionó cantidad de inventario
        if (request.getInventory() != null && request.getInventory() >= 0) {
            stockService.createOrUpdateStock(savedProduct.getProductoId(), request.getInventory());
        }

        return productMapper.toResponseDto(savedProduct);
    }

    private void validateCreateRequest(ProductRequest request) {
        productValidationService.validateProductNameNotExists(request.getName());
        commonValidationService.validatePrices(request);
        Category category = categoryValidationService.findCategoryByNameOrThrow(request.getCategoryName());
        categoryValidationService.validateCategoryActive(category);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Integer id) {
        log.info("Obteniendo producto por ID: {}", id);

        Product product = productValidationService.findProductByIdOrThrow(id);
        return productMapper.toResponseDto(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        log.info("Listando todos los productos");

        List<Product> products = transactionalService.findAllProducts();
        return products.stream()
                .map(productMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getActiveProducts() {
        log.info("Listando productos activos");

        List<Product> products = transactionalService.findProductsByStatus(1); 
        return products.stream()
                .map(productMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> searchProducts(ProductFilterDto filter) {
        log.info("Buscando productos con filtros: {}", filter);

        Specification<Product> spec = builderService.buildSpecificationFromFilter(filter);
        List<Product> products = transactionalService.findAllProducts(spec);

        return products.stream()
                .map(productMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> searchProductsPaginated(
            Integer categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Boolean active,
            String searchTerm,
            int page,
            int size,
            String sortBy,
            String sortDir) {

        log.info("Buscando productos paginados: page={}, size={}", page, size);

        Specification<Product> spec = builderService.buildSpecificationFromParams(
                categoryId, minPrice, maxPrice, active, searchTerm);
        Pageable pageable = builderService.buildPageable(page, size, sortBy, sortDir);

        Page<Product> productsPage = transactionalService.findAllProducts(spec, pageable);
        return productsPage.map(productMapper::toResponseDto);
    }

    @Override
    @Transactional
    public void updateProduct(Integer id, ProductUpdateRequest request) {
        log.info("Actualizando producto con ID: {}", id);

        Product existingProduct = productValidationService.findProductByIdOrThrow(id);
        validateUpdateRequest(request, id);

        productMapper.updateEntityFromDto(request, existingProduct);

        if (request.getCategoryName() != null && !request.getCategoryName().trim().isEmpty()) {
            Category category = categoryValidationService.findCategoryByNameOrThrow(request.getCategoryName());
            categoryValidationService.validateCategoryActive(category);
            existingProduct.setCategoria(category);
        }

        Product updatedProduct = transactionalService.saveProduct(existingProduct);

        if (request.getInventory() != null && request.getInventory() >= 0) {
            stockService.createOrUpdateStock(updatedProduct.getProductoId(), request.getInventory());
        }

        log.info("Producto {} actualizado exitosamente", id);
    }

    private void validateUpdateRequest(ProductUpdateRequest request, Integer productId) {
        if (request.getName() != null && !request.getName().trim().isEmpty()) {
            productValidationService.validateProductNameNotExistsOnUpdate(request.getName(), productId);
        }
        if (request.getUnitValue() != null || request.getIva() != null) {
            commonValidationService.validatePrices(request);
        }
    }

}

