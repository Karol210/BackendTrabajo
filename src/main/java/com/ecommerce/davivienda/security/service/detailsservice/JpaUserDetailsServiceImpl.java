package com.ecommerce.davivienda.security.service.detailsservice;

import com.ecommerce.davivienda.entity.user.User;
import com.ecommerce.davivienda.repository.user.UserRepository;
import com.ecommerce.davivienda.security.service.detailsservice.builder.UserDetailsBuilderService;
import com.ecommerce.davivienda.security.service.detailsservice.validation.JpaUserValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ecommerce.davivienda.constants.Constants.*;

/**
 * Servicio coordinador de UserDetails para Spring Security.
 * Carga los detalles del usuario desde la base de datos y delega a servicios especializados
 * para validación y construcción de UserDetails.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JpaUserDetailsServiceImpl implements UserDetailsService, JpaUserDetailsService {

    private final UserRepository userRepository;
    private final JpaUserValidationService validationService;
    private final UserDetailsBuilderService builderService;

    /**
     * Carga un usuario por su email (username).
     * Implementación requerida por Spring Security para autenticación.
     * Coordina la búsqueda, validación y construcción del UserDetails.
     *
     * @param email Email del usuario (usado como username)
     * @return UserDetails con información del usuario
     * @throws UsernameNotFoundException si el usuario no existe o no es válido
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.debug("Cargando usuario con email: {}", email);
        
        User user = findUserByEmail(email);
        validationService.validateUserStatus(user, email);
        
        String roles = user.getRoles() != null && !user.getRoles().isEmpty()
                ? user.getRoles().stream()
                        .map(userRole -> userRole.getRole().getNombreRol())
                        .collect(java.util.stream.Collectors.joining(", "))
                : "Sin roles";
        
        log.debug("Usuario cargado exitosamente: {} con roles: {}", email, roles);
        
        return builderService.buildUserDetails(user);
    }

    /**
     * Busca un usuario en la base de datos por su email.
     *
     * @param email Email del usuario
     * @return Usuario encontrado
     * @throws UsernameNotFoundException si el usuario no existe
     */
    private User findUserByEmail(String email) {
        return userRepository.findByCredenciales_Correo(email)
                .orElseThrow(() -> {
                    log.warn("[{}] Usuario no encontrado: {}", CODE_USER_NOT_FOUND, email);
                    return new UsernameNotFoundException(
                            String.format("[%s] %s: %s", CODE_USER_NOT_FOUND, ERROR_USER_NOT_FOUND, email)
                    );
                });
    }
}

