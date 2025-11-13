package com.ecommerce.davivienda.controller.cart;

import com.ecommerce.davivienda.dto.cart.summary.CartSummaryDto;
import com.ecommerce.davivienda.models.Response;
import com.ecommerce.davivienda.models.cart.CartItemRequest;
import com.ecommerce.davivienda.service.cartitem.CartItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.ecommerce.davivienda.constants.Constants.*;

/**
 * Controlador REST para gestión de items del carrito de compras.
 * Proporciona endpoints para operaciones CRUD con cálculos de precios e IVA.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/cart-items")
@RequiredArgsConstructor
public class CartItemController {

    private final CartItemService cartItemService;

    /**
     * Agrega un producto al carrito del usuario autenticado.
     * Si el producto ya existe, actualiza la cantidad con el nuevo valor.
     * Si el carrito no existe, lo crea automáticamente.
     * El usuario se obtiene automáticamente del token JWT.
     * Endpoint: POST /api/v1/cart-items/add
     *
     * @param request DTO con datos del item a agregar
     * @return Response con mensaje de éxito
     */
    @PostMapping("/add")
    public ResponseEntity<Response<String>> addItemToCart(
            @Valid @RequestBody CartItemRequest request) {
        
        log.info("Request para agregar producto {} al carrito de usuario autenticado", 
                request.getProductId());
        
        Response<String> response = cartItemService.addItemToCart(request);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

   

    /**
     * Elimina un item del carrito.
     * Endpoint: DELETE /api/v1/cart-items/{id}
     *
     * @param id ID del item a eliminar
     * @return Response con mensaje de éxito
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Response<String>> removeItemFromCart(
            @PathVariable("id") Integer id) {
        
        log.info("Request para eliminar item {} del carrito", id);
        
        Response<String> response = cartItemService.removeItemFromCart(id);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene un resumen completo del carrito del usuario autenticado con totales agregados.
     * Incluye: lista de items, subtotal, IVA total y precio total.
     * El usuario se obtiene automáticamente del token JWT.
     * Endpoint: GET /api/v1/cart-items/summary
     *
     * @return Response con resumen del carrito
     */
    @GetMapping("/summary")
    public ResponseEntity<Response<CartSummaryDto>> getCartSummary() {
        
        log.info("Request para obtener resumen del carrito del usuario autenticado");
        
        CartSummaryDto summary = cartItemService.getCartSummary();
        
        return ResponseEntity.ok(Response.<CartSummaryDto>builder()
                .failure(false)
                .code(HttpStatus.OK.value())
                .message(SUCCESS_CART_ITEMS_FOUND)
                .body(summary)
                .timestamp(String.valueOf(System.currentTimeMillis()))
                .build());
    }


}

