package com.ecommerce.davivienda.security.filter;

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
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

/**
 * Filtro de autenticación JWT para login.
 * Coordina el proceso de autenticación delegando responsabilidades específicas a componentes especializados:
 * - {@link CredentialsExtractor}: Extracción de credenciales del request
 * - {@link JwtTokenGenerator}: Generación de tokens JWT
 * - {@link AuthenticationResponseBuilder}: Construcción de respuestas HTTP
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

        User user = (User) authResult.getPrincipal();
        String userName = user.getUsername();
        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();

        log.info("Procesando autenticación exitosa para usuario: {} con roles: {}", userName, authorities);

        String token = tokenGenerator.generateToken(userName, authorities);
        
        responseBuilder.addTokenToHeader(response, token);
        responseBuilder.writeSuccessResponse(response, token, userName);

        log.info("Autenticación completada exitosamente para el usuario: {}", userName);
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

