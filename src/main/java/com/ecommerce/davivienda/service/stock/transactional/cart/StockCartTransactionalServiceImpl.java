package com.ecommerce.davivienda.service.stock.transactional.cart;

import com.ecommerce.davivienda.entity.cart.Cart;
import com.ecommerce.davivienda.entity.cart.CartItem;
import com.ecommerce.davivienda.exception.cart.CartException;
import com.ecommerce.davivienda.repository.cart.CartItemRepository;
import com.ecommerce.davivienda.repository.cart.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ecommerce.davivienda.constants.Constants.CODE_USER_CART_NOT_FOUND;
import static com.ecommerce.davivienda.constants.Constants.ERROR_USER_CART_NOT_FOUND;

/**
 * Implementación del servicio transaccional para operaciones de carritos en contexto de stock.
 * Centraliza acceso a datos de carritos para validación de inventario.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StockCartTransactionalServiceImpl implements StockCartTransactionalService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    @Transactional(readOnly = true)
    public Cart getCartByUserRoleId(Integer userRoleId) {
        log.debug("Buscando carrito para userRoleId: {}", userRoleId);

        return cartRepository.findByUsuarioRolId(userRoleId)
                .orElseThrow(() -> {
                    log.warn("Carrito no encontrado para userRoleId: {}", userRoleId);
                    return new CartException(ERROR_USER_CART_NOT_FOUND, CODE_USER_CART_NOT_FOUND);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public List<CartItem> getCartItems(Integer cartId) {
        log.debug("Obteniendo items del carrito: {}", cartId);
        return cartItemRepository.findByCartCarritoId(cartId);
    }
}

