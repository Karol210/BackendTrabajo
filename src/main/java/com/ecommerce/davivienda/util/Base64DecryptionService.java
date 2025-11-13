package com.ecommerce.davivienda.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Servicio utilitario para desencriptación de datos en Base64.
 * Proporciona métodos para decodificar strings codificados en Base64.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Component
public class Base64DecryptionService {

    /**
     * Desencripta un string codificado en Base64.
     *
     * @param encodedData String codificado en Base64
     * @return String desencriptado
     * @throws IllegalArgumentException Si el dato no es un Base64 válido
     */
    public String decrypt(String encodedData) {
        if (encodedData == null || encodedData.trim().isEmpty()) {
            log.warn("Intento de desencriptar dato nulo o vacío");
            throw new IllegalArgumentException("El dato a desencriptar no puede estar vacío");
        }

        try {
            log.debug("Desencriptando dato Base64 de longitud: {}", encodedData.length());
            
            byte[] decodedBytes = Base64.getDecoder().decode(encodedData);
            String decryptedData = new String(decodedBytes, StandardCharsets.UTF_8);
            
            log.debug("Dato desencriptado exitosamente");
            return decryptedData;
            
        } catch (IllegalArgumentException e) {
            log.error("Error al desencriptar Base64: dato inválido - {}", e.getMessage());
            throw new IllegalArgumentException("El dato proporcionado no es un Base64 válido", e);
        }
    }

    /**
     * Encripta un string a Base64.
     * Método auxiliar para testing o casos de uso específicos.
     *
     * @param data String a encriptar
     * @return String codificado en Base64
     */
    public String encrypt(String data) {
        if (data == null || data.trim().isEmpty()) {
            log.warn("Intento de encriptar dato nulo o vacío");
            throw new IllegalArgumentException("El dato a encriptar no puede estar vacío");
        }

        log.debug("Encriptando dato de longitud: {}", data.length());
        
        byte[] encodedBytes = Base64.getEncoder().encode(data.getBytes(StandardCharsets.UTF_8));
        String encryptedData = new String(encodedBytes, StandardCharsets.UTF_8);
        
        log.debug("Dato encriptado exitosamente");
        return encryptedData;
    }

    /**
     * Verifica si un string es un Base64 válido.
     *
     * @param encodedData String a verificar
     * @return true si es Base64 válido, false en caso contrario
     */
    public boolean isValidBase64(String encodedData) {
        if (encodedData == null || encodedData.trim().isEmpty()) {
            return false;
        }

        try {
            Base64.getDecoder().decode(encodedData);
            return true;
        } catch (IllegalArgumentException e) {
            log.debug("String no es Base64 válido: {}", e.getMessage());
            return false;
        }
    }
}

