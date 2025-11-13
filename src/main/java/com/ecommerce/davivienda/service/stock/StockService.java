package com.ecommerce.davivienda.service.stock;

import com.ecommerce.davivienda.dto.stock.StockValidationResponseDto;

/**
 * Servicio para gestionar operaciones de stock/inventario.
 * Proporciona métodos para crear, actualizar, consultar y validar inventario de productos.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface StockService {

    /**
     * Crea un registro de stock inicial para un producto.
     * Si ya existe, actualiza la cantidad.
     *
     * @param productoId ID del producto
     * @param cantidad Cantidad inicial de inventario
     */
    void createOrUpdateStock(Integer productoId, Integer cantidad);

    /**
     * Actualiza la cantidad de stock de un producto.
     * Si no existe el registro, lo crea.
     *
     * @param productoId ID del producto
     * @param newQuantity Nueva cantidad de inventario
     */
    void updateStock(Integer productoId, Integer newQuantity);

    /**
     * Obtiene la cantidad actual de stock de un producto.
     *
     * @param productoId ID del producto
     * @return Cantidad actual de stock, 0 si no existe
     */
    Integer getCurrentStock(Integer productoId);

    /**
     * Verifica si un producto tiene suficiente stock.
     *
     * @param productoId ID del producto
     * @param requestedQuantity Cantidad solicitada
     * @return true si hay suficiente stock
     */
    boolean hasEnoughStock(Integer productoId, Integer requestedQuantity);

    /**
     * Valida que todos los productos del carrito del usuario autenticado tengan stock suficiente.
     * Usa el token JWT para identificar al usuario y obtener su carrito.
     * 
     * <p>Proceso:</p>
     * <ol>
     *   <li>Extrae el userRoleId del token JWT del usuario autenticado</li>
     *   <li>Obtiene el carrito del usuario</li>
     *   <li>Valida cada producto del carrito contra el stock disponible</li>
     *   <li>Retorna respuesta con available=true si hay stock suficiente</li>
     *   <li>Retorna respuesta con available=false y lista de productos faltantes si no hay stock</li>
     * </ol>
     *
     * @return Respuesta con indicador de disponibilidad y productos con problemas (si los hay)
     * @throws com.ecommerce.davivienda.exception.cart.CartException si no se encuentra el carrito
     * @throws com.ecommerce.davivienda.exception.cart.CartException si el carrito está vacío
     */
    StockValidationResponseDto validateCartStock();
}

