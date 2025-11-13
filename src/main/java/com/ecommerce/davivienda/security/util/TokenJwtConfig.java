package com.ecommerce.davivienda.security.util;

import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;

/**
 * Configuración de constantes para JWT.
 * Define la clave secreta, prefijos y headers para autenticación JWT.
 *
 * @author Team Tienda Digital
 * @since 1.0.0
 */
public class TokenJwtConfig {
    
    /**
     * Clave secreta para firmar y verificar tokens JWT.
     * Se genera automáticamente usando el algoritmo HS256.
     */
    public static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();
    
    /**
     * Prefijo del token en el header Authorization.
     */
    public static final String PREFIX_TOKEN = "Bearer ";
    
    /**
     * Nombre del header de autorización.
     */
    public static final String HEADER_AUTHORIZATION = "Authorization";
    
    /**
     * Content-Type para respuestas JSON.
     */
    public static final String CONTENT_TYPE = "application/json";
    
    /**
     * Tiempo de expiración del token en milisegundos (1 hora).
     */
    public static final long EXPIRATION_TIME = 3600000; // 1 hora
    
    /**
     * Constructor privado para evitar instanciación.
     */
    private TokenJwtConfig() {
        throw new IllegalStateException("Utility class - No se puede instanciar");
    }
}

