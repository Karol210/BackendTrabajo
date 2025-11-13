package com.ecommerce.davivienda.service.stock;

/**
 * Servicio para gestionar operaciones de stock/inventario.
 * Proporciona métodos para crear, actualizar y consultar inventario de productos.
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
}

