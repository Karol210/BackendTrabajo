package com.ecommerce.davivienda.dto.stock;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la solicitud de validación de stock de un carrito de usuario.
 * Contiene la identificación del usuario mediante su tipo y número de documento.
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
public class StockValidationRequestDto {

    /**
     * Tipo de documento del usuario (CC, TI, CE, PA, NIT).
     */
    @NotBlank(message = "El tipo de documento es obligatorio")
    @JsonProperty("documentType")
    private String documentType;

    /**
     * Número de documento del usuario.
     */
    @NotBlank(message = "El número de documento es obligatorio")
    @JsonProperty("documentNumber")
    private String documentNumber;
}

