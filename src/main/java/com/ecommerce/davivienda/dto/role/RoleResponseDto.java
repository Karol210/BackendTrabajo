package com.ecommerce.davivienda.dto.role;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la respuesta de consultas de roles.
 * Contiene la información completa de un rol.
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
public class RoleResponseDto {

    /**
     * Identificador único del rol.
     */
    @JsonProperty("rolId")
    private Integer rolId;

    /**
     * Nombre del rol.
     * Ejemplo: "Administrador", "Cliente", "Vendedor"
     */
    @JsonProperty("nombre")
    private String nombre;
}

