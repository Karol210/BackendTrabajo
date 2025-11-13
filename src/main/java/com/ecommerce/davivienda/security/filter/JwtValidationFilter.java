package com.ecommerce.davivienda.security.filter;

import com.ecommerce.davivienda.security.SecurityEndpoints;
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
 * <p><b>⚠️ Endpoints públicos centralizados:</b></p>
 * <p>Los endpoints públicos están definidos en {@link SecurityEndpoints}.
 * Este filtro omite completamente la validación JWT para endpoints públicos,
 * permitiendo que funcionen incluso si el cliente envía un token inválido o expirado.</p>
 *
 * @author Team Tienda Digital
 * @since 1.0.0
 */
@Slf4j
public class JwtValidationFilter extends BasicAuthenticationFilter {
    
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
     * Filtra cada request para validar el token JWT cuando está presente.
     * 
     * <p><b>Flujo de validación:</b></p>
     * <ol>
     *   <li>Verifica si es un endpoint público → omite validación completamente</li>
     *   <li>Extrae el token JWT del header Authorization (si existe)</li>
     *   <li>Si NO hay token → continúa sin autenticación (SecurityConfig decide si es válido)</li>
     *   <li>Si hay token → valida y establece autenticación en el contexto</li>
     *   <li>Si token inválido → retorna error 401</li>
     * </ol>
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

        // Omitir validación JWT para endpoints públicos
        if (shouldSkipValidation(request)) {
            chain.doFilter(request, response);
            return;
        }

        String token = tokenExtractor.extractToken(request);

        // Si no hay token, continuar sin autenticación
        // SecurityConfig decidirá si el endpoint requiere autenticación
        if (token == null) {
            log.debug("🔓 Request sin token JWT a: {}", request.getRequestURI());
            chain.doFilter(request, response);
            return;
        }

        // Si hay token, validarlo
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
            log.warn("❌ Token JWT inválido para request a: {}", request.getRequestURI());
            responseBuilder.writeValidationErrorResponse(response, e, ERROR_JWT_TOKEN_INVALID, CODE_JWT_TOKEN_INVALID);
        } catch (IOException e) {
            log.error("❌ Error al parsear authorities del token JWT", e);
            responseBuilder.writeValidationErrorResponse(response, e, ERROR_JWT_AUTHORITIES_PARSE, CODE_JWT_AUTHORITIES_PARSE_ERROR);
        }
    }

    /**
     * Verifica si el request debe omitir la validación JWT completamente.
     * 
     * <p><b>⚠️ Endpoints públicos centralizados en {@link SecurityEndpoints}</b></p>
     * <p>Omite validación para todos los endpoints públicos, permitiendo que funcionen
     * incluso si el cliente envía un token JWT expirado o inválido.</p>
     *
     * @param request Request HTTP
     * @return true si debe omitir validación, false en caso contrario
     */
    private boolean shouldSkipValidation(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        
        for (String publicEndpoint : SecurityEndpoints.PUBLIC_ENDPOINTS) {
            // Comparar con el endpoint público, soportando wildcards (**)
            if (matchesPublicEndpoint(requestUri, publicEndpoint)) {
                log.debug("🌐 Endpoint público detectado: {} - Omitiendo validación JWT", requestUri);
                return true;
            }
        }
        
        return false;
    }

    /**
     * Verifica si una URI coincide con un patrón de endpoint público.
     * Soporta wildcards (**) al final de los patrones.
     *
     * @param requestUri URI del request
     * @param publicEndpoint Patrón de endpoint público
     * @return true si coincide, false en caso contrario
     */
    private boolean matchesPublicEndpoint(String requestUri, String publicEndpoint) {
        // Si el patrón termina con /**, verificar si empieza con el prefijo
        if (publicEndpoint.endsWith("/**")) {
            String prefix = publicEndpoint.substring(0, publicEndpoint.length() - 3);
            return requestUri.equals(prefix) || requestUri.startsWith(prefix + "/");
        }
        
        // Comparación exacta
        return requestUri.equals(publicEndpoint);
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

