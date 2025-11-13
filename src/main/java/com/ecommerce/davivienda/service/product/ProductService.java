package com.ecommerce.davivienda.service.product;

import com.ecommerce.davivienda.dto.product.ProductFilterDto;
import com.ecommerce.davivienda.dto.product.ProductRequestDto;
import com.ecommerce.davivienda.dto.product.ProductResponseDto;
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
    ProductResponseDto createProduct(ProductRequestDto request);

    /**
     * Obtiene un producto por su ID.
     *
     * @param id ID del producto
     * @return Producto encontrado
     * @throws com.ecommerce.davivienda.exception.product.ProductException si no existe
     */
    ProductResponseDto getProductById(Integer id);

    /**
     * Lista todos los productos del catálogo.
     *
     * @return Lista de todos los productos
     */
    List<ProductResponseDto> getAllProducts();

    /**
     * Lista solo los productos activos.
     *
     * @return Lista de productos activos
     */
    List<ProductResponseDto> getActiveProducts();

    /**
     * Busca productos aplicando filtros.
     *
     * @param filter Filtros de búsqueda
     * @return Lista de productos filtrados
     */
    List<ProductResponseDto> searchProducts(ProductFilterDto filter);

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
    Page<ProductResponseDto> searchProductsPaginated(
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
     * Actualiza un producto existente.
     *
     * @param id ID del producto a actualizar
     * @param request Nuevos datos del producto
     * @return Producto actualizado
     * @throws com.ecommerce.davivienda.exception.product.ProductException si no existe o hay errores
     */
    ProductResponseDto updateProduct(Integer id, ProductRequestDto request);

    /**
     * Elimina lógicamente un producto (lo marca como inactivo).
     *
     * @param id ID del producto a eliminar
     * @throws com.ecommerce.davivienda.exception.product.ProductException si no existe
     */
    void deleteProduct(Integer id);

    /**
     * Activa un producto previamente desactivado.
     *
     * @param id ID del producto a activar
     * @return Producto activado
     * @throws com.ecommerce.davivienda.exception.product.ProductException si no existe
     */
    ProductResponseDto activateProduct(Integer id);

    /**
     * Agrega inventario a un producto existente.
     *
     * @param id ID del producto
     * @param quantity Cantidad de inventario a agregar
     * @return Producto con inventario actualizado
     * @throws com.ecommerce.davivienda.exception.product.ProductException si no existe o cantidad inválida
     */
    ProductResponseDto addInventory(Integer id, Integer quantity);
}

