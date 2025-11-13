package com.ecommerce.davivienda.controller.token;

import com.ecommerce.davivienda.models.Response;
import com.ecommerce.davivienda.service.token.TokenInfoService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;

import static com.ecommerce.davivienda.constants.Constants.*;

/**
 * Controller para operaciones de información de tokens JWT.
 * Proporciona endpoints para inspeccionar y validar tokens manualmente.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/token")
@RequiredArgsConstructor
public class TokenInfoController {

    private final TokenInfoService tokenInfoService;

    /**
     * Obtiene información completa del token JWT.
     * 
     * <p>Endpoint de ejemplo: GET /api/v1/token/info</p>
     * <p>Header: Authorization: Bearer {token}</p>
     *
     * @param authorization Header de autorización (Bearer token)
     * @return Response con información del token
     */
    @GetMapping("/info")
    public ResponseEntity<Response<Map<String, Object>>> getTokenInfo(
            @RequestHeader("Authorization") String authorization) {

        log.info("📋 Solicitud de información del token JWT");

        try {
            String token = extractToken(authorization);
            Map<String, Object> tokenInfo = tokenInfoService.getTokenInfo(token);

            log.info("✅ Información del token obtenida exitosamente");

            return ResponseEntity.ok(Response.<Map<String, Object>>builder()
                    .failure(false)
                    .code(HttpStatus.OK.value())
                    .message("Información del token obtenida exitosamente")
                    .body(tokenInfo)
                    .timestamp(String.valueOf(System.currentTimeMillis()))
                    .build());

        } catch (JwtException e) {
            log.error("❌ Error al procesar token: {}", e.getMessage());

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Response.<Map<String, Object>>builder()
                            .failure(true)
                            .code(HttpStatus.UNAUTHORIZED.value())
                            .message(String.format("[%s] %s", CODE_JWT_TOKEN_INVALID, e.getMessage()))
                            .timestamp(String.valueOf(System.currentTimeMillis()))
                            .build());
        }
    }

    /**
     * Extrae el username del token JWT.
     * 
     * <p>Endpoint de ejemplo: GET /api/v1/token/username</p>
     * <p>Header: Authorization: Bearer {token}</p>
     *
     * @param authorization Header de autorización (Bearer token)
     * @return Response con el username
     */
    @GetMapping("/username")
    public ResponseEntity<Response<String>> extractUsername(
            @RequestHeader("Authorization") String authorization) {

        log.info("📋 Solicitud de extracción de username del token JWT");

        try {
            String token = extractToken(authorization);
            String username = tokenInfoService.extractUsername(token);

            log.info("✅ Username extraído exitosamente: {}", username);

            return ResponseEntity.ok(Response.<String>builder()
                    .failure(false)
                    .code(HttpStatus.OK.value())
                    .message("Username extraído exitosamente")
                    .body(username)
                    .timestamp(String.valueOf(System.currentTimeMillis()))
                    .build());

        } catch (JwtException e) {
            log.error("❌ Error al extraer username: {}", e.getMessage());

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Response.<String>builder()
                            .failure(true)
                            .code(HttpStatus.UNAUTHORIZED.value())
                            .message(String.format("[%s] %s", CODE_JWT_TOKEN_INVALID, e.getMessage()))
                            .timestamp(String.valueOf(System.currentTimeMillis()))
                            .build());
        }
    }

    /**
     * Extrae los authorities/roles del token JWT.
     * 
     * <p>Endpoint de ejemplo: GET /api/v1/token/authorities</p>
     * <p>Header: Authorization: Bearer {token}</p>
     *
     * @param authorization Header de autorización (Bearer token)
     * @return Response con los authorities
     */
    @GetMapping("/authorities")
    public ResponseEntity<Response<Collection<? extends GrantedAuthority>>> extractAuthorities(
            @RequestHeader("Authorization") String authorization) {

        log.info("📋 Solicitud de extracción de authorities del token JWT");

        try {
            String token = extractToken(authorization);
            Collection<? extends GrantedAuthority> authorities = tokenInfoService.extractAuthorities(token);

            log.info("✅ Authorities extraídas exitosamente: {}", authorities);

            return ResponseEntity.ok(Response.<Collection<? extends GrantedAuthority>>builder()
                    .failure(false)
                    .code(HttpStatus.OK.value())
                    .message("Authorities extraídas exitosamente")
                    .body(authorities)
                    .timestamp(String.valueOf(System.currentTimeMillis()))
                    .build());

        } catch (JwtException e) {
            log.error("❌ Error al extraer authorities: {}", e.getMessage());

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Response.<Collection<? extends GrantedAuthority>>builder()
                            .failure(true)
                            .code(HttpStatus.UNAUTHORIZED.value())
                            .message(String.format("[%s] %s", CODE_JWT_TOKEN_INVALID, e.getMessage()))
                            .timestamp(String.valueOf(System.currentTimeMillis()))
                            .build());
        }
    }

    /**
     * Valida si un token es válido.
     * 
     * <p>Endpoint de ejemplo: GET /api/v1/token/validate</p>
     * <p>Header: Authorization: Bearer {token}</p>
     *
     * @param authorization Header de autorización (Bearer token)
     * @return Response indicando si el token es válido
     */
    @GetMapping("/validate")
    public ResponseEntity<Response<Boolean>> validateToken(
            @RequestHeader("Authorization") String authorization) {

        log.info("📋 Solicitud de validación de token JWT");

        try {
            String token = extractToken(authorization);
            boolean isValid = tokenInfoService.isTokenValid(token);

            String message = isValid 
                    ? "Token válido" 
                    : "Token inválido";

            log.info("✅ Validación completada: {}", message);

            return ResponseEntity.ok(Response.<Boolean>builder()
                    .failure(false)
                    .code(HttpStatus.OK.value())
                    .message(message)
                    .body(isValid)
                    .timestamp(String.valueOf(System.currentTimeMillis()))
                    .build());

        } catch (Exception e) {
            log.error("❌ Error al validar token: {}", e.getMessage());

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Response.<Boolean>builder()
                            .failure(true)
                            .code(HttpStatus.UNAUTHORIZED.value())
                            .message(String.format("[%s] %s", CODE_JWT_TOKEN_INVALID, e.getMessage()))
                            .timestamp(String.valueOf(System.currentTimeMillis()))
                            .build());
        }
    }

    /**
     * Extrae el token del header Authorization.
     *
     * @param authorization Header de autorización
     * @return Token JWT sin el prefijo "Bearer "
     * @throws IllegalArgumentException si el header no es válido
     */
    private String extractToken(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Header Authorization inválido");
        }
        return authorization.substring(7);
    }
}

