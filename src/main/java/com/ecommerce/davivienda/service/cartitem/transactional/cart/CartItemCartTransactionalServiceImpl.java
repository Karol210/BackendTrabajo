package com.ecommerce.davivienda.service.cartitem.transactional.cart;

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
import java.util.Optional;

import static com.ecommerce.davivienda.constants.Constants.CODE_CART_ITEM_NOT_FOUND;
import static com.ecommerce.davivienda.constants.Constants.ERROR_CART_ITEM_NOT_FOUND;

/**
 * Implementación del servicio transaccional para operaciones de Cart y CartItem.
 * Centraliza el acceso a datos de carritos y sus items.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CartItemCartTransactionalServiceImpl implements CartItemCartTransactionalService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    @Transactional
    public Cart findOrCreateCart(Integer userRoleId) {
        log.debug("Buscando carrito para usuario {}", userRoleId);
        
        return cartRepository.findByUsuarioRolId(userRoleId)
                .orElseGet(() -> {
                    Cart newCart = Cart.builder()
                            .usuarioRolId(userRoleId)
                            .build();
                    Cart savedCart = cartRepository.save(newCart);
                    return savedCart;
                });
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<CartItem> findCartItemByCartAndProduct(Integer cartId, Integer productId) {
        log.debug("Buscando CartItem en carrito {} para producto {}", cartId, productId);
        return cartItemRepository.findByCartAndProduct(cartId, productId);
    }


    @Override
    @Transactional(readOnly = true)
    public List<CartItem> findCartItemsByCartId(Integer cartId) {
        log.debug("Obteniendo items del carrito {}", cartId);
        return cartItemRepository.findByCartCarritoId(cartId);
    }

    @Override
    @Transactional
    public CartItem saveCartItem(CartItem cartItem) {
        log.debug("Guardando CartItem en carrito {}", cartItem.getCart().getCarritoId());
        CartItem saved = cartItemRepository.save(cartItem);
        return saved;
    }

    @Override
    @Transactional
    public void deleteCartItem(CartItem cartItem) {
        log.debug("Eliminando CartItem con ID: {}", cartItem.getProductosCarritoId());
        cartItemRepository.delete(cartItem);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CartItem> findCartItemByIdAndUser(Integer itemId, Integer userRoleId) {
        log.debug("Buscando CartItem con ID {} para usuario {} (con validación ownership)", itemId, userRoleId);
        return cartItemRepository.findByProductIdAndUserRole(itemId, userRoleId);
    }
}

