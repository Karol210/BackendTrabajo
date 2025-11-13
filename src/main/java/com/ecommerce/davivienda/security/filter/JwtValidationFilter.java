package com.ecommerce.davivienda.security.filter;

import com.ecommerce.davivienda.security.response.AuthenticationResponseBuilder;
import com.ecommerce.davivienda.security.token.JwtTokenExtractor;
import com.ecommerce.davivienda.security.token.JwtTokenValidator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;

import static com.ecommerce.davivienda.constants.Constants.CODE_JWT_AUTHORITIES_PARSE_ERROR;
import static com.ecommerce.davivienda.constants.Constants.CODE_JWT_TOKEN_INVALID;
import static com.ecommerce.davivienda.constants.Constants.ERROR_JWT_AUTHORITIES_PARSE;
import static com.ecommerce.davivienda.constants.Constants.ERROR_JWT_TOKEN_INVALID;

/**
 * Filtro de validación JWT para peticiones protegidas.
 * Coordina el proceso de validación delegando responsabilidades específicas a componentes especializados:
 * - {@link JwtTokenExtractor}: Extracción de token del header
 * - {@link JwtTokenValidator}: Validación y parsing del token JWT
 * - {@link AuthenticationResponseBuilder}: Construcción de respuestas de error
 *
 * @author Team Tienda Digital
 * @since 1.0.0
 */
@Slf4j
public class JwtValidationFilter extends BasicAuthenticationFilter {

    // ==================== CONSTANTES LOCALES ====================
    
    /**
     * Endpoint de login que tiene su propio filtro de autenticación.
     * Este endpoint NUNCA debe ser validado por el filtro JWT.
     */
    private static final String LOGIN_ENDPOINT = "/api/v1/auth/login";
    
    // ==================== CAMPOS ====================
    
    private final JwtTokenExtractor tokenExtractor;
    private final JwtTokenValidator tokenValidator;
    private final AuthenticationResponseBuilder responseBuilder;

    /**
     * Constructor con AuthenticationManager y componentes especializados.
     *
     * @param authenticationManager Manager de autenticación de Spring Security
     * @param tokenExtractor Extractor de tokens JWT
     * @param tokenValidator Validador de tokens JWT
     * @param responseBuilder Constructor de respuestas HTTP
     */
    public JwtValidationFilter(
            AuthenticationManager authenticationManager,
            JwtTokenExtractor tokenExtractor,
            JwtTokenValidator tokenValidator,
            AuthenticationResponseBuilder responseBuilder) {
        super(authenticationManager);
        this.tokenExtractor = tokenExtractor;
        this.tokenValidator = tokenValidator;
        this.responseBuilder = responseBuilder;
    }

    /**
     * Filtra cada request para validar el token JWT.
     *
     * @param request Request HTTP
     * @param response Response HTTP
     * @param chain Cadena de filtros
     * @throws IOException si hay error de I/O
     * @throws ServletException si hay error en el servlet
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        if (shouldSkipValidation(request)) {
            chain.doFilter(request, response);
            return;
        }

        String token = tokenExtractor.extractToken(request);

        if (token == null) {
            chain.doFilter(request, response);
            return;
        }

        try {
            Claims claims = tokenValidator.validateAndParseToken(token);
            String userName = claims.getSubject();

            Collection<? extends GrantedAuthority> authorities = tokenValidator.extractAuthorities(claims);

            UsernamePasswordAuthenticationToken authenticationToken =
                    createAuthenticationToken(userName, authorities);

            setAuthenticationInContext(authenticationToken);

            log.info("✅ Token JWT válido para el usuario: {} con authorities: {}", userName, authorities);

            chain.doFilter(request, response);

        } catch (JwtException e) {
            responseBuilder.writeValidationErrorResponse(response, e, ERROR_JWT_TOKEN_INVALID, CODE_JWT_TOKEN_INVALID);
        } catch (IOException e) {
            responseBuilder.writeValidationErrorResponse(response, e, ERROR_JWT_AUTHORITIES_PARSE, CODE_JWT_AUTHORITIES_PARSE_ERROR);
        }
    }

    /**
     * Verifica si el request debe omitir la validación JWT completamente.
     * Solo el endpoint de login debe ser omitido, ya que tiene su propio filtro.
     * 
     * Para los demás endpoints, la autorización la maneja SecurityConfig.
     * Este filtro solo valida tokens JWT cuando están presentes.
     *
     * @param request Request HTTP
     * @return true si debe omitir validación, false en caso contrario
     */
    private boolean shouldSkipValidation(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        
        // Solo saltar el endpoint de login (tiene su propio filtro de autenticación)
        return LOGIN_ENDPOINT.equals(requestUri);
    }

    /**
     * Crea el token de autenticación de Spring Security.
     *
     * @param userName Nombre de usuario
     * @param authorities Authorities del usuario
     * @return Token de autenticación
     */
    private UsernamePasswordAuthenticationToken createAuthenticationToken(
            String userName, 
            Collection<? extends GrantedAuthority> authorities) {
        return new UsernamePasswordAuthenticationToken(userName, null, authorities);
    }

    /**
     * Establece el token de autenticación en el contexto de seguridad.
     *
     * @param authenticationToken Token de autenticación
     */
    private void setAuthenticationInContext(UsernamePasswordAuthenticationToken authenticationToken) {
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}

