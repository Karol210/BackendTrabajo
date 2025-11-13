package com.ecommerce.davivienda.service.payment.validation.cart;

import com.ecommerce.davivienda.entity.cart.Cart;
import com.ecommerce.davivienda.exception.payment.PaymentException;
import com.ecommerce.davivienda.repository.cart.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.ecommerce.davivienda.constants.Constants.*;

/**
 * Implementación del servicio de validación de carritos para procesamiento de pagos.
 * Contiene toda la lógica de validación relacionada con carritos.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentCartValidationServiceImpl implements PaymentCartValidationService {

    private final CartRepository cartRepository;

    @Override
    public Cart validateCart(Integer cartId) {
        log.debug("Validando carrito con ID: {}", cartId);

        if (cartId == null) {
            log.error("ID de carrito es nulo");
            throw new PaymentException(ERROR_CART_NOT_FOUND, CODE_CART_NOT_FOUND);
        }

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> {
                    log.error("Carrito no encontrado con ID: {}", cartId);
                    return new PaymentException(ERROR_CART_NOT_FOUND, CODE_CART_NOT_FOUND);
                });

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            log.error("Carrito vacío con ID: {}", cartId);
            throw new PaymentException(ERROR_CART_EMPTY_FOR_PAYMENT, CODE_CART_EMPTY_FOR_PAYMENT);
        }

        log.info("Carrito validado exitosamente - ID: {}, Items: {}", cartId, cart.getItems().size());
        return cart;
    }

    @Override
    public Cart validateCartByUserEmail(String correo) {
        log.debug("Validando carrito del usuario con correo: {}", correo);

        if (correo == null || correo.trim().isEmpty()) {
            log.error("Correo de usuario es nulo o vacío");
            throw new PaymentException(ERROR_CART_NOT_FOUND, CODE_CART_NOT_FOUND);
        }

        Cart cart = cartRepository.findByUserEmail(correo)
                .orElseThrow(() -> {
                    log.error("Carrito no encontrado para el usuario: {}", correo);
                    return new PaymentException(ERROR_CART_NOT_FOUND, CODE_CART_NOT_FOUND);
                });

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            log.error("Carrito vacío para el usuario: {}", correo);
            throw new PaymentException(ERROR_CART_EMPTY_FOR_PAYMENT, CODE_CART_EMPTY_FOR_PAYMENT);
        }

        log.info("Carrito validado exitosamente para usuario {} - ID: {}, Items: {}", 
                correo, cart.getCarritoId(), cart.getItems().size());
        return cart;
    }
}

