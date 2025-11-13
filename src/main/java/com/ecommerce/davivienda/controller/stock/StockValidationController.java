package com.ecommerce.davivienda.controller.stock;

import com.ecommerce.davivienda.dto.stock.StockValidationRequestDto;
import com.ecommerce.davivienda.dto.stock.StockValidationResponseDto;
import com.ecommerce.davivienda.models.Response;
import com.ecommerce.davivienda.service.stock.StockValidationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.ecommerce.davivienda.constants.Constants.SUCCESS_STOCK_AVAILABLE;

/**
 * Controlador REST para validación de stock/inventario.
 * Proporciona endpoint para verificar disponibilidad de productos en el carrito.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/stock")
@RequiredArgsConstructor
public class StockValidationController {

    private final StockValidationService stockValidationService;

    /**
     * Valida que todos los productos del carrito de un usuario tengan stock suficiente.
     * 
     * <p>Flujo del endpoint:</p>
     * <ol>
     *   <li>Recibe tipo y número de documento del usuario</li>
     *   <li>Busca al usuario y su carrito</li>
     *   <li>Valida stock de cada producto en el carrito</li>
     *   <li>Si hay stock insuficiente → retorna 400 con lista de productos</li>
     *   <li>Si todo tiene stock → retorna 200 con available=true</li>
     * </ol>
     * 
     * <p>Endpoint: POST /api/v1/stock/validate</p>
     *
     * @param request DTO con documentType y documentNumber
     * @return Response con available=true si hay stock, o available=false con lista de productos sin stock
     */
    @PostMapping("/validate")
    public ResponseEntity<Response<StockValidationResponseDto>> validateCartStock(
            @Valid @RequestBody StockValidationRequestDto request) {
        
        log.info("Request para validar stock del carrito - Usuario: {} {}", 
                request.getDocumentType(), request.getDocumentNumber());
        
        StockValidationResponseDto validationResult = stockValidationService.validateCartStock(request);
        
        log.info("Validación de stock exitosa - available: {}", validationResult.getAvailable());
        
        return ResponseEntity.status(HttpStatus.OK)
                .body(Response.<StockValidationResponseDto>builder()
                        .failure(false)
                        .code(HttpStatus.OK.value())
                        .message(SUCCESS_STOCK_AVAILABLE)
                        .body(validationResult)
                        .timestamp(String.valueOf(System.currentTimeMillis()))
                        .build());
    }
}

