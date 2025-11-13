package com.ecommerce.davivienda.service.cart.validation.user;

import com.ecommerce.davivienda.entity.user.User;
import com.ecommerce.davivienda.entity.user.UserRole;
import com.ecommerce.davivienda.exception.cart.CartException;
import com.ecommerce.davivienda.service.cart.transactional.user.CartUserTransactionalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.ecommerce.davivienda.constants.Constants.*;

/**
 * Implementación del servicio de validación para operaciones de usuarios.
 * Valida usuarios, roles y reglas de negocio relacionadas con usuarios.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CartUserValidationServiceImpl implements CartUserValidationService {

    private final CartUserTransactionalService transactionalService;

    @Override
    public User validateUserExists(String email) {
        log.debug("Validando existencia del usuario con email: {}", email);

        return transactionalService.findUserByEmail(email)
                .orElseThrow(() -> {
                    log.error("Usuario no encontrado con email: {}", email);
                    return new CartException(ERROR_USER_NOT_FOUND_FOR_CART, CODE_USER_NOT_FOUND_FOR_CART);
                });
    }

    @Override
    public UserRole getUserPrimaryRole(Integer usuarioId) {
        log.debug("Obteniendo UserRole principal para usuario: {}", usuarioId);

        List<UserRole> userRoles = transactionalService.findUserRolesByUserId(usuarioId);

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

    @Override
    public void validateUserRoleExists(Integer userRoleId) {
        log.debug("Validando existencia del userRoleId: {}", userRoleId);

        if (!transactionalService.existsUserRoleById(userRoleId)) {
            log.warn("UserRoleId no encontrado: {}", userRoleId);
            throw new CartException(ERROR_USER_ROLE_NOT_FOUND, CODE_USER_ROLE_NOT_FOUND);
        }
    }
}

