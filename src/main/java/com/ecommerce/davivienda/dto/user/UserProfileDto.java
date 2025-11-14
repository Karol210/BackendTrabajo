package com.ecommerce.davivienda.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO para el perfil del usuario en respuestas de autenticación.
 * Contiene información del usuario sin datos sensibles (NO incluye contraseña ni IDs internos).
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserProfileDto {

    /**
     * Nombre del usuario.
     */
    @JsonProperty("nombre")
    private String nombre;

    /**
     * Apellido del usuario.
     */
    @JsonProperty("apellido")
    private String apellido;

    /**
     * Correo electrónico del usuario.
     */
    @JsonProperty("correo")
    private String correo;

    /**
     * Tipo de documento del usuario.
     */
    @JsonProperty("tipoDocumento")
    private String tipoDocumento;

    /**
     * Código del tipo de documento.
     */
    @JsonProperty("codigoDocumento")
    private String codigoDocumento;

    /**
     * Número de documento del usuario.
     */
    @JsonProperty("numeroDocumento")
    private String numeroDocumento;

    /**
     * Estado del usuario (Activo, Inactivo, etc.).
     */
    @JsonProperty("estado")
    private String estado;

    /**
     * Roles del usuario (solo nombres, sin IDs).
     */
    @JsonProperty("roles")
    private List<String> roles;
}

