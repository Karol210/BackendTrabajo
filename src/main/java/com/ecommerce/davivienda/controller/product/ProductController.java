package com.ecommerce.davivienda.controller.product;

import com.ecommerce.davivienda.constants.Constants;
import com.ecommerce.davivienda.dto.product.PagedProductResponseDto;
import com.ecommerce.davivienda.dto.product.ProductFilterDto;
import com.ecommerce.davivienda.models.Response;
import com.ecommerce.davivienda.models.product.ProductRequest;
import com.ecommerce.davivienda.models.product.ProductResponse;
import com.ecommerce.davivienda.models.product.ProductUpdateRequest;
import com.ecommerce.davivienda.service.product.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Controller REST para operaciones CRUD de productos.
 * Expone endpoints para crear, consultar, actualizar, eliminar y buscar productos.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * Crea un nuevo producto en el catálogo.
     * Requiere rol ADMIN.
     *
     * @param request Datos del producto a crear
     * @return Response con mensaje de éxito
     */
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('Administrador')")
    public ResponseEntity<Response<String>> createProduct(
            @Valid @RequestBody ProductRequest request) {
        log.info("Request POST /api/v1/products/create - Crear producto: {}", request.getName());

        productService.createProduct(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Response.<String>builder()
                        .failure(false)
                        .code(HttpStatus.CREATED.value())
                        .message(Constants.SUCCESS_PRODUCT_CREATED)
                        .timestamp(String.valueOf(System.currentTimeMillis()))
                        .build());
    }

    /**
     * Obtiene un producto por su ID.
     *
     * @param id ID del producto
     * @return Response con el producto encontrado
     */
    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<Response<ProductResponse>> getProductById(@PathVariable Integer id) {
        log.info("Request GET /api/v1/products/get-by-id/{} - Obtener producto", id);

        ProductResponse product = productService.getProductById(id);

        return ResponseEntity.ok(Response.<ProductResponse>builder()
                .failure(false)
                .code(HttpStatus.OK.value())
                .message(Constants.SUCCESS_PRODUCT_FOUND)
                .body(product)
                .timestamp(String.valueOf(System.currentTimeMillis()))
                .build());
    }

    /**
     * Lista todos los productos del catálogo.
     *
     * @return Response con la lista de productos
     */
    @GetMapping("/list-all")
    public ResponseEntity<Response<List<ProductResponse>>> getAllProducts() {
        log.info("Request GET /api/v1/products/list-all - Listar todos los productos");

        List<ProductResponse> products = productService.getAllProducts();

        return ResponseEntity.ok(Response.<List<ProductResponse>>builder()
                .failure(false)
                .code(HttpStatus.OK.value())
                .message(Constants.SUCCESS_PRODUCTS_LISTED)
                .body(products)
                .timestamp(String.valueOf(System.currentTimeMillis()))
                .build());
    }

    /**
     * Lista solo los productos activos.
     *
     * @return Response con la lista de productos activos
     */
    @GetMapping("/list-active")
    public ResponseEntity<Response<List<ProductResponse>>> getActiveProducts() {
        log.info("Request GET /api/v1/products/list-active - Listar productos activos");

        List<ProductResponse> products = productService.getActiveProducts();

        return ResponseEntity.ok(Response.<List<ProductResponse>>builder()
                .failure(false)
                .code(HttpStatus.OK.value())
                .message(Constants.SUCCESS_PRODUCTS_LISTED)
                .body(products)
                .timestamp(String.valueOf(System.currentTimeMillis()))
                .build());
    }

    /**
     * Busca productos aplicando filtros.
     *
     * @param filter Filtros de búsqueda
     * @return Response con la lista de productos filtrados
     */
    @PostMapping("/search")
    public ResponseEntity<Response<List<ProductResponse>>> searchProducts(
            @RequestBody ProductFilterDto filter) {
        log.info("Request POST /api/v1/products/search - Buscar productos con filtros");

        List<ProductResponse> products = productService.searchProducts(filter);

        return ResponseEntity.ok(Response.<List<ProductResponse>>builder()
                .failure(false)
                .code(HttpStatus.OK.value())
                .message(Constants.SUCCESS_PRODUCTS_SEARCH)
                .body(products)
                .timestamp(String.valueOf(System.currentTimeMillis()))
                .build());
    }

    /**
     * Busca productos aplicando filtros con paginación.
     *
     * @param categoryId Filtrar por ID de categoría
     * @param minPrice Precio mínimo (ej: 10.99)
     * @param maxPrice Precio máximo (ej: 99.99)
     * @param active Filtrar por estado activo (true/false)
     * @param searchTerm Búsqueda por nombre (búsqueda parcial)
     * @param page Número de página (default: 0)
     * @param size Tamaño de página (default: 10)
     * @param sortBy Campo para ordenar (default: productoId).
     *               <p><b>Valores válidos:</b> productoId, valorUnitario, nombre, descripcion, estadoProductoId, creationDate</p>
     * @param sortDir Dirección de orden (asc/desc, default: asc)
     * @return Response con página de productos filtrados
     */
    @GetMapping("/search/paginated")
    public ResponseEntity<Response<PagedProductResponseDto>> searchProductsPaginated(
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "productoId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        log.info("GET /api/v1/products/search/paginated - Buscar con filtros y paginación");

        Page<ProductResponse> productsPage = productService.searchProductsPaginated(
                categoryId, minPrice, maxPrice, active, searchTerm,
                page, size, sortBy, sortDir);

        PagedProductResponseDto pagedResponse = PagedProductResponseDto.fromPage(productsPage);

        return ResponseEntity.ok(Response.<PagedProductResponseDto>builder()
                .failure(false)
                .code(HttpStatus.OK.value())
                .message(Constants.SUCCESS_PRODUCTS_SEARCH)
                .body(pagedResponse)
                .timestamp(String.valueOf(System.currentTimeMillis()))
                .build());
    }

    /**
     * Actualiza un producto existente por cualquier criterio.
     * Requiere rol ADMIN.
     * Permite actualización parcial o completa de campos.
     * Solo los campos proporcionados en el request serán actualizados.
     *
     * @param id ID del producto a actualizar
     * @param request Datos del producto a actualizar (todos los campos opcionales)
     * @return Response con mensaje de actualización exitosa
     */
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('Administrador')")
    public ResponseEntity<Response<String>> updateProduct(
            @PathVariable Integer id,
            @Valid @RequestBody ProductUpdateRequest request) {
        log.info("Request PUT /api/v1/products/update/{} - Actualizar producto", id);

        productService.updateProduct(id, request);

        return ResponseEntity.ok(Response.<String>builder()
                .failure(false)
                .code(HttpStatus.OK.value())
                .message(Constants.SUCCESS_PRODUCT_UPDATED)
                .timestamp(String.valueOf(System.currentTimeMillis()))
                .build());
    }

}

