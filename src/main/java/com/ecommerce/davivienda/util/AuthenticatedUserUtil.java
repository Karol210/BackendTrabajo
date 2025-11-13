package com.ecommerce.davivienda.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Utilidad para obtener información del usuario autenticado.
 * Extrae datos del contexto de seguridad de Spring Security.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Component
public class AuthenticatedUserUtil {

    /**
     * Obtiene el username del usuario autenticado actualmente.
     *
     * @return Username del usuario
     * @throws IllegalStateException si no hay usuario autenticado
     */
    public String getCurrentUsername() {
        Authentication authentication = getAuthentication();
        String username = authentication.getName();
        
        log.debug("Usuario autenticado: {}", username);
        return username;
    }

    /**
     * Obtiene los authorities/roles del usuario autenticado.
     *
     * @return Colección de authorities
     * @throws IllegalStateException si no hay usuario autenticado
     */
    public Collection<? extends GrantedAuthority> getCurrentAuthorities() {
        Authentication authentication = getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        
        log.debug("Authorities del usuario: {}", authorities);
        return authorities;
    }

    /**
     * Verifica si el usuario autenticado tiene un rol específico.
     *
     * @param role Rol a verificar (ej: "ROLE_ADMIN")
     * @return true si el usuario tiene el rol, false en caso contrario
     */
    public boolean hasRole(String role) {
        try {
            Collection<? extends GrantedAuthority> authorities = getCurrentAuthorities();
            boolean hasRole = authorities.stream()
                    .anyMatch(auth -> role.equals(auth.getAuthority()));
            
            log.debug("Usuario tiene rol '{}': {}", role, hasRole);
            return hasRole;
            
        } catch (IllegalStateException e) {
            log.debug("No hay usuario autenticado para verificar rol '{}'", role);
            return false;
        }
    }

    /**
     * Verifica si hay un usuario autenticado actualmente.
     *
     * @return true si hay usuario autenticado, false en caso contrario
     */
    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean authenticated = authentication != null && authentication.isAuthenticated() 
                && !"anonymousUser".equals(authentication.getPrincipal());
        
        log.debug("¿Usuario autenticado?: {}", authenticated);
        return authenticated;
    }

    /**
     * Obtiene la autenticación actual del contexto de seguridad.
     *
     * @return Authentication object
     * @throws IllegalStateException si no hay usuario autenticado
     */
    private Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("No hay usuario autenticado en el contexto de seguridad");
        }
        
        if ("anonymousUser".equals(authentication.getPrincipal())) {
            throw new IllegalStateException("Usuario anónimo no tiene acceso");
        }
        
        return authentication;
    }
}

