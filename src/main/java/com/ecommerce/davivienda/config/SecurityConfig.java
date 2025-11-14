package com.ecommerce.davivienda.config;

import com.ecommerce.davivienda.mapper.user.UserProfileMapper;
import com.ecommerce.davivienda.repository.user.UserRepository;
import com.ecommerce.davivienda.security.SecurityEndpoints;
import com.ecommerce.davivienda.security.credentials.CredentialsExtractor;
import com.ecommerce.davivienda.security.filter.JwtAuthenticationFilter;
import com.ecommerce.davivienda.security.filter.JwtValidationFilter;
import com.ecommerce.davivienda.security.response.AuthenticationResponseBuilder;
import com.ecommerce.davivienda.security.token.JwtTokenExtractor;
import com.ecommerce.davivienda.security.token.JwtTokenGenerator;
import com.ecommerce.davivienda.security.token.JwtTokenValidator;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.ecommerce.davivienda.constants.ConstantsSecurity.*;
import static com.ecommerce.davivienda.security.SecurityEndpoints.*;

/**
 * Configuración de seguridad de Spring Security con autenticación JWT.
 * Define las políticas de acceso, filtros de autenticación y configuración CORS.
 * 
 * <p><b>⚠️ Constantes centralizadas:</b></p>
 * <ul>
 *   <li>Endpoints públicos: Importados desde {@link SecurityEndpoints}</li>
 *   <li>Mensajes de error de seguridad: Importados desde {@link com.ecommerce.davivienda.constants.ConstantsSecurity}</li>
 *   <li>Configuraciones CORS: Definidas localmente en esta clase (específicas de configuración)</li>
 * </ul>
 *
 * @author Team Tienda Digital
 * @since 1.0.0
 */
@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    // ==================== CORS CONFIGURATION ====================
    
    private static final List<String> ALLOWED_ORIGINS = Arrays.asList(
            "http://localhost:3000",
            "http://localhost:4200"
    );
    
    private static final List<String> ALLOWED_METHODS = Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "PATCH"
    );
    
    private static final List<String> ALLOWED_HEADERS = Arrays.asList(
            "Authorization", "Content-Type", "X-Requested-With"
    );
    
    private static final String CORS_PATH_PATTERN = "/**";
    private static final String CORS_FILTER_PATTERN = "/*";

    private final AuthenticationConfiguration authenticationConfiguration;
    private final CredentialsExtractor credentialsExtractor;
    private final JwtTokenGenerator tokenGenerator;
    private final JwtTokenExtractor tokenExtractor;
    private final JwtTokenValidator tokenValidator;
    private final AuthenticationResponseBuilder responseBuilder;
    private final UserRepository userRepository;
    private final UserProfileMapper userProfileMapper;

    /**
     * Bean de AuthenticationManager para procesar autenticaciones.
     *
     * @return AuthenticationManager configurado
     * @throws Exception si hay error en configuración
     */
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Bean de PasswordEncoder para encriptar contraseñas con BCrypt.
     *
     * @return PasswordEncoder BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Crea y configura el filtro de autenticación JWT.
     * Establece el endpoint de login usando {@link JwtAuthenticationFilter#setFilterProcessesUrl(String)}.
     *
     * @return JwtAuthenticationFilter configurado
     * @throws Exception si hay error al obtener AuthenticationManager
     */
    private JwtAuthenticationFilter createJwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(
                authenticationManager(),
                credentialsExtractor,
                tokenGenerator,
                responseBuilder,
                userRepository,
                userProfileMapper
        );
        filter.setFilterProcessesUrl(JwtAuthenticationFilter.LOGIN_ENDPOINT);
        return filter;
    }

    /**
     * Crea y configura el filtro de validación JWT.
     * Inyecta las capacidades especializadas para validar tokens JWT.
     *
     * @return JwtValidationFilter configurado
     * @throws Exception si hay error al obtener AuthenticationManager
     */
    private JwtValidationFilter createJwtValidationFilter() throws Exception {
        return new JwtValidationFilter(
                authenticationManager(),
                tokenExtractor,
                tokenValidator,
                responseBuilder
        );
    }

    /**
     * Configura la cadena de filtros de seguridad con JWT.
     * Define endpoints públicos, protegidos y agrega filtros JWT.
     *
     * @param http HttpSecurity
     * @return SecurityFilterChain configurado
     * @throws Exception si hay error en configuración
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(this::configureAuthorization)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> 
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(this::configureExceptionHandling)
                .addFilter(createJwtAuthenticationFilter())
                .addFilter(createJwtValidationFilter())
                .build();
    }

    /**
     * Configura las reglas de autorización para endpoints públicos y protegidos.
     * 
     * Esta configuración es el ÚNICO lugar donde se definen endpoints públicos.
     * JwtValidationFilter NO duplica esta lógica, solo valida tokens JWT cuando están presentes.
     *
     * @param authz AuthorizeHttpRequestsConfigurer para configurar endpoints
     */
    private void configureAuthorization(
            org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authz) {
        authz
                // Endpoints públicos - NO requieren autenticación
                .requestMatchers(ENDPOINT_AUTH).permitAll()
                .requestMatchers(ENDPOINT_ACTUATOR).permitAll()
                .requestMatchers(ENDPOINT_DEBUG).permitAll()                          

                // Endpoints específicos de productos públicos
                .requestMatchers(ENDPOINT_PRODUCT_LIST_ACTIVE).permitAll()            
                .requestMatchers(ENDPOINT_PRODUCT_LIST_ALL).permitAll()               
                .requestMatchers(ENDPOINT_PRODUCT_SEARCH_PAGINATED).permitAll()       
                .requestMatchers(ENDPOINT_PRODUCT_SEARCH).permitAll()                 
                .requestMatchers(ENDPOINT_PRODUCT_GET_BY_ID).permitAll()             

                .requestMatchers(ENDPOINT_CATEGORIES).permitAll()
                .requestMatchers(ENDPOINT_DOCUMENT_TYPES).permitAll()

                // Endpoints específicos de usuarios públicos
                .requestMatchers(ENDPOINT_USERS_CREATE).permitAll()
                .requestMatchers(ENDPOINT_USERS_CHANGE_PASSWORD).permitAll()

                
                .anyRequest().authenticated();
    }

    /**
     * Configura el manejo de excepciones de seguridad.
     * Define handlers para acceso denegado y falta de autenticación.
     *
     * @param exception ExceptionHandlingConfigurer para configurar handlers
     */
    private void configureExceptionHandling(
            org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer<HttpSecurity> exception) {
        exception
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    String userName = extractUserNameFromRequest(request);
                    log.warn("🚫 Acceso denegado: {} intentó acceder a {}", userName, request.getRequestURI());
                    writeErrorResponse(response, HttpStatus.FORBIDDEN, ERROR_ACCESS_DENIED_MESSAGE);
                })
                .authenticationEntryPoint((request, response, authException) -> {
                    log.warn("🔒 No autenticado: intento de acceso a {}", request.getRequestURI());
                    writeErrorResponse(response, HttpStatus.UNAUTHORIZED, ERROR_UNAUTHENTICATED_MESSAGE);
                });
    }

    /**
     * Extrae el nombre de usuario del request.
     *
     * @param request HttpServletRequest
     * @return Nombre de usuario o "Usuario desconocido"
     */
    private String extractUserNameFromRequest(jakarta.servlet.http.HttpServletRequest request) {
        return request.getUserPrincipal() != null 
                ? request.getUserPrincipal().getName() 
                : ERROR_UNKNOWN_USER;
    }

    /**
     * Escribe una respuesta de error JSON en el HttpServletResponse.
     *
     * @param response HttpServletResponse donde escribir la respuesta
     * @param status HttpStatus código de estado HTTP
     * @param message Mensaje de error
     * @throws IOException si hay error al escribir la respuesta
     */
    private void writeErrorResponse(HttpServletResponse response, HttpStatus status, String message) 
            throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        
        String jsonResponse = String.format(
                JSON_ERROR_TEMPLATE,
                status.value(),
                message,
                System.currentTimeMillis()
        );
        
        response.getWriter().write(jsonResponse);
    }

    /**
     * Configuración de CORS para permitir peticiones cross-origin.
     *
     * @return CorsConfigurationSource configurado
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = buildCorsConfiguration();
        return registerCorsConfiguration(corsConfiguration);
    }

    /**
     * Construye la configuración CORS con orígenes, métodos y headers permitidos.
     *
     * @return CorsConfiguration configurada
     */
    private CorsConfiguration buildCorsConfiguration() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(ALLOWED_ORIGINS);
        corsConfiguration.setAllowedMethods(ALLOWED_METHODS);
        corsConfiguration.setAllowedHeaders(ALLOWED_HEADERS);
        corsConfiguration.setAllowCredentials(true);
        return corsConfiguration;
    }

    /**
     * Registra la configuración CORS para todos los endpoints.
     *
     * @param corsConfiguration Configuración CORS a registrar
     * @return UrlBasedCorsConfigurationSource configurado
     */
    private UrlBasedCorsConfigurationSource registerCorsConfiguration(CorsConfiguration corsConfiguration) {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(CORS_PATH_PATTERN, corsConfiguration);
        return source;
    }

    /**
     * Bean de CorsFilter con máxima prioridad.
     * Este filtro se ejecuta ANTES de la cadena de seguridad.
     *
     * @return FilterRegistrationBean configurado
     */
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilterRegistrationBean() {
        FilterRegistrationBean<CorsFilter> filterRegistrationBean = 
                new FilterRegistrationBean<>(new CorsFilter(corsConfigurationSource()));
        filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        filterRegistrationBean.addUrlPatterns(CORS_FILTER_PATTERN);
        return filterRegistrationBean;
    }
}

