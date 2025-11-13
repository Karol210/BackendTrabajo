package com.ecommerce.davivienda.constants;

/**
 * Clase de constantes de seguridad para mensajes de error y templates JSON.
 * Define mensajes de error y templates relacionados con seguridad y autenticación.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public class ConstantsSecurity {

    // ==================== SECURITY - ERROR MESSAGES ====================
    
    /**
     * Mensaje de error cuando el usuario no tiene permisos suficientes.
     * Se muestra cuando se intenta acceder a un recurso sin los permisos necesarios.
     */
    public static final String ERROR_ACCESS_DENIED_MESSAGE = 
            "No tienes permisos suficientes para ejecutar esta acción";
    
    /**
     * Mensaje de error cuando el usuario no está autenticado.
     * Se muestra cuando se intenta acceder a un recurso protegido sin autenticación.
     */
    public static final String ERROR_UNAUTHENTICATED_MESSAGE = 
            "No estás autenticado. Por favor, inicia sesión.";
    
    /**
     * Mensaje por defecto cuando no se puede identificar al usuario.
     * Se usa como valor de respaldo cuando no hay información de usuario disponible.
     */
    public static final String ERROR_UNKNOWN_USER = "Usuario desconocido";
    
    // ==================== SECURITY - JSON TEMPLATES ====================
    
    /**
     * Template JSON para respuestas de error de seguridad.
     * Formato: {"failure":true,"code":%d,"message":"%s","timestamp":"%d"}
     * 
     * <p>Parámetros esperados:</p>
     * <ul>
     *   <li>%d (int): Código HTTP del error</li>
     *   <li>%s (String): Mensaje descriptivo del error</li>
     *   <li>%d (long): Timestamp en milisegundos</li>
     * </ul>
     */
    public static final String JSON_ERROR_TEMPLATE = 
            "{\"failure\":true,\"code\":%d,\"message\":\"%s\",\"timestamp\":\"%d\"}";

    /**
     * Constructor privado para evitar instanciación.
     * Esta clase solo contiene constantes estáticas y no debe ser instanciada.
     */
    private ConstantsSecurity() {
        throw new IllegalStateException("Utility class - No se puede instanciar");
    }
}

