package com.ecommerce.davivienda.service.cart;

import com.ecommerce.davivienda.entity.cart.Cart;
import com.ecommerce.davivienda.entity.user.User;
import com.ecommerce.davivienda.entity.user.UserRole;
import com.ecommerce.davivienda.exception.cart.CartException;
import com.ecommerce.davivienda.models.cart.CartCreateResponse;
import com.ecommerce.davivienda.repository.cart.CartRepository;
import com.ecommerce.davivienda.service.cart.validation.CartValidationService;
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

    private final CartRepository cartRepository;
    private final CartValidationService validationService;
    private final AuthenticatedUserUtil authenticatedUserUtil;

    @Override
    @Transactional
    public CartCreateResponse createCart() {
        log.info("Iniciando creación de carrito para usuario autenticado");

        try {
            String userEmail = authenticatedUserUtil.getCurrentUsername();
            log.debug("Email extraído del token: {}", userEmail);

            User user = validationService.validateUserExists(userEmail);
            UserRole primaryRole = validationService.getUserPrimaryRole(user.getUsuarioId());
            validationService.validateUserHasNoCart(primaryRole.getUsuarioRolId());

            Cart cart = buildCart(primaryRole);
            Cart savedCart = cartRepository.save(cart);

            log.info("Carrito creado exitosamente: carritoId={}, usuarioRolId={}, userEmail={}", 
                    savedCart.getCarritoId(), 
                    savedCart.getUsuarioRolId(), 
                    userEmail);

            return buildResponse(savedCart, userEmail);

        } catch (CartException e) {
            log.error("Error de negocio al crear carrito: {}", e.getMessage());
            throw e;
        } catch (IllegalStateException e) {
            log.error("Error de seguridad al crear carrito: {}", e.getMessage());
            throw new CartException(ERROR_CART_AUTHENTICATION_REQUIRED, CODE_CART_AUTHENTICATION_REQUIRED, e);
        } catch (Exception e) {
            log.error("Error inesperado al crear carrito: {}", e.getMessage(), e);
            throw new CartException(ERROR_CART_CREATION_FAILED, CODE_CART_CREATION_FAILED, e);
        }
    }

    /**
     * Construye una entidad Cart a partir del UserRole.
     *
     * @param userRole UserRole del usuario
     * @return Cart construido
     */
    private Cart buildCart(UserRole userRole) {
        return Cart.builder()
                .usuarioRolId(userRole.getUsuarioRolId())
                .build();
    }

    /**
     * Construye el DTO de respuesta con la información del carrito creado.
     *
     * @param cart Carrito creado
     * @param userEmail Email del usuario
     * @return DTO de respuesta
     */
    private CartCreateResponse buildResponse(Cart cart, String userEmail) {
        return CartCreateResponse.builder()
                .cartId(cart.getCarritoId())
                .userEmail(userEmail)
                .message(SUCCESS_CART_CREATED)
                .build();
    }
}

