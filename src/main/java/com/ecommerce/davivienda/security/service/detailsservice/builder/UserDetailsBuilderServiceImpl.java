package com.ecommerce.davivienda.security.service.detailsservice.builder;

import com.ecommerce.davivienda.entity.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementación del servicio de construcción de UserDetails.
 * Transforma entidades User en objetos UserDetails de Spring Security.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
public class UserDetailsBuilderServiceImpl implements UserDetailsBuilderService {

    @Override
    public UserDetails buildUserDetails(User user) {
        log.debug("Construyendo UserDetails para usuario: {}", user.getCorreo());
        
        List<GrantedAuthority> authorities = buildAuthorities(user);
        
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getCorreo())
                .password(user.getContrasena())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!user.isActive())
                .build();
    }

    /**
     * Construye la lista de autoridades (permisos) del usuario.
     * Extrae todos los roles de los UserRole asociados al usuario.
     *
     * @param user Usuario del cual extraer los roles
     * @return Lista de autoridades
     */
    private List<GrantedAuthority> buildAuthorities(User user) {
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            log.debug("Usuario sin roles: {}", user.getCorreo());
            return List.of();
        }
        
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .filter(userRole -> userRole.getRole() != null)
                .map(userRole -> new SimpleGrantedAuthority(userRole.getRole().getNombreRol()))
                .collect(java.util.stream.Collectors.toList());
        
        log.debug("Se construyeron {} autoridades para usuario: {}", authorities.size(), user.getCorreo());
        return authorities;
    }
}

