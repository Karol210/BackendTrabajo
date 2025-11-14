package com.ecommerce.davivienda.security.filter;

import com.ecommerce.davivienda.dto.user.UserProfileDto;
import com.ecommerce.davivienda.mapper.user.UserProfileMapper;
import com.ecommerce.davivienda.repository.user.UserRepository;
import com.ecommerce.davivienda.security.credentials.CredentialsExtractor;
import com.ecommerce.davivienda.security.response.AuthenticationResponseBuilder;
import com.ecommerce.davivienda.security.token.JwtTokenGenerator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

/**
 * Filtro de autenticación JWT para login.
 * Coordina el proceso de autenticación delegando responsabilidades específicas a componentes especializados:
 * - {@link CredentialsExtractor}: Extracción de credenciales del request
 * - {@link JwtTokenGenerator}: Generación de tokens JWT
 * - {@link AuthenticationResponseBuilder}: Construcción de respuestas HTTP con perfil de usuario
 * - {@link UserProfileMapper}: Transformación de entidad User a DTO de perfil
 *
 * @author Team Tienda Digital
 * @since 1.0.0
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    /**
     * Endpoint de login. Configurar con {@code setFilterProcessesUrl()} desde la configuración de Security.
     */
    public static final String LOGIN_ENDPOINT = "/api/v1/auth/login";

    private final AuthenticationManager authenticationManager;
    private final CredentialsExtractor credentialsExtractor;
    private final JwtTokenGenerator tokenGenerator;
    private final AuthenticationResponseBuilder responseBuilder;
    private final UserRepository userRepository;
    private final UserProfileMapper userProfileMapper;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) 
            throws AuthenticationException {
        
        Map<String, String> credentials = credentialsExtractor.extractCredentials(request);
        String email = credentialsExtractor.extractEmail(credentials);
        
        log.info("Intento de autenticación para el usuario: {}", email);
        
        UsernamePasswordAuthenticationToken authenticationToken = 
                credentialsExtractor.createAuthenticationToken(credentials);
        
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request, 
            HttpServletResponse response, 
            FilterChain chain,
            Authentication authResult) throws IOException, ServletException {

        org.springframework.security.core.userdetails.User springUser = 
                (org.springframework.security.core.userdetails.User) authResult.getPrincipal();
        String userName = springUser.getUsername();
        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();

        log.info("Procesando autenticación exitosa para usuario: {} con roles: {}", userName, authorities);

        // Obtener perfil completo del usuario desde BD
        UserProfileDto userProfile = getUserProfile(userName);

        // Generar token JWT
        String token = tokenGenerator.generateToken(userName, authorities);
        
        // Construir respuesta con token y perfil de usuario
        responseBuilder.addTokenToHeader(response, token);
        responseBuilder.writeSuccessResponse(response, token, userName, userProfile);

        log.info("Autenticación completada exitosamente para el usuario: {}", userName);
    }

    /**
     * Obtiene el perfil del usuario desde la base de datos y lo convierte a DTO.
     * 
     * @param email Email del usuario
     * @return UserProfileDto con información del perfil (sin IDs ni credenciales)
     */
    private UserProfileDto getUserProfile(String email) {
        return userRepository.findByCredenciales_Correo(email)
                .map(userProfileMapper::toProfileDto)
                .orElse(null);
    }

    @Override
    protected void unsuccessfulAuthentication(
            HttpServletRequest request, 
            HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {

        responseBuilder.writeErrorResponse(response, failed);
        log.warn("Autenticación fallida: {}", failed.getMessage());
    }
}

