package com.ecommerce.davivienda.security.credentials;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

import static com.ecommerce.davivienda.constants.Constants.*;

/**
 * Componente responsable de extraer y procesar credenciales del request.
 * Maneja la lectura del request y creación del token de autenticación.
 *
 * @author Team Tienda Digital
 * @since 1.0.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CredentialsExtractor {

    private static final String FIELD_EMAIL = "email";
    private static final String FIELD_PASSWORD = "password";

    private final ObjectMapper objectMapper;

    /**
     * Extrae las credenciales del HttpServletRequest.
     *
     * @param request HttpServletRequest con credenciales en el body
     * @return Map con email y password
     * @throws RuntimeException Si hay error al leer las credenciales
     */
    @SuppressWarnings("unchecked")
    public Map<String, String> extractCredentials(HttpServletRequest request) {
        try {
            Map<String, String> credentials = objectMapper.readValue(request.getInputStream(), Map.class);
            log.debug("Credenciales extraídas exitosamente para usuario: {}", 
                    credentials.get(FIELD_EMAIL));
            return credentials;
            
        } catch (IOException e) {
            log.error("{}: {}", ERROR_CREDENTIALS_READ, e.getMessage(), e);
            throw new RuntimeException(ERROR_CREDENTIALS_READ, e);
        }
    }

    /**
     * Crea un token de autenticación desde las credenciales.
     *
     * @param credentials Map con email y password
     * @return UsernamePasswordAuthenticationToken
     */
    public UsernamePasswordAuthenticationToken createAuthenticationToken(
            Map<String, String> credentials) {
        
        String email = credentials.get(FIELD_EMAIL);
        String password = credentials.get(FIELD_PASSWORD);
        
        log.debug("Creando token de autenticación para usuario: {}", email);
        
        return new UsernamePasswordAuthenticationToken(email, password);
    }

    /**
     * Extrae el email de las credenciales.
     *
     * @param credentials Map con credenciales
     * @return Email del usuario
     */
    public String extractEmail(Map<String, String> credentials) {
        return credentials.get(FIELD_EMAIL);
    }
}

