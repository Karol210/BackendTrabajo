package com.ecommerce.davivienda.service.cart.validation;

import com.ecommerce.davivienda.entity.user.User;
import com.ecommerce.davivienda.entity.user.UserRole;
import com.ecommerce.davivienda.exception.cart.CartException;
import com.ecommerce.davivienda.repository.cart.CartRepository;
import com.ecommerce.davivienda.repository.user.UserRepository;
import com.ecommerce.davivienda.repository.user.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ecommerce.davivienda.constants.Constants.*;

/**
 * Implementación del servicio de validación para operaciones de carritos.
 * Valida usuarios, roles y existencia de carritos antes de operaciones de negocio.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CartValidationServiceImpl implements CartValidationService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final CartRepository cartRepository;

    @Override
    @Transactional(readOnly = true)
    public User validateUserExists(String email) {
        log.debug("Validando existencia del usuario con email: {}", email);

        return userRepository.findByCredenciales_Correo(email)
                .orElseThrow(() -> {
                    log.error("Usuario no encontrado con email: {}", email);
                    return new CartException(ERROR_USER_NOT_FOUND_FOR_CART, CODE_USER_NOT_FOUND_FOR_CART);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public void validateUserHasNoCart(Integer usuarioRolId) {
        log.debug("Validando que el usuarioRolId {} no tenga carrito existente", usuarioRolId);

        if (cartRepository.existsByUsuarioRolId(usuarioRolId)) {
            log.error("Usuario ya tiene un carrito existente: usuarioRolId={}", usuarioRolId);
            throw new CartException(ERROR_USER_ALREADY_HAS_CART, CODE_USER_ALREADY_HAS_CART);
        }

        log.debug("Usuario no tiene carrito existente");
    }

    @Override
    @Transactional(readOnly = true)
    public UserRole getUserPrimaryRole(Integer usuarioId) {
        log.debug("Obteniendo UserRole principal para usuario: {}", usuarioId);

        List<UserRole> userRoles = userRoleRepository.findByUsuarioId(usuarioId);

        if (userRoles.isEmpty()) {
            log.error("Usuario {} no tiene roles asignados", usuarioId);
            throw new CartException(ERROR_USER_NO_ROLES, CODE_USER_NO_ROLES);
        }

        UserRole primaryRole = userRoles.get(0);
        log.debug("UserRole obtenido: usuarioRolId={}, rol={}", 
                primaryRole.getUsuarioRolId(), 
                primaryRole.getRole().getNombreRol());

        return primaryRole;
    }
}

