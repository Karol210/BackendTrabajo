package com.ecommerce.davivienda.models.user;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request para la solicitud de creación de usuarios.
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
public class UserRequest {

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
     * Nombre o código del tipo de documento.
     * Ejemplos: "Cédula de Ciudadanía", "CC", "Pasaporte", "PA"
     * Si se envía documentTypeId, este campo es opcional.
     */
    @JsonProperty("documentType")
    private String documentType;

    /**
     * ID del tipo de documento (formato antiguo - DEPRECADO).
     * Se mantiene por compatibilidad. Usar documentType cuando sea posible.
     */
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
     * Lista de nombres de roles del usuario.
     * Ejemplos: ["Administrador"], ["Cliente"], ["Cliente", "Vendedor"]
     * Si se envía roleIds, este campo es opcional.
     */
    @JsonProperty("roles")
    private List<String> roles;

    /**
     * Lista de IDs de roles del usuario (formato antiguo - DEPRECADO).
     * Se mantiene por compatibilidad. Usar roles cuando sea posible.
     */
    @JsonProperty("roleIds")
    private List<Integer> roleIds;

    /**
     * Nombre del estado del usuario.
     * Ejemplos: "Activo", "Inactivo", "Suspendido", "Bloqueado"
     * Opcional en creación (se asigna "Activo" por defecto).
     * Si se envía statusId, este campo es opcional.
     */
    @JsonProperty("status")
    private String status;

    /**
     * ID del estado del usuario (formato antiguo - DEPRECADO).
     * Se mantiene por compatibilidad. Usar status cuando sea posible.
     */
    @JsonProperty("statusId")
    private Integer statusId;
}

