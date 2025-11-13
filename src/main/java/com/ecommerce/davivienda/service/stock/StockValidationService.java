package com.ecommerce.davivienda.service.stock;

import com.ecommerce.davivienda.dto.stock.StockValidationRequestDto;
import com.ecommerce.davivienda.dto.stock.StockValidationResponseDto;

/**
 * Servicio para validación de stock de productos en el carrito.
 * Verifica que haya inventario suficiente para todos los productos del carrito.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface StockValidationService {

    /**
     * Valida que todos los productos del carrito de un usuario tengan stock suficiente.
     * 
     * <p>Proceso:</p>
     * <ol>
     *   <li>Busca al usuario por documento (tipo y número)</li>
     *   <li>Obtiene el carrito del usuario</li>
     *   <li>Valida cada producto del carrito contra el stock disponible</li>
     *   <li>Si algún producto no tiene stock suficiente, lanza InsufficientStockException</li>
     *   <li>Si todos los productos tienen stock, retorna respuesta exitosa</li>
     * </ol>
     *
     * @param request Datos del usuario (documentType y documentNumber)
     * @return Respuesta con indicador de disponibilidad y productos con problemas (si los hay)
     * @throws com.ecommerce.davivienda.exception.stock.InsufficientStockException si hay productos sin stock suficiente
     * @throws com.ecommerce.davivienda.exception.stock.StockException si hay errores de validación
     * @throws com.ecommerce.davivienda.exception.CartException si no se encuentra el carrito
     */
    StockValidationResponseDto validateCartStock(StockValidationRequestDto request);
}

