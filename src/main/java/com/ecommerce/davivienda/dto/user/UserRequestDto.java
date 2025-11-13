package com.ecommerce.davivienda.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la solicitud de creación y actualización de usuarios.
 * Contiene los datos personales, credenciales, rol y estado del usuario.
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
public class UserRequestDto {

    /**
     * ID del usuario (solo para actualización).
     */
    @JsonProperty("id")
    private Integer id;

    /**
     * Nombre del usuario.
     */
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @JsonProperty("nombre")
    private String nombre;

    /**
     * Apellido del usuario.
     */
    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 100, message = "El apellido debe tener entre 2 y 100 caracteres")
    @JsonProperty("apellido")
    private String apellido;

    /**
     * ID del tipo de documento.
     */
    @NotNull(message = "El tipo de documento es obligatorio")
    @JsonProperty("documentTypeId")
    private Integer documentTypeId;

    /**
     * Número de documento del usuario.
     */
    @NotBlank(message = "El número de documento es obligatorio")
    @Size(min = 5, max = 50, message = "El número de documento debe tener entre 5 y 50 caracteres")
    @JsonProperty("documentNumber")
    private String documentNumber;

    /**
     * Correo electrónico del usuario (username para autenticación).
     */
    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El correo electrónico debe ser válido")
    @JsonProperty("email")
    private String email;

    /**
     * Contraseña del usuario (solo para creación y cambio de contraseña).
     */
    @JsonProperty("password")
    private String password;

    /**
     * Lista de IDs de roles del usuario.
     * Un usuario puede tener múltiples roles (ej: Administrador + Cliente).
     */
    @NotNull(message = "Los roles son obligatorios")
    @JsonProperty("roleIds")
    private java.util.List<Integer> roleIds;

    /**
     * ID del estado del usuario (Activo, Inactivo, etc.).
     * Opcional en creación (se asigna "Activo" por defecto).
     */
    @JsonProperty("statusId")
    private Integer statusId;
}

