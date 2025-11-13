package com.ecommerce.davivienda.service.cart.transactional.cart;

import com.ecommerce.davivienda.entity.cart.Cart;
import com.ecommerce.davivienda.repository.cart.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Implementación del servicio transaccional para operaciones de consulta y persistencia de Cart.
 * Centraliza todas las operaciones de acceso a datos de carritos.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CartCartTransactionalServiceImpl implements CartCartTransactionalService {

    private final CartRepository cartRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<Cart> findCartById(Integer cartId) {
        log.debug("Buscando carrito con ID: {}", cartId);
        return cartRepository.findById(cartId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsuarioRolId(Integer usuarioRolId) {
        log.debug("Verificando existencia de carrito para usuarioRolId: {}", usuarioRolId);
        return cartRepository.existsByUsuarioRolId(usuarioRolId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cart> findCartByUsuarioRolId(Integer usuarioRolId) {
        log.debug("Buscando carrito para usuarioRolId: {}", usuarioRolId);
        return cartRepository.findByUsuarioRolId(usuarioRolId);
    }

    @Override
    @Transactional
    public Cart saveCart(Cart cart) {
        log.debug("Guardando carrito con usuarioRolId: {}", cart.getUsuarioRolId());
        return cartRepository.save(cart);
    }
}

