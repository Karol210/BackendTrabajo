package com.ecommerce.davivienda.exception;

import com.ecommerce.davivienda.constants.Constants;
import com.ecommerce.davivienda.exception.product.ProductException;
import com.ecommerce.davivienda.exception.user.UserException;
import com.ecommerce.davivienda.models.Response;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * Manejador global de excepciones para todos los controladores.
 * Captura y transforma excepciones en respuestas HTTP estandarizadas.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@RestControllerAdvice
public class ExceptionHandlerController {

    /**
     * Maneja excepciones de usuario personalizadas.
     *
     * @param e Excepción de usuario
     * @param request Request HTTP
     * @return Response con error de usuario
     */
    @ExceptionHandler({UserException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response<Object> handleUserException(UserException e, HttpServletRequest request) {
        log.error("UserException: URL={} | ErrorCode={} | Message={}", 
                request.getRequestURI(), e.getErrorCode(), e.getMessage());


        return Response.builder()
                .failure(true)
                .code(HttpStatus.BAD_REQUEST.value())
                .errorCode(e.getErrorCode())
                .message(e.getMessage())
                .timestamp(String.valueOf(System.currentTimeMillis()))
                .build();
    }

    /**
     * Maneja excepciones de producto personalizadas.
     *
     * @param e Excepción de producto
     * @param request Request HTTP
     * @return Response con error de producto
     */
    @ExceptionHandler({ProductException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response<Object> handleProductException(ProductException e, HttpServletRequest request) {
        log.error("ProductException: URL={} | ErrorCode={} | Message={}", 
                request.getRequestURI(), e.getErrorCode(), e.getMessage());

        return Response.builder()
                .failure(true)
                .code(HttpStatus.BAD_REQUEST.value())
                .errorCode(e.getErrorCode())
                .message(e.getMessage())
                .timestamp(String.valueOf(System.currentTimeMillis()))
                .build();
    }

    /**
     * Maneja excepciones de carrito personalizadas.
     *
     * @param e Excepción de carrito
     * @param request Request HTTP
     * @return Response con error de carrito
     */
    @ExceptionHandler({CartException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response<Object> handleCartException(CartException e, HttpServletRequest request) {
        log.error("CartException: URL={} | ErrorCode={} | Message={}", 
                request.getRequestURI(), e.getErrorCode(), e.getMessage());

        return Response.builder()
                .failure(true)
                .code(HttpStatus.BAD_REQUEST.value())
                .errorCode(e.getErrorCode())
                .message(e.getMessage())
                .timestamp(String.valueOf(System.currentTimeMillis()))
                .build();
    }

    /**
     * Maneja excepciones de validación de datos.
     *
     * @param e Excepción de validación
     * @param request Request HTTP
     * @return Response con errores de validación
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response<Object> handleValidationException(
            MethodArgumentNotValidException e, 
            HttpServletRequest request) {
        
        String errors = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        log.error("ValidationException: URL={} | Errors={}", request.getRequestURI(), errors);

        return Response.builder()
                .failure(true)
                .code(HttpStatus.BAD_REQUEST.value())
                .errorCode(Constants.CODE_VALIDATION_EXCEPTION)
                .message("Errores de validación: " + errors)
                .timestamp(String.valueOf(System.currentTimeMillis()))
                .build();
    }

    /**
     * Maneja excepciones de integridad de datos (duplicados, violación de constraints).
     *
     * @param e Excepción de integridad
     * @param request Request HTTP
     * @return Response con error de integridad
     */
    @ExceptionHandler({DataIntegrityViolationException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response<Object> handleDataIntegrityViolation(
            DataIntegrityViolationException e, 
            HttpServletRequest request) {
        
        log.error("DataIntegrityViolationException: URL={} | Message={}", 
                request.getRequestURI(), e.getMessage());

        String message = "Error de integridad de datos. Posible duplicado o violación de constraint.";

        return Response.builder()
                .failure(true)
                .code(HttpStatus.CONFLICT.value())
                .errorCode(Constants.CODE_DATA_INTEGRITY_VIOLATION)
                .message(message)
                .timestamp(String.valueOf(System.currentTimeMillis()))
                .build();
    }

    /**
     * Maneja excepciones de acceso denegado.
     *
     * @param e Excepción de acceso
     * @param request Request HTTP
     * @return Response con error de acceso
     */
    @ExceptionHandler({AccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Response<Object> handleAccessDeniedException(
            AccessDeniedException e, 
            HttpServletRequest request) {
        
        log.error("AccessDeniedException: URL={} | Message={}", 
                request.getRequestURI(), e.getMessage());

        return Response.builder()
                .failure(true)
                .code(HttpStatus.FORBIDDEN.value())
                .errorCode(Constants.CODE_ACCESS_DENIED)
                .message("Acceso denegado: no tiene permisos para esta operación")
                .timestamp(String.valueOf(System.currentTimeMillis()))
                .build();
    }

    /**
     * Maneja excepciones genéricas no capturadas.
     *
     * @param e Excepción genérica
     * @param request Request HTTP
     * @return Response con error interno
     */
    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response<Object> handleGenericException(Exception e, HttpServletRequest request) {
        log.error("Exception: URL={} | Message={} | Type={}", 
                request.getRequestURI(), e.getMessage(), e.getClass().getSimpleName(), e);

        return Response.builder()
                .failure(true)
                .errorCode(Constants.CODE_GENERIC_ERROR)
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Error interno del servidor")
                .timestamp(String.valueOf(System.currentTimeMillis()))
                .build();
    }
}

