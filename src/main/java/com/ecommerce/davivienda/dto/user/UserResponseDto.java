package com.ecommerce.davivienda.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para la respuesta de operaciones sobre usuarios.
 * Contiene los datos completos del usuario excepto la contraseña.
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
public class UserResponseDto {

    /**
     * ID del usuario.
     */
    @JsonProperty("id")
    private Integer id;

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
     * Tipo de documento (código).
     */
    @JsonProperty("documentType")
    private String documentType;

    /**
     * Número de documento del usuario.
     */
    @JsonProperty("documentNumber")
    private String documentNumber;

    /**
     * Correo electrónico del usuario.
     */
    @JsonProperty("email")
    private String email;

    /**
     * ID de la relación usuario-rol principal.
     */
    @JsonProperty("usuarioRolId")
    private Integer usuarioRolId;

    /**
     * Lista de roles del usuario.
     */
    @JsonProperty("roles")
    private java.util.List<String> roles;

    /**
     * Estado del usuario (Activo, Inactivo, etc.).
     */
    @JsonProperty("status")
    private String status;

    /**
     * Fecha de creación del usuario.
     */
    @JsonProperty("createdAt")
    private LocalDateTime createdAt;

    /**
     * Verifica si el usuario está activo.
     *
     * @return true si el estado es "Activo", false en caso contrario
     */
    public boolean isActive() {
        return "Activo".equalsIgnoreCase(status);
    }
}

