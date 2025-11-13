package com.ecommerce.davivienda.service.token;

import com.ecommerce.davivienda.security.token.JwtTokenValidator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.ecommerce.davivienda.constants.Constants.ERROR_JWT_TOKEN_INFO_EXTRACTION;

/**
 * Implementación del servicio para extraer información de tokens JWT.
 * Utiliza {@link JwtTokenValidator} para validar y parsear tokens.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TokenInfoServiceImpl implements TokenInfoService {

    private final JwtTokenValidator jwtTokenValidator;

    @Override
    public Map<String, Object> getTokenInfo(String token) {
        log.debug("Extrayendo información completa del token JWT");

        try {
            Claims claims = jwtTokenValidator.validateAndParseToken(token);
            Collection<? extends GrantedAuthority> authorities = jwtTokenValidator.extractAuthorities(claims);

            Map<String, Object> tokenInfo = buildTokenInfoMap(claims, authorities);

            log.info("✅ Información del token extraída exitosamente para usuario: {}", 
                    claims.getSubject());
            
            return tokenInfo;

        } catch (JwtException e) {
            log.error("❌ Error al validar token: {}", e.getMessage());
            throw new JwtException(ERROR_JWT_TOKEN_INFO_EXTRACTION + ": " + e.getMessage(), e);
        } catch (IOException e) {
            log.error("❌ Error al extraer authorities del token: {}", e.getMessage());
            throw new RuntimeException(ERROR_JWT_TOKEN_INFO_EXTRACTION, e);
        }
    }

    @Override
    public String extractUsername(String token) {
        log.debug("Extrayendo username del token JWT");

        try {
            Claims claims = jwtTokenValidator.validateAndParseToken(token);
            String username = claims.getSubject();

            log.debug("Username extraído: {}", username);
            return username;

        } catch (JwtException e) {
            log.error("❌ Error al extraer username: {}", e.getMessage());
            throw new JwtException(ERROR_JWT_TOKEN_INFO_EXTRACTION + ": " + e.getMessage(), e);
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> extractAuthorities(String token) {
        log.debug("Extrayendo authorities del token JWT");

        try {
            Claims claims = jwtTokenValidator.validateAndParseToken(token);
            Collection<? extends GrantedAuthority> authorities = jwtTokenValidator.extractAuthorities(claims);

            log.debug("Authorities extraídas: {}", authorities);
            return authorities;

        } catch (JwtException e) {
            log.error("❌ Error al extraer authorities: {}", e.getMessage());
            throw new JwtException(ERROR_JWT_TOKEN_INFO_EXTRACTION + ": " + e.getMessage(), e);
        } catch (IOException e) {
            log.error("❌ Error al deserializar authorities: {}", e.getMessage());
            throw new RuntimeException(ERROR_JWT_TOKEN_INFO_EXTRACTION, e);
        }
    }

    @Override
    public boolean isTokenValid(String token) {
        try {
            jwtTokenValidator.validateAndParseToken(token);
            log.debug("✅ Token válido");
            return true;
        } catch (JwtException e) {
            log.debug("❌ Token inválido: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public Claims extractClaims(String token) {
        log.debug("Extrayendo claims del token JWT");

        try {
            return jwtTokenValidator.validateAndParseToken(token);
        } catch (JwtException e) {
            log.error("❌ Error al extraer claims: {}", e.getMessage());
            throw new JwtException(ERROR_JWT_TOKEN_INFO_EXTRACTION + ": " + e.getMessage(), e);
        }
    }

    /**
     * Construye un mapa con toda la información del token.
     *
     * @param claims Claims del token
     * @param authorities Authorities del usuario
     * @return Mapa con información del token
     */
    private Map<String, Object> buildTokenInfoMap(
            Claims claims, 
            Collection<? extends GrantedAuthority> authorities) {
        
        Map<String, Object> tokenInfo = new HashMap<>();
        
        // Información del usuario
        tokenInfo.put("username", claims.getSubject());
        tokenInfo.put("authorities", authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
        
        // Información del token
        tokenInfo.put("issuedAt", claims.getIssuedAt());
        tokenInfo.put("expiration", claims.getExpiration());
        tokenInfo.put("isExpired", claims.getExpiration().before(new Date()));
        
        // Claims adicionales (si existen)
        if (claims.get("email") != null) {
            tokenInfo.put("email", claims.get("email"));
        }
        
        return tokenInfo;
    }
}

