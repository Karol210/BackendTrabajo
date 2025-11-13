package com.ecommerce.davivienda.dto.document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la solicitud de creación o actualización de tipos de documento.
 * Contiene los datos necesarios para crear o modificar un tipo de documento.
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
public class DocumentTypeRequestDto {

    /**
     * Nombre descriptivo del tipo de documento.
     * Ejemplo: "Cédula de Ciudadanía", "Pasaporte"
     */
    @NotBlank(message = "El nombre del tipo de documento es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    @JsonProperty("nombre")
    private String nombre;

    /**
     * Código abreviado del tipo de documento.
     * Ejemplo: "CC", "PA", "CE"
     */
    @NotBlank(message = "El código del tipo de documento es obligatorio")
    @Size(max = 50, message = "El código no puede exceder 50 caracteres")
    @JsonProperty("codigo")
    private String codigo;
}

