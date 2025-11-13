package com.ecommerce.davivienda.dto.document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la respuesta de consultas de tipos de documento.
 * Contiene la información completa de un tipo de documento.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocumentTypeResponseDto {

    /**
     * Identificador único del tipo de documento.
     */
    @JsonProperty("documentoId")
    private Integer documentoId;

    /**
     * Nombre descriptivo del tipo de documento.
     * Ejemplo: "Cédula de Ciudadanía", "Pasaporte"
     */
    @JsonProperty("nombre")
    private String nombre;

    /**
     * Código abreviado del tipo de documento.
     * Ejemplo: "CC", "PA", "CE"
     */
    @JsonProperty("codigo")
    private String codigo;
}

