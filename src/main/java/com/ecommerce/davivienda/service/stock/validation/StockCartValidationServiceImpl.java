package com.ecommerce.davivienda.service.stock.validation;

import com.ecommerce.davivienda.entity.cart.CartItem;
import com.ecommerce.davivienda.exception.stock.StockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.ecommerce.davivienda.constants.Constants.CODE_CART_NO_ITEMS;
import static com.ecommerce.davivienda.constants.Constants.ERROR_CART_NO_ITEMS;

/**
 * Implementación del servicio de validación de carrito en contexto de stock.
 * Valida reglas de negocio relacionadas con carritos e inventario.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StockCartValidationServiceImpl implements StockCartValidationService {

    @Override
    public void validateCartHasItems(List<CartItem> cartItems) {
        if (cartItems == null || cartItems.isEmpty()) {
            log.warn("Carrito sin productos para validar");
            throw new StockException(ERROR_CART_NO_ITEMS, CODE_CART_NO_ITEMS);
        }
    }
}

