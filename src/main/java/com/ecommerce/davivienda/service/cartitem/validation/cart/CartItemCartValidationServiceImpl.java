package com.ecommerce.davivienda.service.cartitem.validation.cart;

import com.ecommerce.davivienda.entity.cart.Cart;
import com.ecommerce.davivienda.entity.cart.CartItem;
import com.ecommerce.davivienda.exception.cart.CartException;
import com.ecommerce.davivienda.service.cart.autocreate.CartAutoCreateService;
import com.ecommerce.davivienda.service.cartitem.transactional.cart.CartItemCartTransactionalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.ecommerce.davivienda.constants.Constants.*;

/**
 * Implementación del servicio de validación de carritos para items del carrito.
 * Aplica reglas de negocio relacionadas con la validez y ownership de carritos.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CartItemCartValidationServiceImpl implements CartItemCartValidationService {

    private final CartItemCartTransactionalService transactionalService;
    private final CartAutoCreateService autoCreateService;

    @Override
    public Cart validateCartExistsAndBelongsToUser(Integer cartId, Integer userRoleId) {
        log.debug("Validando existencia del carrito {} y ownership del usuario {}", cartId, userRoleId);
        
        if (cartId == null) {
            throw new CartException(ERROR_CART_NOT_FOUND, CODE_CART_NOT_FOUND);
        }
        
        if (userRoleId == null) {
            throw new CartException(ERROR_USER_ROLE_NOT_FOUND, CODE_USER_ROLE_NOT_FOUND);
        }
        
        return transactionalService.findCartByIdAndUser(cartId, userRoleId)
                .orElseThrow(() -> new CartException(ERROR_CART_UNAUTHORIZED, CODE_CART_UNAUTHORIZED));
    }
    
    @Override
    public Cart validateOrCreateCart(Integer cartId, Integer userRoleId) {
        log.debug("Validando o creando carrito con ID: {} y userRoleId: {}", cartId, userRoleId);
        return autoCreateService.getOrCreateCart(cartId, userRoleId);
    }

    @Override
    public void validateProductNotInCart(Integer cartId, Integer productId) {
        if (transactionalService.existsCartItemByCartAndProduct(cartId, productId)) {
            log.warn("El producto {} ya existe en el carrito {}", productId, cartId);
            throw new CartException(ERROR_CART_ITEM_ALREADY_EXISTS, CODE_CART_ITEM_ALREADY_EXISTS);
        }
    }
    
    @Override
    public CartItem validateItemBelongsToUser(Integer itemId, Integer userRoleId) {
        log.debug("Validando que el item {} pertenece al carrito del usuario {}", itemId, userRoleId);
        
        if (itemId == null) {
            throw new CartException(ERROR_CART_ITEM_NOT_FOUND, CODE_CART_ITEM_NOT_FOUND);
        }
        
        if (userRoleId == null) {
            throw new CartException(ERROR_USER_ROLE_NOT_FOUND, CODE_USER_ROLE_NOT_FOUND);
        }
        
        // Buscar item validando ownership en una sola query
       return transactionalService.findCartItemByIdAndUser(itemId, userRoleId)
                .orElseThrow(() -> {
                    log.warn("El item {} no existe o no pertenece al carrito del usuario {}", itemId, userRoleId);
                    return new CartException(ERROR_CART_ITEM_UNAUTHORIZED, CODE_CART_ITEM_UNAUTHORIZED);
                });
        
    }

    @Override
    public void validateProductBelongsToUser(Integer productId, Integer userRoleId) {
        log.debug("Validando que el producto {} pertenece al carrito del usuario {}", productId, userRoleId);
        
        if (productId == null) {
            throw new CartException(ERROR_CART_ITEM_NOT_FOUND, CODE_CART_ITEM_NOT_FOUND);
        }
        
        if (userRoleId == null) {
            throw new CartException(ERROR_USER_ROLE_NOT_FOUND, CODE_USER_ROLE_NOT_FOUND);
        }
        
        transactionalService.findCartItemByProductAndUser(productId, userRoleId)
                .orElseThrow(() -> {
                    log.warn("El producto {} no existe en el carrito del usuario {}", productId, userRoleId);
                    return new CartException(ERROR_CART_ITEM_UNAUTHORIZED, CODE_CART_ITEM_UNAUTHORIZED);
                });
        
        log.info("Validación exitosa: Producto {} pertenece al carrito del usuario {}", productId, userRoleId);
    }
}

