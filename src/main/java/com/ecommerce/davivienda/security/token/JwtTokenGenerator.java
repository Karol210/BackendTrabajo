package com.ecommerce.davivienda.security.token;

import com.ecommerce.davivienda.util.JsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;

import static com.ecommerce.davivienda.security.util.TokenJwtConfig.*;

/**
 * Componente responsable de la generación de tokens JWT.
 * Maneja la creación de claims y la construcción del token.
 *
 * @author Team Tienda Digital
 * @since 1.0.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenGenerator {

    private static final String FIELD_AUTHORITIES = "authorities";
    private static final String FIELD_USERNAME = "username";

    private final JsonUtils jsonUtils;

    /**
     * Genera un token JWT para el usuario autenticado.
     *
     * @param userName Nombre del usuario
     * @param authorities Permisos del usuario
     * @return Token JWT generado
     * @throws JsonProcessingException Si hay error al serializar authorities
     */
    public String generateToken(String userName, Collection<? extends GrantedAuthority> authorities) 
            throws JsonProcessingException {
        
        log.debug("Generando token para usuario: {} con roles: {}", userName, authorities);

        String authoritiesJson = serializeAuthorities(authorities);
        Claims claims = buildClaims(userName, authoritiesJson);

        String token = buildJwtToken(userName, claims);
        
        log.info("Token generado exitosamente para usuario: {}", userName);
        return token;
    }

    /**
     * Serializa las authorities del usuario a JSON.
     *
     * @param authorities Permisos del usuario
     * @return JSON con authorities
     * @throws JsonProcessingException Si hay error en la serialización
     */
    private String serializeAuthorities(Collection<? extends GrantedAuthority> authorities) 
            throws JsonProcessingException {
        
        String authoritiesJson = jsonUtils.serializeToJson(authorities);
        log.debug("Authorities serializadas: {}", authoritiesJson);
        return authoritiesJson;
    }

    /**
     * Construye los claims del token JWT.
     *
     * @param userName Nombre del usuario
     * @param authoritiesJson JSON con authorities
     * @return Claims construidos
     */
    private Claims buildClaims(String userName, String authoritiesJson) {
        return Jwts.claims()
                .add(FIELD_AUTHORITIES, authoritiesJson)
                .add(FIELD_USERNAME, userName)
                .build();
    }

    /**
     * Construye el token JWT completo.
     *
     * @param userName Nombre del usuario
     * @param claims Claims del token
     * @return Token JWT firmado
     */
    private String buildJwtToken(String userName, Claims claims) {
        return Jwts.builder()
                .subject(userName)
                .claims(claims)
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .issuedAt(new Date())
                .signWith(SECRET_KEY)
                .compact();
    }
}

