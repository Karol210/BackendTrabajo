package com.ecommerce.davivienda.service.cart.autocreate;

import com.ecommerce.davivienda.entity.cart.Cart;
import com.ecommerce.davivienda.exception.cart.CartException;
import com.ecommerce.davivienda.repository.cart.CartRepository;
import com.ecommerce.davivienda.repository.user.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.ecommerce.davivienda.constants.Constants.*;

/**
 * Implementación del servicio de auto-creación de carritos.
 * Maneja la lógica de obtener o crear carritos automáticamente.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CartAutoCreateServiceImpl implements CartAutoCreateService {

    private final CartRepository cartRepository;
    private final UserRoleRepository userRoleRepository;

    @Override
    @Transactional
    public Cart getOrCreateCart(Integer cartId, Integer userRoleId) {
        log.debug("Intentando obtener o crear carrito con ID: {} y userRoleId: {}", cartId, userRoleId);
        
        if (cartId == null) {
            log.warn("CartId es nulo, no se puede buscar ni crear carrito");
            throw new CartException(ERROR_CART_NOT_FOUND, CODE_CART_NOT_FOUND);
        }
        
        Optional<Cart> existingCart = cartRepository.findById(cartId);
        
        if (existingCart.isPresent()) {
            log.debug("Carrito encontrado con ID: {}", cartId);
            return existingCart.get();
        }
        
        log.info("Carrito no encontrado con ID: {}, intentando crear automáticamente", cartId);
        
        if (userRoleId == null) {
            log.warn("No se puede crear carrito sin userRoleId");
            throw new CartException(ERROR_CART_USER_ROLE_REQUIRED, CODE_CART_USER_ROLE_REQUIRED);
        }
        
        validateUserRoleExists(userRoleId);
        
        return createCart(cartId, userRoleId);
    }

    /**
     * Valida que el userRoleId exista en la base de datos.
     *
     * @param userRoleId ID del usuario_rol a validar
     */
    private void validateUserRoleExists(Integer userRoleId) {
        log.debug("Validando existencia del userRoleId: {}", userRoleId);
        
        if (!userRoleRepository.existsById(userRoleId)) {
            log.warn("UserRoleId no encontrado: {}", userRoleId);
            throw new CartException(ERROR_USER_ROLE_NOT_FOUND, CODE_USER_ROLE_NOT_FOUND);
        }
    }
    
    /**
     * Crea un nuevo carrito con el ID y userRoleId proporcionados.
     *
     * @param cartId ID del carrito a crear
     * @param userRoleId ID del usuario_rol
     * @return Cart creado
     */
    private Cart createCart(Integer cartId, Integer userRoleId) {
        log.info("Creando nuevo carrito con ID: {} para userRoleId: {}", cartId, userRoleId);
        
        Cart newCart = Cart.builder()
                .carritoId(cartId)
                .usuarioRolId(userRoleId)
                .build();
        
        Cart savedCart = cartRepository.save(newCart);
        
        log.info("Carrito creado exitosamente con ID: {}", savedCart.getCarritoId());
        
        return savedCart;
    }
}

