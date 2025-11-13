package com.ecommerce.davivienda.service.stock;

import com.ecommerce.davivienda.dto.stock.ProductStockDetailDto;
import com.ecommerce.davivienda.dto.stock.StockValidationRequestDto;
import com.ecommerce.davivienda.dto.stock.StockValidationResponseDto;
import com.ecommerce.davivienda.entity.cart.Cart;
import com.ecommerce.davivienda.entity.cart.CartItem;
import com.ecommerce.davivienda.entity.product.Stock;
import com.ecommerce.davivienda.exception.cart.CartException;
import com.ecommerce.davivienda.exception.stock.InsufficientStockException;
import com.ecommerce.davivienda.exception.stock.StockException;
import com.ecommerce.davivienda.repository.cart.CartItemRepository;
import com.ecommerce.davivienda.repository.cart.CartRepository;
import com.ecommerce.davivienda.repository.product.StockRepository;
import com.ecommerce.davivienda.service.cartitem.validation.user.CartItemUserValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.ecommerce.davivienda.constants.Constants.*;

/**
 * Implementación del servicio de validación de stock.
 * Valida que haya inventario suficiente para todos los productos del carrito.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StockValidationServiceImpl implements StockValidationService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final StockRepository stockRepository;
    private final CartItemUserValidationService userValidationService;

    @Override
    @Transactional(readOnly = true)
    public StockValidationResponseDto validateCartStock(StockValidationRequestDto request) {
        log.info("Iniciando validación de stock para usuario: {} {}", 
                request.getDocumentType(), request.getDocumentNumber());

        try {
            Integer userRoleId = getUserRoleIdFromDocument(request);
            
            Cart cart = getCartByUserRoleId(userRoleId);
            
            List<CartItem> cartItems = getCartItems(cart.getCarritoId());
            
            validateCartHasItems(cartItems);
            
            List<ProductStockDetailDto> insufficientStockProducts = checkStockAvailability(cartItems);
            
            if (!insufficientStockProducts.isEmpty()) {
                log.warn("Stock insuficiente para {} productos del carrito", 
                        insufficientStockProducts.size());
                
                throw new InsufficientStockException(
                    ERROR_INSUFFICIENT_STOCK,
                    CODE_INSUFFICIENT_STOCK,
                    insufficientStockProducts
                );
            }
            
            log.info("Validación exitosa: todos los productos tienen stock suficiente");
            
            return buildSuccessResponse(cartItems.size());

        } catch (InsufficientStockException | CartException | StockException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado durante validación de stock: {}", e.getMessage(), e);
            throw new StockException(
                "Error al validar stock del carrito: " + e.getMessage(),
                CODE_GENERIC_ERROR,
                e
            );
        }
    }

    /**
     * Obtiene el userRoleId del usuario mediante su documento.
     */
    private Integer getUserRoleIdFromDocument(StockValidationRequestDto request) {
        log.debug("Obteniendo userRoleId para documento: {} {}", 
                request.getDocumentType(), request.getDocumentNumber());
        
        return userValidationService.getUserRoleIdFromDocument(
                request.getDocumentType(), 
                request.getDocumentNumber()
        );
    }

    /**
     * Busca el carrito del usuario por userRoleId.
     */
    private Cart getCartByUserRoleId(Integer userRoleId) {
        log.debug("Buscando carrito para userRoleId: {}", userRoleId);
        
        return cartRepository.findByUsuarioRolId(userRoleId)
                .orElseThrow(() -> {
                    log.warn("Carrito no encontrado para userRoleId: {}", userRoleId);
                    return new CartException(ERROR_USER_CART_NOT_FOUND, CODE_USER_CART_NOT_FOUND);
                });
    }

    /**
     * Obtiene todos los items del carrito.
     */
    private List<CartItem> getCartItems(Integer cartId) {
        log.debug("Obteniendo items del carrito: {}", cartId);
        
        return cartItemRepository.findByCartCarritoId(cartId);
    }

    /**
     * Valida que el carrito tenga al menos un producto.
     */
    private void validateCartHasItems(List<CartItem> cartItems) {
        if (cartItems == null || cartItems.isEmpty()) {
            log.warn("Carrito sin productos para validar");
            throw new StockException(ERROR_CART_NO_ITEMS, CODE_CART_NO_ITEMS);
        }
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
            Integer productId = item.getProduct().getProductoId();
            Integer requestedQuantity = item.getCantidad();
            String productName = item.getProduct().getNombre();

            log.debug("Validando stock para producto: {} (ID: {}), cantidad solicitada: {}", 
                    productName, productId, requestedQuantity);

            Stock stock = stockRepository.findByProductoId(productId)
                    .orElse(null);

            if (stock == null) {
                log.warn("No existe registro de stock para producto: {} (ID: {})", 
                        productName, productId);
                
                insufficientStockProducts.add(buildProductStockDetail(
                        productId, productName, requestedQuantity, 0
                ));
                continue;
            }

            if (!stock.hasEnoughStock(requestedQuantity)) {
                Integer availableQuantity = stock.getCantidad() != null ? stock.getCantidad() : 0;
                
                log.warn("Stock insuficiente para producto: {} (ID: {}). Solicitado: {}, Disponible: {}", 
                        productName, productId, requestedQuantity, availableQuantity);
                
                insufficientStockProducts.add(buildProductStockDetail(
                        productId, productName, requestedQuantity, availableQuantity
                ));
            } else {
                log.debug("Stock suficiente para producto: {} (ID: {})", productName, productId);
            }
        }

        return insufficientStockProducts;
    }

    /**
     * Construye el DTO con detalles de un producto con stock insuficiente.
     */
    private ProductStockDetailDto buildProductStockDetail(Integer productId, String productName,
                                                          Integer requestedQuantity, Integer availableQuantity) {
        Integer missingQuantity = Math.max(0, requestedQuantity - availableQuantity);
        
        return ProductStockDetailDto.builder()
                .productId(productId)
                .productName(productName)
                .requestedQuantity(requestedQuantity)
                .availableQuantity(availableQuantity)
                .missingQuantity(missingQuantity)
                .build();
    }

    /**
     * Construye la respuesta exitosa de validación de stock.
     */
    private StockValidationResponseDto buildSuccessResponse(Integer totalProducts) {
        return StockValidationResponseDto.builder()
                .available(true)
                .message(SUCCESS_STOCK_AVAILABLE)
                .insufficientStockProducts(new ArrayList<>())
                .totalProductsInCart(totalProducts)
                .productsWithIssues(0)
                .build();
    }
}

