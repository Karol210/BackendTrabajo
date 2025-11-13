package com.ecommerce.davivienda.service.product;

import com.ecommerce.davivienda.dto.product.ProductFilterDto;
import com.ecommerce.davivienda.dto.product.ProductRequestDto;
import com.ecommerce.davivienda.dto.product.ProductResponseDto;
import com.ecommerce.davivienda.entity.product.Category;
import com.ecommerce.davivienda.entity.product.Product;
import com.ecommerce.davivienda.mapper.product.ProductMapper;
import com.ecommerce.davivienda.repository.product.ProductRepository;
import com.ecommerce.davivienda.service.product.builder.ProductBuilderService;
import com.ecommerce.davivienda.service.product.validation.ProductValidationService;
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
 * Coordina operaciones CRUD delegando validaciones y construcción a servicios especializados.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ProductValidationService validationService;
    private final ProductBuilderService builderService;

    @Override
    @Transactional
    public ProductResponseDto createProduct(ProductRequestDto request) {
        log.info("Creando producto: {}", request.getName());

        validationService.validateCreateRequest(request);
        Category category = validationService.findCategoryByIdOrThrow(request.getCategoryId());

        Product product = productMapper.toEntity(request);
        product.setCategoria(category);

        Product savedProduct = productRepository.save(product);
        log.info("Producto creado exitosamente con ID: {}", savedProduct.getProductoId());

        return productMapper.toResponseDto(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponseDto getProductById(Integer id) {
        log.info("Obteniendo producto por ID: {}", id);

        Product product = validationService.findProductByIdOrThrow(id);
        return productMapper.toResponseDto(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getAllProducts() {
        log.info("Listando todos los productos");

        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(productMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getActiveProducts() {
        log.info("Listando productos activos");

        List<Product> products = productRepository.findByEstadoProductoId(1); // 1 = Activo
        return products.stream()
                .map(productMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDto> searchProducts(ProductFilterDto filter) {
        log.info("Buscando productos con filtros: {}", filter);

        Specification<Product> spec = builderService.buildSpecificationFromFilter(filter);
        List<Product> products = productRepository.findAll(spec);

        return products.stream()
                .map(productMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponseDto> searchProductsPaginated(
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

        Page<Product> productsPage = productRepository.findAll(spec, pageable);
        return productsPage.map(productMapper::toResponseDto);
    }

    @Override
    @Transactional
    public ProductResponseDto updateProduct(Integer id, ProductRequestDto request) {
        log.info("Actualizando producto ID: {}", id);

        Product product = validationService.findProductByIdOrThrow(id);
        validationService.validateUpdateRequest(id, request);
        Category category = validationService.findCategoryByIdOrThrow(request.getCategoryId());

        productMapper.updateEntityFromDto(request, product);
        product.setCategoria(category);

        Product updatedProduct = productRepository.save(product);
        log.info("Producto actualizado exitosamente: {}", id);

        return productMapper.toResponseDto(updatedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(Integer id) {
        log.info("Eliminando producto ID: {}", id);

        Product product = validationService.findProductByIdOrThrow(id);
        product.deactivate(); // Cambia estadoProductoId a 2 (Inactivo)

        productRepository.save(product);
        log.info("Producto eliminado exitosamente: {}", id);
    }

    @Override
    @Transactional
    public ProductResponseDto activateProduct(Integer id) {
        log.info("Activando producto ID: {}", id);

        Product product = validationService.findProductByIdOrThrow(id);
        product.activate(); // Cambia estadoProductoId a 1 (Activo)

        Product activatedProduct = productRepository.save(product);
        log.info("Producto activado exitosamente: {}", id);

        return productMapper.toResponseDto(activatedProduct);
    }

    @Override
    @Transactional
    public ProductResponseDto addInventory(Integer id, Integer quantity) {
        log.info("Agregando {} unidades de inventario al producto ID: {}", quantity, id);

        validationService.validateInventoryQuantity(quantity);
        Product product = validationService.findProductByIdOrThrow(id);

        // Nota: El inventario se maneja en tabla 'stock' separada
        // Por ahora, solo se valida la operación
        log.warn("La funcionalidad de inventario requiere tabla 'stock' separada");
        log.info("Inventario NO actualizado. Se requiere implementar tabla stock");

        return productMapper.toResponseDto(product);
    }
}

