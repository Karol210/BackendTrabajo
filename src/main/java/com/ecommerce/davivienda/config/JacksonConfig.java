package com.ecommerce.davivienda.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * Configuración de Jackson para serialización/deserialización JSON.
 * Define el comportamiento global de ObjectMapper.
 *
 * @author Team Tienda Digital
 * @since 1.0.0
 */
@Configuration
public class JacksonConfig {

    /**
     * Configura ObjectMapper global para la aplicación.
     *
     * @param builder Builder de Jackson
     * @return ObjectMapper configurado
     */
    @Bean
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.build();
        
        // Registrar módulo para Java 8 Date/Time API
        objectMapper.registerModule(new JavaTimeModule());
        
        // Deshabilitar serialización de fechas como timestamps
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        
        // Configuraciones adicionales
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        
        return objectMapper;
    }
}

