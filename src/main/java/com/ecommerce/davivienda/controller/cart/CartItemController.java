package com.ecommerce.davivienda.controller.cart;

import com.ecommerce.davivienda.dto.cart.CartItemBatchRequestDto;
import com.ecommerce.davivienda.dto.cart.CartItemRequestDto;
import com.ecommerce.davivienda.dto.cart.CartItemResponseDto;
import com.ecommerce.davivienda.dto.cart.CartSummaryDto;
import com.ecommerce.davivienda.models.Response;
import com.ecommerce.davivienda.service.cart.CartItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     * Agrega un producto al carrito.
     * Si el producto ya existe, reemplaza la cantidad con el nuevo valor.
     * Endpoint: POST /api/v1/cart-items/add
     *
     * @param request DTO con datos del item a agregar
     * @return Response con mensaje de confirmación
     */
    @PostMapping("/add")
    public ResponseEntity<Response<Void>> addItemToCart(
            @Valid @RequestBody CartItemRequestDto request) {
        
        log.info("Request para agregar producto {} al carrito (usuario: {} {})", 
                request.getProductId(), request.getDocumentType(), request.getDocumentNumber());
        
        cartItemService.addItemToCart(request);
        
        log.info("Producto {} agregado exitosamente para usuario {} {}", 
                request.getProductId(), request.getDocumentType(), request.getDocumentNumber());
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Response.<Void>builder()
                        .failure(false)
                        .code(HttpStatus.CREATED.value())
                        .message(SUCCESS_CART_ITEM_ADDED)
                        .timestamp(String.valueOf(System.currentTimeMillis()))
                        .build());
    }

    /**
     * Agrega múltiples productos al carrito de una sola vez.
     * Endpoint: POST /api/v1/cart-items/add-batch
     *
     * @param request DTO con lista de items a agregar
     * @return Response con items agregados y cálculos
     */
    @PostMapping("/add-batch")
    public ResponseEntity<Response<List<CartItemResponseDto>>> addItemsToCartBatch(
            @Valid @RequestBody CartItemBatchRequestDto request) {
        
        log.info("Request para agregar {} productos al carrito {} en lote", 
                request.getItems().size(), request.getCartId());
        
        List<CartItemResponseDto> cartItems = cartItemService.addItemsToCartBatch(request);
        
        log.info("{} de {} productos agregados exitosamente", 
                cartItems.size(), request.getItems().size());
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Response.<List<CartItemResponseDto>>builder()
                        .failure(false)
                        .code(HttpStatus.CREATED.value())
                        .message(String.format(SUCCESS_CART_ITEMS_BATCH_ADDED + " (%d de %d)", 
                                cartItems.size(), request.getItems().size()))
                        .body(cartItems)
                        .timestamp(String.valueOf(System.currentTimeMillis()))
                        .build());
    }

    /**
     * Actualiza la cantidad de un item del carrito.
     * Endpoint: PUT /api/v1/cart-items/{id}/quantity
     *
     * @param id ID del item a actualizar
     * @param quantity Nueva cantidad (query param)
     * @return Response con item actualizado y nuevos cálculos
     */
    @PutMapping("/{id}/quantity")
    public ResponseEntity<Response<CartItemResponseDto>> updateItemQuantity(
            @PathVariable("id") Integer id,
            @RequestParam("quantity") Integer quantity) {
        
        log.info("Request para actualizar cantidad del item {} a {}", id, quantity);
        
        CartItemResponseDto cartItem = cartItemService.updateItemQuantity(id, quantity);
        
        log.info("Cantidad del item actualizada exitosamente");
        
        return ResponseEntity.ok(Response.<CartItemResponseDto>builder()
                .failure(false)
                .code(HttpStatus.OK.value())
                .message(SUCCESS_CART_ITEM_UPDATED)
                .body(cartItem)
                .timestamp(String.valueOf(System.currentTimeMillis()))
                .build());
    }

    /**
     * Elimina un item del carrito.
     * Endpoint: DELETE /api/v1/cart-items/{id}
     *
     * @param id ID del item a eliminar
     * @return Response confirmando eliminación
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Response<Void>> removeItemFromCart(
            @PathVariable("id") Integer id) {
        
        log.info("Request para eliminar item {} del carrito", id);
        
        cartItemService.removeItemFromCart(id);
        
        log.info("Item eliminado exitosamente del carrito");
        
        return ResponseEntity.ok(Response.<Void>builder()
                .failure(false)
                .code(HttpStatus.OK.value())
                .message(SUCCESS_CART_ITEM_DELETED)
                .timestamp(String.valueOf(System.currentTimeMillis()))
                .build());
    }

    /**
     * Obtiene todos los items de un carrito con cálculos.
     * Endpoint: GET /api/v1/cart-items/cart/{cartId}
     *
     * @param cartId ID del carrito
     * @return Response con lista de items y cálculos
     */
    @GetMapping("/cart/{cartId}")
    public ResponseEntity<Response<List<CartItemResponseDto>>> getCartItems(
            @PathVariable("cartId") Integer cartId) {
        
        log.info("Request para obtener items del carrito {}", cartId);
        
        List<CartItemResponseDto> items = cartItemService.getCartItems(cartId);
        
        log.info("Se encontraron {} items en el carrito", items.size());
        
        return ResponseEntity.ok(Response.<List<CartItemResponseDto>>builder()
                .failure(false)
                .code(HttpStatus.OK.value())
                .message(SUCCESS_CART_ITEMS_FOUND)
                .body(items)
                .timestamp(String.valueOf(System.currentTimeMillis()))
                .build());
    }

    /**
     * Obtiene un resumen completo del carrito con totales agregados.
     * Incluye: lista de items, subtotal, IVA total y precio total.
     * Endpoint: GET /api/v1/cart-items/cart/{cartId}/summary
     *
     * @param cartId ID del carrito
     * @return Response con resumen del carrito
     */
    @GetMapping("/cart/{cartId}/summary")
    public ResponseEntity<Response<CartSummaryDto>> getCartSummary(
            @PathVariable("cartId") Integer cartId) {
        
        log.info("Request para obtener resumen del carrito {}", cartId);
        
        CartSummaryDto summary = cartItemService.getCartSummary(cartId);
        
        log.info("Resumen del carrito obtenido: {} items, total: {}", 
                summary.getTotalItems(), summary.getTotalPrice());
        
        return ResponseEntity.ok(Response.<CartSummaryDto>builder()
                .failure(false)
                .code(HttpStatus.OK.value())
                .message(SUCCESS_CART_ITEMS_FOUND)
                .body(summary)
                .timestamp(String.valueOf(System.currentTimeMillis()))
                .build());
    }

    /**
     * Limpia todos los items de un carrito.
     * Endpoint: DELETE /api/v1/cart-items/cart/{cartId}/clear
     *
     * @param cartId ID del carrito a limpiar
     * @return Response confirmando limpieza
     */
    @DeleteMapping("/cart/{cartId}/clear")
    public ResponseEntity<Response<Void>> clearCart(
            @PathVariable("cartId") Integer cartId) {
        
        log.info("Request para limpiar carrito {}", cartId);
        
        cartItemService.clearCart(cartId);
        
        log.info("Carrito limpiado exitosamente");
        
        return ResponseEntity.ok(Response.<Void>builder()
                .failure(false)
                .code(HttpStatus.OK.value())
                .message(SUCCESS_CART_CLEARED)
                .timestamp(String.valueOf(System.currentTimeMillis()))
                .build());
    }

    /**
     * Obtiene un item específico del carrito por su ID.
     * Endpoint: GET /api/v1/cart-items/{id}
     *
     * @param id ID del item
     * @return Response con item y cálculos
     */
    @GetMapping("/{id}")
    public ResponseEntity<Response<CartItemResponseDto>> getCartItemById(
            @PathVariable("id") Integer id) {
        
        log.info("Request para obtener item del carrito con ID: {}", id);
        
        CartItemResponseDto cartItem = cartItemService.getCartItemById(id);
        
        log.info("Item del carrito obtenido exitosamente");
        
        return ResponseEntity.ok(Response.<CartItemResponseDto>builder()
                .failure(false)
                .code(HttpStatus.OK.value())
                .message(SUCCESS_CART_ITEMS_FOUND)
                .body(cartItem)
                .timestamp(String.valueOf(System.currentTimeMillis()))
                .build());
    }
}

