package com.ecommerce.davivienda.service.product;

import com.ecommerce.davivienda.dto.product.ProductFilterDto;
import com.ecommerce.davivienda.models.product.ProductRequest;
import com.ecommerce.davivienda.models.product.ProductResponse;
import com.ecommerce.davivienda.models.product.ProductUpdateRequest;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

/**
 * Servicio principal para operaciones CRUD de productos.
 * Coordina las operaciones entre validaciones, construcción y persistencia.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface ProductService {

    /**
     * Crea un nuevo producto en el catálogo.
     *
     * @param request Datos del producto a crear
     * @return Producto creado
     * @throws com.ecommerce.davivienda.exception.product.ProductException si hay errores de validación
     */
    ProductResponse createProduct(ProductRequest request);

    /**
     * Obtiene un producto por su ID.
     *
     * @param id ID del producto
     * @return Producto encontrado
     * @throws com.ecommerce.davivienda.exception.product.ProductException si no existe
     */
    ProductResponse getProductById(Integer id);

    /**
     * Lista todos los productos del catálogo.
     *
     * @return Lista de todos los productos
     */
    List<ProductResponse> getAllProducts();

    /**
     * Lista solo los productos activos.
     *
     * @return Lista de productos activos
     */
    List<ProductResponse> getActiveProducts();

    /**
     * Busca productos aplicando filtros.
     *
     * @param filter Filtros de búsqueda
     * @return Lista de productos filtrados
     */
    List<ProductResponse> searchProducts(ProductFilterDto filter);

    /**
     * Busca productos aplicando filtros con paginación.
     *
     * @param categoryId Filtrar por ID de categoría
     * @param minPrice Precio mínimo
     * @param maxPrice Precio máximo
     * @param active Filtrar por estado activo
     * @param searchTerm Búsqueda por nombre
     * @param page Número de página
     * @param size Tamaño de página
     * @param sortBy Campo para ordenar
     * @param sortDir Dirección de orden
     * @return Página de productos filtrados
     */
    Page<ProductResponse> searchProductsPaginated(
            Integer categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Boolean active,
            String searchTerm,
            int page,
            int size,
            String sortBy,
            String sortDir);

    /**
     * Actualiza un producto existente por cualquier criterio.
     * Permite actualización parcial o completa de campos.
     * Solo los campos proporcionados en el request serán actualizados.
     *
     * @param id ID del producto a actualizar
     * @param request Datos del producto a actualizar (todos los campos opcionales)
     * @throws com.ecommerce.davivienda.exception.product.ProductException si el producto no existe
     */
    void updateProduct(Integer id, ProductUpdateRequest request);

}

