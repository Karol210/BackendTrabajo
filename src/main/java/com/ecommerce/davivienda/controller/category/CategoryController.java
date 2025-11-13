package com.ecommerce.davivienda.controller.category;

import com.ecommerce.davivienda.dto.category.CategoryResponseDto;
import com.ecommerce.davivienda.models.Response;
import com.ecommerce.davivienda.service.category.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller para gestionar endpoints de categorías.
 * Proporciona operaciones de consulta sobre categorías de productos.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * Lista todas las categorías disponibles.
     *
     * @return Response con lista de categorías
     */
    @GetMapping("/list-all")
    public ResponseEntity<Response<List<CategoryResponseDto>>> getAllCategories() {
        log.info("GET /api/v1/categories/list-all - Listar todas las categorías");

        List<CategoryResponseDto> categories = categoryService.getAllCategories();

        Response<List<CategoryResponseDto>> response = Response.<List<CategoryResponseDto>>builder()
                .failure(false)
                .code(HttpStatus.OK.value())
                .message("Categorías listadas exitosamente")
                .body(categories)
                .timestamp(String.valueOf(System.currentTimeMillis()))
                .build();

        log.info("Categorías listadas exitosamente. Total: {}", categories.size());
        return ResponseEntity.ok(response);
    }


    /**
     * Busca una categoría por su nombre.
     *
     * @param name Nombre de la categoría
     * @return Response con la categoría
     */
    @GetMapping("/find-by-name/{name}")
    public ResponseEntity<Response<CategoryResponseDto>> getCategoryByName(@PathVariable String name) {
        log.info("GET /api/v1/categories/find-by-name/{} - Buscar categoría por nombre", name);

        CategoryResponseDto category = categoryService.getCategoryByName(name);

        Response<CategoryResponseDto> response = Response.<CategoryResponseDto>builder()
                .failure(false)
                .code(HttpStatus.OK.value())
                .message("Categoría encontrada")
                .body(category)
                .timestamp(String.valueOf(System.currentTimeMillis()))
                .build();

        log.info("Categoría encontrada: {}", category.getName());
        return ResponseEntity.ok(response);
    }
}

