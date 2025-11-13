package com.ecommerce.davivienda.service.stock;

import com.ecommerce.davivienda.dto.stock.ProductStockDetailDto;
import com.ecommerce.davivienda.dto.stock.StockValidationResponseDto;
import com.ecommerce.davivienda.entity.cart.Cart;
import com.ecommerce.davivienda.entity.cart.CartItem;
import com.ecommerce.davivienda.entity.product.Stock;
import com.ecommerce.davivienda.mapper.stock.StockMapper;
import com.ecommerce.davivienda.service.auth.AuthUserService;
import com.ecommerce.davivienda.service.stock.transactional.cart.StockCartTransactionalService;
import com.ecommerce.davivienda.service.stock.transactional.stock.StockStockTransactionalService;
import com.ecommerce.davivienda.service.stock.validation.StockCartValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementación del servicio de stock/inventario.
 * Coordina operaciones CRUD y validación de inventario delegando a capacidades.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final StockStockTransactionalService stockTransactionalService;
    private final StockCartTransactionalService cartTransactionalService;
    private final StockCartValidationService cartValidationService;
    private final StockMapper stockMapper;
    private final AuthUserService authUserService;

    @Override
    @Transactional
    public void createOrUpdateStock(Integer productoId, Integer cantidad) {
        log.info("Creando/Actualizando stock para producto ID: {}, cantidad: {}", productoId, cantidad);
        stockTransactionalService.createOrUpdateStock(productoId, cantidad);
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
        return stockTransactionalService.getCurrentStock(productoId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasEnoughStock(Integer productoId, Integer requestedQuantity) {
        return stockTransactionalService.hasEnoughStock(productoId, requestedQuantity);
    }

    @Override
    @Transactional(readOnly = true)
    public StockValidationResponseDto validateCartStock() {
        log.info("Iniciando validación de stock para usuario autenticado");

        Integer userRoleId = authUserService.getAuthenticatedUserRoleId();
        log.debug("UserRoleId obtenido del token: {}", userRoleId);

        Cart cart = cartTransactionalService.getCartByUserRoleId(userRoleId);

        List<CartItem> cartItems = cartTransactionalService.getCartItems(cart.getCarritoId());

        cartValidationService.validateCartHasItems(cartItems);

        List<ProductStockDetailDto> insufficientStockProducts = checkStockAvailability(cartItems);

        if (!insufficientStockProducts.isEmpty()) {
            log.warn("Stock insuficiente para {} productos del carrito",
                    insufficientStockProducts.size());

            return stockMapper.buildInsufficientStockResponse(
                    cartItems.size(),
                    insufficientStockProducts
            );
        }

        log.info("Stock suficiente para todos los productos del carrito");
        return stockMapper.buildSuccessResponse(cartItems.size());
    }

    /**
     * Verifica la disponibilidad de stock para cada producto del carrito.
     *
     * @param cartItems Items del carrito a validar
     * @return Lista de productos con stock insuficiente (vacía si todos tienen stock)
     */
    private List<ProductStockDetailDto> checkStockAvailability(List<CartItem> cartItems) {
        log.debug("Verificando stock para {} productos", cartItems.size());

        List<ProductStockDetailDto> insufficientStockProducts = new ArrayList<>();

        for (CartItem item : cartItems) {
            validateSingleItemStock(item, insufficientStockProducts);
        }

        return insufficientStockProducts;
    }

    /**
     * Valida el stock de un solo item del carrito.
     *
     * @param item Item del carrito a validar
     * @param insufficientStockProducts Lista donde se agregan productos con stock insuficiente
     */
    private void validateSingleItemStock(CartItem item, List<ProductStockDetailDto> insufficientStockProducts) {
        Integer productId = item.getProduct().getProductoId();
        Integer requestedQuantity = item.getCantidad();
        String productName = item.getProduct().getNombre();

        log.debug("Validando stock para producto: {} (ID: {}), cantidad solicitada: {}",
                productName, productId, requestedQuantity);

        Stock stock = stockTransactionalService.findByProductoId(productId).orElse(null);

        if (stock == null) {
            handleMissingStock(productId, productName, requestedQuantity, insufficientStockProducts);
            return;
        }

        if (!stock.hasEnoughStock(requestedQuantity)) {
            handleInsufficientStock(productId, productName, requestedQuantity, stock, insufficientStockProducts);
            return;
        }

        log.debug("Stock suficiente para producto: {} (ID: {})", productName, productId);
    }

    /**
     * Maneja el caso cuando no existe registro de stock para un producto.
     *
     * @param productId ID del producto
     * @param productName Nombre del producto
     * @param requestedQuantity Cantidad solicitada
     * @param insufficientStockProducts Lista donde se agrega el producto
     */
    private void handleMissingStock(Integer productId, String productName, Integer requestedQuantity,
                                     List<ProductStockDetailDto> insufficientStockProducts) {
        log.warn("No existe registro de stock para producto: {} (ID: {})", productName, productId);

        insufficientStockProducts.add(stockMapper.buildProductStockDetail(
                productId, productName, requestedQuantity, 0
        ));
    }

    /**
     * Maneja el caso cuando hay stock insuficiente para un producto.
     *
     * @param productId ID del producto
     * @param productName Nombre del producto
     * @param requestedQuantity Cantidad solicitada
     * @param stock Registro de stock actual
     * @param insufficientStockProducts Lista donde se agrega el producto
     */
    private void handleInsufficientStock(Integer productId, String productName, Integer requestedQuantity,
                                          Stock stock, List<ProductStockDetailDto> insufficientStockProducts) {
        Integer availableQuantity = stock.getCantidad() != null ? stock.getCantidad() : 0;

        log.warn("Stock insuficiente para producto: {} (ID: {}). Solicitado: {}, Disponible: {}",
                productName, productId, requestedQuantity, availableQuantity);

        insufficientStockProducts.add(stockMapper.buildProductStockDetail(
                productId, productName, requestedQuantity, availableQuantity
        ));
    }
}


