package com.ecommerce.davivienda.service.cartitem.validation.common;

import com.ecommerce.davivienda.exception.cart.CartException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.ecommerce.davivienda.constants.Constants.CODE_CART_INVALID_QUANTITY;
import static com.ecommerce.davivienda.constants.Constants.ERROR_CART_INVALID_QUANTITY;

/**
 * Implementación del servicio de validaciones genéricas para items del carrito.
 * Aplica reglas de negocio comunes a múltiples dominios.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
public class CartItemCommonValidationServiceImpl implements CartItemCommonValidationService {

    @Override
    public void validateQuantity(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            log.warn("Cantidad inválida: {}", quantity);
            throw new CartException(ERROR_CART_INVALID_QUANTITY, CODE_CART_INVALID_QUANTITY);
        }
    }
}

