package com.ecommerce.davivienda.security;

/**
 * Constantes centralizadas de endpoints de seguridad.
 * 
 * <p><b>⚠️ ÚNICA FUENTE DE VERDAD para endpoints públicos</b></p>
 * <p>Esta clase define todos los endpoints que NO requieren autenticación.
 * Es utilizada por:</p>
 * <ul>
 *   <li>{@code SecurityConfig}: Para configurar qué endpoints son públicos</li>
 *   <li>{@code JwtValidationFilter}: Para omitir validación JWT en endpoints públicos</li>
 * </ul>
 * 
 * <p><b>Razón de existencia:</b></p>
 * <p>Endpoints públicos deben funcionar incluso si el cliente envía un token JWT 
 * inválido o expirado. Sin esta lista, el filtro JWT retornaría error 401 
 * al intentar validar tokens inválidos en endpoints públicos.</p>
 *
 * @author Team Tienda Digital
 * @since 1.0.0
 */
public final class SecurityEndpoints {

    // Constructor privado para evitar instanciación
    private SecurityEndpoints() {
        throw new UnsupportedOperationException("Clase de constantes - no instanciable");
    }

    // ==================== AUTHENTICATION ENDPOINTS ====================
    
    /**
     * Endpoints de autenticación (login, registro, etc.)
     */
    public static final String ENDPOINT_AUTH = "/api/v1/auth/**";
    
    // ==================== MONITORING ENDPOINTS ====================
    
    /**
     * Endpoints de monitoreo (actuator)
     */
    public static final String ENDPOINT_ACTUATOR = "/actuator/**";
    
    /**
     * Endpoints de debug - ⚠️ TEMPORAL - Eliminar en producción
     */
    public static final String ENDPOINT_DEBUG = "/api/v1/debug/**";
    
    // ==================== PRODUCT ENDPOINTS ====================
    
    /**
     * Listar todos los productos activos
     */
    public static final String ENDPOINT_PRODUCT_LIST_ACTIVE = "/api/v1/products/list-active";
    
    /**
     * Listar todos los productos (públicos y activos)
     */
    public static final String ENDPOINT_PRODUCT_LIST_ALL = "/api/v1/products/list-all";
    
    /**
     * Buscar productos con paginación
     */
    public static final String ENDPOINT_PRODUCT_SEARCH_PAGINATED = "/api/v1/products/search/paginated";
    
    /**
     * Buscar productos
     */
    public static final String ENDPOINT_PRODUCT_SEARCH = "/api/v1/products/search";
    
    /**
     * Obtener producto por ID
     */
    public static final String ENDPOINT_PRODUCT_GET_BY_ID = "/api/v1/products/get-by-id/**";
    
    // ==================== USER ENDPOINTS ====================
    
    /**
     * Endpoint público de creación de usuarios (registro)
     */
    public static final String ENDPOINT_USERS_CREATE = "/api/v1/users/create";
    
    /**
     * Endpoint público de recuperación de contraseña
     */
    public static final String ENDPOINT_USERS_CHANGE_PASSWORD = "/api/v1/users/change-password";
    
    // ==================== DOCUMENT TYPE ENDPOINTS ====================
    
    /**
     * Endpoints de tipos de documento
     */
    public static final String ENDPOINT_DOCUMENT_TYPES = "/api/v1/document-types/**";
    
    // ==================== CATEGORY ENDPOINTS ====================
    
    /**
     * Endpoints de categorías
     */
    public static final String ENDPOINT_CATEGORIES = "/api/v1/categories/**";
    
    // ==================== PUBLIC ENDPOINTS ARRAY ====================
    
    /**
     * Array de todos los endpoints públicos para uso en filtros.
     * Incluye todos los endpoints que NO requieren autenticación.
     * 
     * <p><b>Uso en JwtValidationFilter:</b></p>
     * <p>Estos endpoints omiten la validación JWT completamente, permitiendo
     * que funcionen incluso si el cliente envía un token inválido o expirado.</p>
     */
    public static final String[] PUBLIC_ENDPOINTS = {
        ENDPOINT_AUTH,
        ENDPOINT_ACTUATOR,
        ENDPOINT_DEBUG,
        ENDPOINT_PRODUCT_LIST_ACTIVE,
        ENDPOINT_PRODUCT_LIST_ALL,
        ENDPOINT_PRODUCT_SEARCH_PAGINATED,
        ENDPOINT_PRODUCT_SEARCH,
        ENDPOINT_PRODUCT_GET_BY_ID,
        ENDPOINT_USERS_CREATE,
        ENDPOINT_USERS_CHANGE_PASSWORD,
        ENDPOINT_DOCUMENT_TYPES,
        ENDPOINT_CATEGORIES
    };
}

