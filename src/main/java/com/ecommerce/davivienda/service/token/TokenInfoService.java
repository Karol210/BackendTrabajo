package com.ecommerce.davivienda.service.token;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Map;

/**
 * Servicio para extraer información de tokens JWT.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface TokenInfoService {

    /**
     * Valida un token JWT y extrae toda su información.
     *
     * @param token Token JWT (sin prefijo "Bearer ")
     * @return Mapa con información del token
     * @throws io.jsonwebtoken.JwtException si el token es inválido o ha expirado
     */
    Map<String, Object> getTokenInfo(String token);

    /**
     * Extrae el username del token JWT.
     *
     * @param token Token JWT (sin prefijo "Bearer ")
     * @return Username del usuario autenticado
     * @throws io.jsonwebtoken.JwtException si el token es inválido
     */
    String extractUsername(String token);

    /**
     * Extrae los roles/authorities del token JWT.
     *
     * @param token Token JWT (sin prefijo "Bearer ")
     * @return Colección de authorities del usuario
     * @throws io.jsonwebtoken.JwtException si el token es inválido
     */
    Collection<? extends GrantedAuthority> extractAuthorities(String token);

    /**
     * Verifica si un token es válido.
     *
     * @param token Token JWT (sin prefijo "Bearer ")
     * @return true si el token es válido, false en caso contrario
     */
    boolean isTokenValid(String token);

    /**
     * Extrae información completa de los claims del token.
     *
     * @param token Token JWT (sin prefijo "Bearer ")
     * @return Claims del token
     * @throws io.jsonwebtoken.JwtException si el token es inválido
     */
    Claims extractClaims(String token);
}

