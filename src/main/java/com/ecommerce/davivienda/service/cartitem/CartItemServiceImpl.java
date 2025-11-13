package com.ecommerce.davivienda.service.cartitem;

import com.ecommerce.davivienda.dto.cart.summary.CartSummaryDto;
import com.ecommerce.davivienda.entity.cart.Cart;
import com.ecommerce.davivienda.models.cart.CartItemRequest;
import com.ecommerce.davivienda.entity.cart.CartItem;
import com.ecommerce.davivienda.entity.product.Product;
import com.ecommerce.davivienda.mapper.cart.CartItemMapper;
import com.ecommerce.davivienda.models.Response;
import com.ecommerce.davivienda.service.auth.AuthUserService;
import com.ecommerce.davivienda.service.cartitem.transactional.cart.CartItemCartTransactionalService;
import com.ecommerce.davivienda.service.cartitem.validation.cart.CartItemCartValidationService;
import com.ecommerce.davivienda.service.cartitem.validation.common.CartItemCommonValidationService;
import com.ecommerce.davivienda.service.cartitem.validation.product.CartItemProductValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.ecommerce.davivienda.constants.Constants.*;

/**
 * Implementación del servicio para gestión de items del carrito.
 * Coordina validaciones (product, cart, common), mapeo y persistencia de items con cálculos.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final CartItemCartTransactionalService transactionalService;
    private final CartItemCartValidationService cartValidationService;
    private final CartItemProductValidationService productValidationService;
    private final CartItemCommonValidationService commonValidationService;
    private final CartItemMapper cartItemMapper;
    private final AuthUserService authUserService;

    @Override
    @Transactional
    public Response<String> addItemToCart(CartItemRequest request) {
        log.info("Iniciando proceso para agregar producto {} al carrito", request.getProductId());
        
        Integer userRoleId = authUserService.getAuthenticatedUserRoleId();
        Cart cart = transactionalService.findOrCreateCart(userRoleId);
        
        Product product = productValidationService.validateProductExists(request.getProductId());
        productValidationService.validateProductActive(product);
        commonValidationService.validateQuantity(request.getQuantity());
        
        CartItem cartItem = createOrUpdateCartItem(request, cart, product);
        transactionalService.saveCartItem(cartItem);
        
        return Response.success(SUCCESS_CART_ITEM_ADDED);
    }

    @Override
    @Transactional
    public Response<String> removeItemFromCart(Integer itemId) {
        log.info("Eliminando item {} del carrito", itemId);
        
        Integer userRoleId = authUserService.getAuthenticatedUserRoleId();
        
       CartItem cartItem = cartValidationService.validateItemBelongsToUser(itemId, userRoleId);
        
        transactionalService.deleteCartItem(cartItem);
        
        log.info("Item {} eliminado exitosamente", itemId);
        
        return Response.success(SUCCESS_CART_ITEM_DELETED);
    }


    @Override
    @Transactional(readOnly = true)
    public CartSummaryDto getCartSummary() {
        log.info("Obteniendo resumen del carrito del usuario autenticado");
        
        Integer userRoleId = authUserService.getAuthenticatedUserRoleId();
        Cart cart = transactionalService.findOrCreateCart(userRoleId);
        
        List<CartItem> items = transactionalService.findCartItemsByCartId(cart.getCarritoId());
        
        log.info("Generando resumen del carrito {} con {} items para usuario {}", 
                cart.getCarritoId(), items.size(), userRoleId);
        
        return cartItemMapper.toCartSummaryDto(items);
    }

    /**
     * Crea un nuevo CartItem o actualiza uno existente.
     * Si el producto ya existe en el carrito, actualiza su cantidad.
     * Si no existe, crea un nuevo item.
     *
     * @param request DTO con datos del item
     * @param cart Carrito al que pertenece el item
     * @param product Producto a agregar
     * @return CartItem creado o actualizado
     */
    private CartItem createOrUpdateCartItem(CartItemRequest request, Cart cart, Product product) {
        Optional<CartItem> existingItem = transactionalService
                .findCartItemByCartAndProduct(cart.getCarritoId(), request.getProductId());
        
        if (existingItem.isPresent()) {
            CartItem cartItem = existingItem.get();
            cartItemMapper.updateQuantity(cartItem, request.getQuantity());
            log.debug("Producto {} actualizado a {} unidades en carrito {}", 
                    request.getProductId(), request.getQuantity(), cart.getCarritoId());
            return cartItem;
        } else {
            CartItem cartItem = cartItemMapper.toEntity(request, cart, product);
            log.debug("Producto {} creado con {} unidades en carrito {}", 
                    request.getProductId(), request.getQuantity(), cart.getCarritoId());
            return cartItem;
        }
    }
}

