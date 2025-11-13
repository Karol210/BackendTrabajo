package com.ecommerce.davivienda.service.cartitem;

import com.ecommerce.davivienda.dto.cart.summary.CartSummaryDto;
import com.ecommerce.davivienda.models.Response;
import com.ecommerce.davivienda.models.cart.CartItemRequest;


/**
 * Servicio para gestión de items del carrito de compras.
 * Proporciona operaciones CRUD con cálculos de precios e IVA.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface CartItemService {

    /**
     * Agrega un producto al carrito.
     * Si el producto ya existe, actualiza la cantidad con el nuevo valor.
     * Si el carrito no existe, lo crea automáticamente.
     *
     * @param request DTO con datos del item a agregar
     * @return Response con mensaje de éxito
     */
    Response<String> addItemToCart(CartItemRequest request);


    /**
     * Elimina un item del carrito.
     *
     * @param itemId ID del item a eliminar
     * @return Response con mensaje de éxito
     */
    Response<String> removeItemFromCart(Integer itemId);

    /**
     * Obtiene un resumen completo del carrito del usuario autenticado con totales agregados.
     * El usuario se obtiene automáticamente del token JWT.
     *
     * @return DTO con resumen del carrito
     */
    CartSummaryDto getCartSummary();



}

