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

}

