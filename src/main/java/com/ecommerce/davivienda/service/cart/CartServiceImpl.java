package com.ecommerce.davivienda.service.cart;

import com.ecommerce.davivienda.entity.cart.Cart;
import com.ecommerce.davivienda.entity.user.User;
import com.ecommerce.davivienda.entity.user.UserRole;
import com.ecommerce.davivienda.exception.cart.CartException;
import com.ecommerce.davivienda.mapper.cart.CartMapper;
import com.ecommerce.davivienda.service.cart.transactional.cart.CartCartTransactionalService;
import com.ecommerce.davivienda.service.cart.validation.cart.CartCartValidationService;
import com.ecommerce.davivienda.service.cart.validation.user.CartUserValidationService;
import com.ecommerce.davivienda.util.AuthenticatedUserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ecommerce.davivienda.constants.Constants.*;

/**
 * Implementación del servicio de gestión de carritos de compras.
 * Coordina la creación de carritos para usuarios autenticados.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartCartTransactionalService cartTransactionalService;
    private final CartUserValidationService userValidationService;
    private final CartCartValidationService cartValidationService;
    private final CartMapper cartMapper;
    private final AuthenticatedUserUtil authenticatedUserUtil;

    @Override
    @Transactional
    public void createCart() {
        log.info("Iniciando creación de carrito para usuario autenticado");

        try {
            String userEmail = authenticatedUserUtil.getCurrentUsername();
            log.debug("Email extraído del token: {}", userEmail);

            User user = userValidationService.validateUserExists(userEmail);
            UserRole primaryRole = userValidationService.getUserPrimaryRole(user.getUsuarioId());
            cartValidationService.validateUserHasNoCart(primaryRole.getUsuarioRolId());

            Cart cart = cartMapper.toEntity(primaryRole);
            Cart savedCart = cartTransactionalService.saveCart(cart);

            log.info("Carrito creado exitosamente: carritoId={}, usuarioRolId={}, userEmail={}", 
                    savedCart.getCarritoId(), 
                    savedCart.getUsuarioRolId(), 
                    userEmail);

        } catch (IllegalStateException e) {
            throw new CartException(ERROR_CART_AUTHENTICATION_REQUIRED, CODE_CART_AUTHENTICATION_REQUIRED, e);
        } catch (Exception e) {
            throw new CartException(ERROR_CART_CREATION_FAILED, CODE_CART_CREATION_FAILED, e);
        }
    }
}

