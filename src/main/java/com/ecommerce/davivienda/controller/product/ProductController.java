package com.ecommerce.davivienda.controller.product;

import com.ecommerce.davivienda.constants.Constants;
import com.ecommerce.davivienda.dto.product.*;
import com.ecommerce.davivienda.models.Response;
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
     * @param requestDto Datos del producto a crear
     * @return Response con el producto creado
     */
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('Administrador')")
    public ResponseEntity<Response<ProductResponseDto>> createProduct(
            @Valid @RequestBody ProductRequestDto requestDto) {
        log.info("Request POST /api/v1/products/create - Crear producto: {}", requestDto.getName());

        ProductResponseDto product = productService.createProduct(requestDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Response.<ProductResponseDto>builder()
                        .failure(false)
                        .code(HttpStatus.CREATED.value())
                        .message(Constants.SUCCESS_PRODUCT_CREATED)
                        .body(product)
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
    public ResponseEntity<Response<ProductResponseDto>> getProductById(@PathVariable Integer id) {
        log.info("Request GET /api/v1/products/get-by-id/{} - Obtener producto", id);

        ProductResponseDto product = productService.getProductById(id);

        return ResponseEntity.ok(Response.<ProductResponseDto>builder()
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
    public ResponseEntity<Response<List<ProductResponseDto>>> getAllProducts() {
        log.info("Request GET /api/v1/products/list-all - Listar todos los productos");

        List<ProductResponseDto> products = productService.getAllProducts();

        return ResponseEntity.ok(Response.<List<ProductResponseDto>>builder()
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
    public ResponseEntity<Response<List<ProductResponseDto>>> getActiveProducts() {
        log.info("Request GET /api/v1/products/list-active - Listar productos activos");

        List<ProductResponseDto> products = productService.getActiveProducts();

        return ResponseEntity.ok(Response.<List<ProductResponseDto>>builder()
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
    public ResponseEntity<Response<List<ProductResponseDto>>> searchProducts(
            @RequestBody ProductFilterDto filter) {
        log.info("Request POST /api/v1/products/search - Buscar productos con filtros");

        List<ProductResponseDto> products = productService.searchProducts(filter);

        return ResponseEntity.ok(Response.<List<ProductResponseDto>>builder()
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

        Page<ProductResponseDto> productsPage = productService.searchProductsPaginated(
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
     * Actualiza un producto existente.
     * Requiere rol ADMIN.
     *
     * @param id ID del producto a actualizar
     * @param requestDto Nuevos datos del producto
     * @return Response con el producto actualizado
     */
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response<ProductResponseDto>> updateProduct(
            @PathVariable Integer id,
            @Valid @RequestBody ProductRequestDto requestDto) {
        log.info("Request PUT /api/v1/products/update/{} - Actualizar producto", id);

        ProductResponseDto product = productService.updateProduct(id, requestDto);

        return ResponseEntity.ok(Response.<ProductResponseDto>builder()
                .failure(false)
                .code(HttpStatus.OK.value())
                .message(Constants.SUCCESS_PRODUCT_UPDATED)
                .body(product)
                .timestamp(String.valueOf(System.currentTimeMillis()))
                .build());
    }

    /**
     * Elimina lógicamente un producto (lo marca como inactivo).
     * Requiere rol ADMIN.
     *
     * @param id ID del producto a eliminar
     * @return Response de confirmación
     */
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('Administrador')")
    public ResponseEntity<Response<Void>> deleteProduct(@PathVariable Integer id) {
        log.info("Request DELETE /api/v1/products/delete/{} - Eliminar producto", id);

        productService.deleteProduct(id);

        return ResponseEntity.ok(Response.<Void>builder()
                .failure(false)
                .code(HttpStatus.OK.value())
                .message(Constants.SUCCESS_PRODUCT_DELETED)
                .timestamp(String.valueOf(System.currentTimeMillis()))
                .build());
    }

    /**
     * Activa un producto previamente desactivado.
     * Requiere rol ADMIN.
     *
     * @param id ID del producto a activar
     * @return Response con el producto activado
     */
    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAuthority('Administrador')")
    public ResponseEntity<Response<ProductResponseDto>> activateProduct(@PathVariable Integer id) {
        log.info("Request PATCH /api/v1/products/{}/activate - Activar producto", id);

        ProductResponseDto product = productService.activateProduct(id);

        return ResponseEntity.ok(Response.<ProductResponseDto>builder()
                .failure(false)
                .code(HttpStatus.OK.value())
                .message(Constants.SUCCESS_PRODUCT_ACTIVATED)
                .body(product)
                .timestamp(String.valueOf(System.currentTimeMillis()))
                .build());
    }

    /**
     * Agrega inventario a un producto existente.
     * Suma la cantidad especificada al inventario actual del producto.
     * Requiere rol ADMIN.
     *
     * @param id ID del producto
     * @param requestDto Cantidad de inventario a agregar
     * @return Response con el producto actualizado
     */
    @PatchMapping("/{id}/inventory/add")
    @PreAuthorize("hasAuthority('Administrador')")
    public ResponseEntity<Response<ProductResponseDto>> addInventory(
            @PathVariable Integer id,
            @Valid @RequestBody InventoryAddRequestDto requestDto) {
        log.info("Request PATCH /api/v1/products/{}/inventory/add - Agregar {} unidades de inventario",
                id, requestDto.getQuantity());

        ProductResponseDto product = productService.addInventory(id, requestDto.getQuantity());

        return ResponseEntity.ok(Response.<ProductResponseDto>builder()
                .failure(false)
                .code(HttpStatus.OK.value())
                .message(Constants.SUCCESS_INVENTORY_ADDED)
                .body(product)
                .timestamp(String.valueOf(System.currentTimeMillis()))
                .build());
    }
}

