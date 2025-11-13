package com.ecommerce.davivienda.security.token;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.ecommerce.davivienda.security.util.TokenJwtConfig.HEADER_AUTHORIZATION;
import static com.ecommerce.davivienda.security.util.TokenJwtConfig.PREFIX_TOKEN;

/**
 * Componente responsable de extraer tokens JWT del header Authorization.
 * Maneja la extracción y limpieza del token desde el request HTTP.
 *
 * @author Team Tienda Digital
 * @since 1.0.0
 */
@Slf4j
@Component
public class JwtTokenExtractor {

    /**
     * Extrae el token JWT del header Authorization del request.
     *
     * @param request Request HTTP
     * @return Token JWT sin el prefijo "Bearer ", o null si no existe o es inválido
     */
    public String extractToken(HttpServletRequest request) {
        String header = request.getHeader(HEADER_AUTHORIZATION);

        if (!hasValidAuthorizationHeader(header)) {
            log.debug("Header Authorization no válido o ausente");
            return null;
        }

        String token = removePrefix(header);
        log.debug("Token JWT extraído exitosamente");
        return token;
    }

    /**
     * Verifica si el header Authorization es válido.
     *
     * @param header Header Authorization
     * @return true si el header existe y comienza con el prefijo correcto
     */
    private boolean hasValidAuthorizationHeader(String header) {
        return header != null && header.startsWith(PREFIX_TOKEN);
    }

    /**
     * Remueve el prefijo "Bearer " del token.
     *
     * @param header Header Authorization completo
     * @return Token sin prefijo
     */
    private String removePrefix(String header) {
        return header.replace(PREFIX_TOKEN, "");
    }
}

