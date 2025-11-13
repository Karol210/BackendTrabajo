package com.ecommerce.davivienda.service.auth;

import com.ecommerce.davivienda.entity.user.User;
import com.ecommerce.davivienda.entity.user.UserRole;
import com.ecommerce.davivienda.exception.cart.CartException;
import com.ecommerce.davivienda.repository.user.UserRepository;
import com.ecommerce.davivienda.repository.user.UserRoleRepository;
import com.ecommerce.davivienda.util.AuthenticatedUserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ecommerce.davivienda.constants.Constants.*;

/**
 * Implementación del servicio genérico para gestión de autenticación de usuarios.
 * Responsable de extraer y validar el usuario autenticado desde el token JWT.
 * Reutilizable por todos los módulos del sistema.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthUserServiceImpl implements AuthUserService {

    private final AuthenticatedUserUtil authenticatedUserUtil;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    @Override
    @Transactional(readOnly = true)
    public Integer getAuthenticatedUserRoleId() {
        try {
            String userEmail = authenticatedUserUtil.getCurrentUsername();
            log.debug("Email extraído del token: {}", userEmail);
            
            Integer userRoleId = getUserRoleIdFromEmail(userEmail);
            validateUserHasClientRole(userRoleId);
            
            log.debug("Usuario autenticado con userRoleId: {}", userRoleId);
            
            return userRoleId;
            
        } catch (IllegalStateException e) {
            log.error("Error de autenticación: {}", e.getMessage());
            throw new CartException(ERROR_CART_AUTHENTICATION_REQUIRED, CODE_CART_AUTHENTICATION_REQUIRED, e);
        }
    }

    /**
     * Obtiene el userRoleId del usuario basado en su email (username del JWT).
     * Valida que el usuario exista y que tenga roles asignados.
     *
     * @param email Email del usuario (username del JWT)
     * @return ID del UserRole del usuario
     */
    private Integer getUserRoleIdFromEmail(String email) {
        log.debug("Obteniendo userRoleId para email: {}", email);
        
        User user = userRepository.findByCredenciales_Correo(email)
                .orElseThrow(() -> {
                    log.warn("Usuario no encontrado con email: {}", email);
                    return new CartException(ERROR_USER_NOT_FOUND_BY_DOCUMENT, CODE_USER_NOT_FOUND_BY_DOCUMENT);
                });
        
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            log.warn("Usuario sin roles asignados: {}", email);
            throw new CartException(ERROR_USER_WITHOUT_ROLES, CODE_USER_WITHOUT_ROLES);
        }
        
        UserRole userRole = user.getRoles().get(0);
        Integer userRoleId = userRole.getUsuarioRolId();
        
        log.debug("UserRoleId {} encontrado para usuario: {}", userRoleId, email);
        
        return userRoleId;
    }

    /**
     * Valida que el userRoleId tenga el rol de "Cliente".
     * Solo los usuarios con rol de Cliente pueden realizar operaciones de ecommerce.
     *
     * @param userRoleId ID del UserRole a validar
     */
    private void validateUserHasClientRole(Integer userRoleId) {
        log.debug("Validando que userRoleId {} tenga rol de Cliente", userRoleId);
        
        UserRole userRole = userRoleRepository.findById(userRoleId)
                .orElseThrow(() -> {
                    log.warn("UserRole no encontrado con ID: {}", userRoleId);
                    return new CartException(ERROR_USER_ROLE_NOT_FOUND, CODE_USER_ROLE_NOT_FOUND);
                });
        
        String roleName = userRole.getRole().getNombreRol();
        
        if (!"Cliente".equalsIgnoreCase(roleName)) {
            log.warn("Usuario con userRoleId {} no tiene rol de Cliente. Rol actual: {}", 
                    userRoleId, roleName);
            throw new CartException(ERROR_USER_NOT_CLIENT_ROLE, CODE_USER_NOT_CLIENT_ROLE);
        }
        
        log.debug("Usuario con userRoleId {} validado correctamente como Cliente", userRoleId);
    }
}

