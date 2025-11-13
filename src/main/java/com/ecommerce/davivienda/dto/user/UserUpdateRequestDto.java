package com.ecommerce.davivienda.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la solicitud de actualización parcial de usuarios.
 * Solo el ID es obligatorio, todos los demás campos son opcionales.
 * Los campos que sean null no se actualizarán (se mantendrá el valor existente).
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
public class UserUpdateRequestDto {

    /**
     * ID del usuario (OBLIGATORIO para actualización).
     */
    @NotNull(message = "El ID del usuario es obligatorio")
    @JsonProperty("id")
    private Integer id;

    /**
     * Nombre del usuario (opcional).
     * Si es null, se mantiene el valor existente.
     */
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @JsonProperty("nombre")
    private String nombre;

    /**
     * Apellido del usuario (opcional).
     * Si es null, se mantiene el valor existente.
     */
    @Size(min = 2, max = 100, message = "El apellido debe tener entre 2 y 100 caracteres")
    @JsonProperty("apellido")
    private String apellido;

    /**
     * ID del tipo de documento (opcional).
     * Si es null, se mantiene el valor existente.
     */
    @JsonProperty("documentTypeId")
    private Integer documentTypeId;

    /**
     * Número de documento del usuario (opcional).
     * Si es null, se mantiene el valor existente.
     */
    @Size(min = 5, max = 50, message = "El número de documento debe tener entre 5 y 50 caracteres")
    @JsonProperty("documentNumber")
    private String documentNumber;

    /**
     * Correo electrónico del usuario (opcional).
     * Si es null, se mantiene el valor existente.
     */
    @Email(message = "El correo electrónico debe ser válido")
    @JsonProperty("email")
    private String email;

    /**
     * Lista de IDs de roles del usuario (opcional).
     * Si es null, se mantienen los roles existentes.
     */
    @JsonProperty("roleIds")
    private java.util.List<Integer> roleIds;

    /**
     * ID del estado del usuario (opcional).
     * Si es null, se mantiene el estado existente.
     */
    @JsonProperty("statusId")
    private Integer statusId;
}

