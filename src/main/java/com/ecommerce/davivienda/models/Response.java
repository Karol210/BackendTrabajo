package com.ecommerce.davivienda.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Modelo genérico de respuesta para todas las APIs.
 * Proporciona una estructura estándar para respuestas exitosas y fallidas.
 *
 * @param <T> Tipo del cuerpo de la respuesta
 * @author Team Tienda Digital
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> {

    /**
     * Indica si la operación falló.
     */
    @JsonProperty("failure")
    private Boolean failure;

    /**
     * Código HTTP de la respuesta.
     */
    @JsonProperty("code")
    private Integer code;

    /**
     * Código de error de la respuesta.
     */
    @JsonProperty("errorCode")
    private String errorCode;

    /**
     * Mensaje descriptivo de la respuesta.
     */
    @JsonProperty("message")
    private String message;

    /**
     * Cuerpo de la respuesta con los datos solicitados.
     */
    @JsonProperty("body")
    private T body;

    /**
     * Timestamp de la respuesta en milisegundos.
     */
    @JsonProperty("timestamp")
    private String timestamp;

    /**
     * Crea una respuesta exitosa con datos.
     *
     * @param body Cuerpo de la respuesta
     * @param <T> Tipo del cuerpo
     * @return Response exitoso
     */
    public static <T> Response<T> success(T body) {
        return Response.<T>builder()
                .failure(false)
                .code(200)
                .body(body)
                .timestamp(String.valueOf(System.currentTimeMillis()))
                .build();
    }

    /**
     * Crea una respuesta de error.
     *
     * @param code Código HTTP
     * @param message Mensaje de error
     * @param <T> Tipo del cuerpo
     * @return Response de error
     */
    public static <T> Response<T> error(Integer code, String message) {
        return Response.<T>builder()
                .failure(true)
                .code(code)
                .message(message)
                .timestamp(String.valueOf(System.currentTimeMillis()))
                .build();
    }
}

