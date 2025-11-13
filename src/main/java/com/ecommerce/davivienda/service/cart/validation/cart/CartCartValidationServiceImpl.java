package com.ecommerce.davivienda.service.cart.validation.cart;

import com.ecommerce.davivienda.exception.cart.CartException;
import com.ecommerce.davivienda.service.cart.transactional.cart.CartCartTransactionalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.ecommerce.davivienda.constants.Constants.CODE_USER_ALREADY_HAS_CART;
import static com.ecommerce.davivienda.constants.Constants.ERROR_USER_ALREADY_HAS_CART;

/**
 * Implementación del servicio de validación para operaciones de carritos.
 * Valida reglas de negocio relacionadas con carritos.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CartCartValidationServiceImpl implements CartCartValidationService {

    private final CartCartTransactionalService transactionalService;

    @Override
    public void validateUserHasNoCart(Integer usuarioRolId) {
        log.debug("Validando que el usuarioRolId {} no tenga carrito existente", usuarioRolId);

        if (transactionalService.existsByUsuarioRolId(usuarioRolId)) {
            log.error("Usuario ya tiene un carrito existente: usuarioRolId={}", usuarioRolId);
            throw new CartException(ERROR_USER_ALREADY_HAS_CART, CODE_USER_ALREADY_HAS_CART);
        }

        log.debug("Usuario no tiene carrito existente");
    }
}

