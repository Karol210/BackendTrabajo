package com.ecommerce.davivienda.controller.cart;

import com.ecommerce.davivienda.models.Response;
import com.ecommerce.davivienda.service.cart.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.ecommerce.davivienda.constants.Constants.SUCCESS_CART_CREATED;

/**
 * Controlador REST para gestión de carritos de compras.
 * Proporciona endpoints para crear y gestionar carritos de usuarios autenticados.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    /**
     * Crea un carrito para el usuario autenticado.
     * Extrae el email del usuario desde el token JWT, valida su existencia,
     * obtiene su rol principal y crea el carrito asociado.
     * 
     * Endpoint: POST /api/v1/carts/create
     *
     * @return Response simple con mensaje de éxito
     */
    @PostMapping("/create")
    public ResponseEntity<Response<String>> createCart() {
        
        log.info("Request para crear carrito de usuario autenticado");
        
        cartService.createCart();
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Response.<String>builder()
                        .failure(false)
                        .code(HttpStatus.CREATED.value())
                        .message(SUCCESS_CART_CREATED)
                        .timestamp(String.valueOf(System.currentTimeMillis()))
                        .build());
    }
}

