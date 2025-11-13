package com.ecommerce.davivienda.controller.stock;

import com.ecommerce.davivienda.dto.stock.StockValidationResponseDto;
import com.ecommerce.davivienda.models.Response;
import com.ecommerce.davivienda.service.stock.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST para operaciones de stock/inventario.
 * Proporciona endpoints para gestión y validación de inventario.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/stock")
@RequiredArgsConstructor
public class StockValidationController {

    private final StockService stockService;

    /**
     * Valida que todos los productos del carrito del usuario autenticado tengan stock suficiente.
     * Usa el token JWT para identificar al usuario y obtener su carrito.
     * 
     * <p>Flujo del endpoint:</p>
     * <ol>
     *   <li>Extrae el userRoleId del token JWT del usuario autenticado</li>
     *   <li>Obtiene el carrito del usuario</li>
     *   <li>Valida stock de cada producto en el carrito</li>
     *   <li>Si hay stock insuficiente → retorna 200 con available=false y lista de productos</li>
     *   Si todo tiene stock → retorna 200 con available=true
     *
     * @return Response con resultado de validación detallado
     */
    @GetMapping("/validate")
    public ResponseEntity<Response<StockValidationResponseDto>> validateCartStock() {
        
        log.info("GET /api/v1/stock/validate - Validar stock del carrito (usuario autenticado)");
        
        StockValidationResponseDto validationResult = stockService.validateCartStock();
        
        log.info("Validación de stock completada - available: {}, productos con problemas: {}", 
                validationResult.getAvailable(), validationResult.getProductsWithIssues());
        
        // Siempre retornar 200 OK, el campo 'available' indica si puede continuar
        return ResponseEntity.status(HttpStatus.OK)
                .body(Response.<StockValidationResponseDto>builder()
                        .failure(false)
                        .code(HttpStatus.OK.value())
                        .message(validationResult.getMessage())
                        .body(validationResult)
                        .timestamp(String.valueOf(System.currentTimeMillis()))
                        .build());
    }
}
