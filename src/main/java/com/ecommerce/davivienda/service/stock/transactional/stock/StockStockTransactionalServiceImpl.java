package com.ecommerce.davivienda.service.stock.transactional.stock;

import com.ecommerce.davivienda.entity.product.Stock;
import com.ecommerce.davivienda.repository.product.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Implementación del servicio transaccional para operaciones de stock.
 * Centraliza todas las operaciones de acceso a datos de inventario.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StockStockTransactionalServiceImpl implements StockStockTransactionalService {

    private final StockRepository stockRepository;

    @Override
    @Transactional
    public void createOrUpdateStock(Integer productoId, Integer cantidad) {
        log.debug("Creando/Actualizando stock para producto ID: {}, cantidad: {}", productoId, cantidad);

        stockRepository.findByProductoId(productoId)
                .ifPresentOrElse(
                        existingStock -> {
                            log.debug("Stock existente encontrado. Actualizando cantidad de {} a {}",
                                    existingStock.getCantidad(), cantidad);
                            existingStock.setCantidad(cantidad);
                            stockRepository.save(existingStock);
                        },
                        () -> {
                            log.debug("Creando nuevo registro de stock para producto ID: {}", productoId);
                            Stock newStock = Stock.builder()
                                    .productoId(productoId)
                                    .cantidad(cantidad)
                                    .build();
                            stockRepository.save(newStock);
                        }
                );
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Stock> findByProductoId(Integer productoId) {
        log.debug("Buscando stock para producto ID: {}", productoId);
        return stockRepository.findByProductoId(productoId);
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

