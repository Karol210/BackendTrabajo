package com.ecommerce.davivienda.service.payment.transactional.cartitem;

import com.ecommerce.davivienda.entity.cart.CartItem;
import com.ecommerce.davivienda.repository.cart.CartItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación del servicio transaccional para operaciones de CartItem.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentCartItemTransactionalServiceImpl implements PaymentCartItemTransactionalService {

    private final CartItemRepository cartItemRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CartItem> findByCartId(Integer cartId) {
        log.debug("Obteniendo items del carrito: {}", cartId);
        return cartItemRepository.findByCartCarritoId(cartId);
    }
}

