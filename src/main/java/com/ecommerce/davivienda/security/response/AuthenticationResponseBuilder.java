package com.ecommerce.davivienda.security.response;

import com.ecommerce.davivienda.models.Response;
import com.ecommerce.davivienda.util.JsonUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.ecommerce.davivienda.constants.Constants.*;
import static com.ecommerce.davivienda.security.util.TokenJwtConfig.*;

/**
 * Componente responsable de construir respuestas HTTP para autenticación.
 * Maneja la creación de respuestas exitosas y de error.
 *
 * @author Team Tienda Digital
 * @since 1.0.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationResponseBuilder {

    private static final String FIELD_TOKEN = "token";
    private static final String FIELD_USERNAME = "username";
    private static final String FIELD_MESSAGE = "message";
    private static final String FIELD_EXPIRES_IN = "expiresIn";
    
    private static final String SUCCESS_MESSAGE_TEMPLATE = "Hola %s, has iniciado sesión con éxito";

    private final JsonUtils jsonUtils;

    /**
     * Escribe respuesta exitosa de autenticación en el HttpServletResponse.
     *
     * @param response HttpServletResponse
     * @param token Token JWT generado
     * @param userName Nombre del usuario autenticado
     * @throws IOException Si hay error al escribir la respuesta
     */
    public void writeSuccessResponse(HttpServletResponse response, String token, String userName) 
            throws IOException {
        
        log.debug("Construyendo respuesta exitosa para usuario: {}", userName);

        Map<String, Object> body = buildSuccessBody(token, userName);
        
        response.setContentType(CONTENT_TYPE);
        response.setStatus(HttpStatus.OK.value());
        response.getWriter().write(jsonUtils.serializeToJson(body));
        
        log.info("Respuesta exitosa enviada para usuario: {}", userName);
    }

    /**
     * Escribe respuesta de error de autenticación en el HttpServletResponse.
     *
     * @param response HttpServletResponse
     * @param failed Excepción de autenticación
     * @throws IOException Si hay error al escribir la respuesta
     */
    public void writeErrorResponse(HttpServletResponse response, AuthenticationException failed) 
            throws IOException {
        
        log.debug("Construyendo respuesta de error: {}", failed.getMessage());

        Response<Object> errorResponse = buildErrorResponse(failed);
        
        response.setContentType(CONTENT_TYPE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(jsonUtils.serializeToJson(errorResponse));
        
        log.warn("Respuesta de error enviada - Código: {} - Mensaje: {}", 
                errorResponse.getErrorCode(), failed.getMessage());
    }

    /**
     * Escribe respuesta de error de validación JWT en el HttpServletResponse.
     *
     * @param response HttpServletResponse
     * @param exception Excepción ocurrida
     * @param errorMessage Mensaje de error para el usuario
     * @param errorCode Código de error
     * @throws IOException Si hay error al escribir la respuesta
     */
    public void writeValidationErrorResponse(
            HttpServletResponse response,
            Exception exception,
            String errorMessage,
            String errorCode) throws IOException {

        log.debug("Construyendo respuesta de error de validación - Código: {}", errorCode);

        Response<Object> errorResponse = buildValidationErrorResponse(errorMessage, errorCode);
        
        response.setContentType(CONTENT_TYPE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(jsonUtils.serializeToJson(errorResponse));
        
        log.warn("Token JWT inválido - Código: {} - Mensaje técnico: {} - Mensaje usuario: {}", 
                errorCode, exception.getMessage(), errorMessage);
    }

    /**
     * Agrega token JWT al header de respuesta.
     *
     * @param response HttpServletResponse
     * @param token Token JWT
     */
    public void addTokenToHeader(HttpServletResponse response, String token) {
        response.addHeader(HEADER_AUTHORIZATION, PREFIX_TOKEN + token);
        log.debug("Token agregado al header Authorization");
    }

    /**
     * Construye el body de respuesta exitosa.
     *
     * @param token Token JWT
     * @param userName Nombre del usuario
     * @return Map con datos de respuesta
     */
    private Map<String, Object> buildSuccessBody(String token, String userName) {
        Map<String, Object> body = new HashMap<>();
        body.put(FIELD_TOKEN, token);
        body.put(FIELD_USERNAME, userName);
        body.put(FIELD_MESSAGE, String.format(SUCCESS_MESSAGE_TEMPLATE, userName));
        body.put(FIELD_EXPIRES_IN, EXPIRATION_TIME);
        return body;
    }

    /**
     * Construye respuesta de error de autenticación fallida.
     *
     * @param failed Excepción de autenticación
     * @return Response con información del error
     */
    private Response<Object> buildErrorResponse(AuthenticationException failed) {
        return Response.builder()
                .failure(true)
                .code(HttpStatus.UNAUTHORIZED.value())
                .errorCode(CODE_AUTHENTICATION_FAILED)
                .message( ERROR_AUTHENTICATION_FAILED)
                .timestamp(String.valueOf(System.currentTimeMillis()))
                .build();
    }

    /**
     * Construye respuesta de error de validación JWT.
     *
     * @param errorMessage Mensaje de error para el usuario
     * @param errorCode Código de error
     * @return Response con información del error
     */
    private Response<Object> buildValidationErrorResponse(String errorMessage, String errorCode) {
        return Response.builder()
                .failure(true)
                .code(HttpStatus.UNAUTHORIZED.value())
                .errorCode(errorCode)
                .message(errorMessage)
                .timestamp(String.valueOf(System.currentTimeMillis()))
                .build();
    }
}

