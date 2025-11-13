package com.ecommerce.davivienda.service.payment.transactional.cart;

import com.ecommerce.davivienda.entity.cart.Cart;
import com.ecommerce.davivienda.exception.payment.PaymentException;
import com.ecommerce.davivienda.repository.cart.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ecommerce.davivienda.constants.Constants.*;

/**
 * Implementación del servicio transaccional para actualización de estado del carrito.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentCartTransactionalServiceImpl implements PaymentCartTransactionalService {

    private static final Integer PROCESSING_STATUS_ID = 2; // Estado "Procesando" (según init-ecommerce.sql)

    private final CartRepository cartRepository;

    @Override
    @Transactional
    public void updateCartStatusToProcessing(Integer cartId) {
        log.debug("Actualizando estado del carrito {} a 'Procesando' (ID: {})", cartId, PROCESSING_STATUS_ID);

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new PaymentException(
                        ERROR_CART_NOT_FOUND,
                        CODE_CART_NOT_FOUND
                ));

        cart.setEstadoCarritoId(PROCESSING_STATUS_ID);
        cartRepository.save(cart);

        log.info("Carrito {} actualizado a estado 'Procesando'", cartId);
    }
}

