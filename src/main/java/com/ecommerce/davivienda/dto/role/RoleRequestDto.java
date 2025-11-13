package com.ecommerce.davivienda.dto.role;

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
 * DTO para la solicitud de creación o actualización de roles.
 * Contiene los datos necesarios para crear o modificar un rol.
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
public class RoleRequestDto {

    /**
     * Nombre del rol.
     * Ejemplo: "Administrador", "Cliente", "Vendedor"
     */
    @NotBlank(message = "El nombre del rol es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    @JsonProperty("nombre")
    private String nombre;
}

