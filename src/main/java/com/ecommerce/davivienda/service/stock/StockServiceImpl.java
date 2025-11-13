package com.ecommerce.davivienda.service.stock;

import com.ecommerce.davivienda.entity.product.Stock;
import com.ecommerce.davivienda.repository.product.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del servicio de stock/inventario.
 * Gestiona las operaciones CRUD sobre el inventario de productos.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;

    @Override
    @Transactional
    public void createOrUpdateStock(Integer productoId, Integer cantidad) {
        log.info("Creando/Actualizando stock para producto ID: {}, cantidad: {}", productoId, cantidad);

        stockRepository.findByProductoId(productoId)
                .ifPresentOrElse(
                        existingStock -> {
                            log.info("Stock existente encontrado. Actualizando cantidad de {} a {}",
                                    existingStock.getCantidad(), cantidad);
                            existingStock.setCantidad(cantidad);
                            stockRepository.save(existingStock);
                        },
                        () -> {
                            log.info("Creando nuevo registro de stock para producto ID: {}", productoId);
                            Stock newStock = Stock.builder()
                                    .productoId(productoId)
                                    .cantidad(cantidad)
                                    .build();
                            stockRepository.save(newStock);
                        }
                );

        log.info("Stock actualizado exitosamente para producto ID: {}", productoId);
    }

    @Override
    @Transactional
    public void updateStock(Integer productoId, Integer newQuantity) {
        log.info("Actualizando stock para producto ID: {} a cantidad: {}", productoId, newQuantity);
        createOrUpdateStock(productoId, newQuantity);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getCurrentStock(Integer productoId) {
        log.debug("Consultando stock actual para producto ID: {}", productoId);
        return stockRepository.findByProductoId(productoId)
                .map(Stock::getCantidad)
                .orElse(0);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasEnoughStock(Integer productoId, Integer requestedQuantity) {
        log.debug("Verificando disponibilidad de stock para producto ID: {}, cantidad solicitada: {}",
                productoId, requestedQuantity);

        return stockRepository.findByProductoId(productoId)
                .map(stock -> stock.hasEnoughStock(requestedQuantity))
                .orElse(false);
    }
}

