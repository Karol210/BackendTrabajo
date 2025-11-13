package com.ecommerce.davivienda.security.service.detailsservice.validation;

import com.ecommerce.davivienda.entity.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.ecommerce.davivienda.constants.Constants.*;

/**
 * Implementación del servicio de validación de usuarios.
 * Valida el estado activo del usuario y su rol asociado.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
public class JpaUserValidationServiceImpl implements JpaUserValidationService {

    @Override
    public void validateUserStatus(User user, String email) {
        validateUserActive(user, email);
        validateUserRole(user, email);
    }

    /**
     * Valida que el usuario esté activo.
     *
     * @param user Usuario a validar
     * @param email Email del usuario (para logs)
     * @throws UsernameNotFoundException si el usuario está inactivo
     */
    private void validateUserActive(User user, String email) {
        if (!user.isActive()) {
            log.warn("[{}] Usuario inactivo: {}", CODE_USER_INACTIVE, email);
            throw new UsernameNotFoundException(
                    String.format("[%s] %s: %s", CODE_USER_INACTIVE, ERROR_USER_INACTIVE, email)
            );
        }
    }

    /**
     * Valida que el usuario tenga al menos un rol activo.
     *
     * @param user Usuario a validar
     * @param email Email del usuario (para logs)
     * @throws UsernameNotFoundException si el usuario no tiene roles activos
     */
    private void validateUserRole(User user, String email) {
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            log.warn("[{}] Usuario sin roles activos: {}", CODE_USER_NO_ACTIVE_ROLE, email);
            throw new UsernameNotFoundException(
                    String.format("[%s] %s: %s", CODE_USER_NO_ACTIVE_ROLE, ERROR_USER_NO_ACTIVE_ROLE, email)
            );
        }
    }
}

