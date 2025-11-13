package com.ecommerce.davivienda.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Utilidad transversal para operaciones de serialización y deserialización JSON.
 * Proporciona métodos reutilizables para trabajar con ObjectMapper en toda la aplicación.
 *
 * @author Team Tienda Digital
 * @since 1.0.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JsonUtils {

    private final ObjectMapper objectMapper;

    /**
     * Serializa un objeto a JSON string.
     *
     * @param object Objeto a serializar
     * @return JSON string representando el objeto
     * @throws JsonProcessingException Si hay error en la serialización
     */
    public String serializeToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            log.warn("Intento de serializar objeto nulo");
            return "{}";
        }
        
        log.debug("Serializando objeto de tipo: {}", object.getClass().getSimpleName());
        return objectMapper.writeValueAsString(object);
    }

    /**
     * Deserializa un JSON string a un objeto del tipo especificado.
     *
     * @param json JSON string
     * @param valueType Clase del objeto destino
     * @param <T> Tipo del objeto
     * @return Objeto deserializado
     * @throws JsonProcessingException Si hay error en la deserialización
     */
    public <T> T deserializeFromJson(String json, Class<T> valueType) throws JsonProcessingException {
        if (json == null || json.trim().isEmpty()) {
            log.warn("Intento de deserializar JSON vacío");
            return null;
        }
        
        log.debug("Deserializando JSON a tipo: {}", valueType.getSimpleName());
        return objectMapper.readValue(json, valueType);
    }
}

