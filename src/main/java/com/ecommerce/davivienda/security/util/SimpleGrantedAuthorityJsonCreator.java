package com.ecommerce.davivienda.security.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Clase auxiliar para deserializar SimpleGrantedAuthority desde JSON.
 * Utilizada por Jackson para mapear roles desde el token JWT.
 *
 * @author Team Tienda Digital
 * @since 1.0.0
 */
public abstract class SimpleGrantedAuthorityJsonCreator {

    /**
     * Constructor para deserialización de Jackson.
     *
     * @param role Rol del usuario
     */
    @JsonCreator
    public SimpleGrantedAuthorityJsonCreator(@JsonProperty("authority") String role) {
        // Constructor abstracto para Jackson
    }
}

