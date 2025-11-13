package com.ecommerce.davivienda.service.stock.transactional.stock;

import com.ecommerce.davivienda.entity.product.Stock;

import java.util.Optional;

/**
 * Servicio transaccional para operaciones de consulta y persistencia de Stock.
 * Maneja todas las operaciones de acceso a datos de stock/inventario.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface StockStockTransactionalService {

    /**
     * Crea o actualiza un registro de stock para un producto.
     *
     * @param productoId ID del producto
     * @param cantidad Cantidad de stock
     */
    void createOrUpdateStock(Integer productoId, Integer cantidad);

    /**
     * Busca el stock de un producto por ID.
     *
     * @param productoId ID del producto
     * @return Optional con el Stock si existe
     */
    Optional<Stock> findByProductoId(Integer productoId);

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

