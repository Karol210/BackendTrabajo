package com.ecommerce.davivienda.security.token;

import com.ecommerce.davivienda.security.util.SimpleGrantedAuthorityJsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import static com.ecommerce.davivienda.security.util.TokenJwtConfig.SECRET_KEY;

/**
 * Componente responsable de validar y parsear tokens JWT.
 * Maneja la validación de firma, extracción de claims y deserialización de authorities.
 *
 * @author Team Tienda Digital
 * @since 1.0.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenValidator {

    private static final String FIELD_AUTHORITIES = "authorities";

    private final ObjectMapper objectMapper;

    /**
     * Valida un token JWT y extrae los claims.
     *
     * @param token Token JWT a validar
     * @return Claims del token
     * @throws JwtException si el token es inválido o ha expirado
     */
    public Claims validateAndParseToken(String token) throws JwtException {
        log.debug("Validando token JWT...");

        Claims claims = parseToken(token);
        
        String userName = claims.getSubject();
        Object authoritiesClaims = claims.get(FIELD_AUTHORITIES);
        
        log.info("🔍 JWT Claims - Usuario: {}, Authorities raw: {}", userName, authoritiesClaims);
        log.debug("Token JWT válido");

        return claims;
    }

    /**
     * Extrae y deserializa las authorities del token JWT.
     *
     * @param claims Claims del token
     * @return Colección de authorities
     * @throws IOException si hay error al deserializar
     */
    public Collection<? extends GrantedAuthority> extractAuthorities(Claims claims) throws IOException {
        log.debug("Deserializando authorities del token...");

        Object authoritiesClaims = claims.get(FIELD_AUTHORITIES);
        Collection<? extends GrantedAuthority> authorities = deserializeAuthorities(authoritiesClaims);

        log.info("🔍 JWT Authorities deserialized: {}", authorities);
        
        return authorities;
    }

    /**
     * Parsea el token JWT y valida su firma.
     *
     * @param token Token JWT
     * @return Claims del token
     * @throws JwtException si el token es inválido
     */
    private Claims parseToken(String token) throws JwtException {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Deserializa las authorities desde el formato JSON.
     *
     * @param authoritiesClaims Claims de authorities en formato JSON
     * @return Colección de authorities
     * @throws IOException si hay error al deserializar
     */
    private Collection<? extends GrantedAuthority> deserializeAuthorities(Object authoritiesClaims) 
            throws IOException {
        
        return Arrays.asList(
                objectMapper
                        .addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityJsonCreator.class)
                        .readValue(authoritiesClaims.toString().getBytes(), SimpleGrantedAuthority[].class)
        );
    }
}

