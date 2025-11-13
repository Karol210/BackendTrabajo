package com.ecommerce.davivienda.service.user.validation.common;

import com.ecommerce.davivienda.exception.user.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.ecommerce.davivienda.constants.Constants.*;

/**
 * Implementación del servicio de validación genérica para usuarios.
 * Capacidad interna que NO debe ser expuesta como API REST.
 * 
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
public class UserCommonValidationServiceImpl implements UserCommonValidationService {

    @Override
    public void validatePasswordNotEmpty(String password) {
        if (password == null || password.trim().isEmpty()) {
            log.error("Intento de crear usuario sin contraseña");
            throw new UserException(ERROR_PASSWORD_EMPTY, CODE_PASSWORD_EMPTY);
        }
    }
}

