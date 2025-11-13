package com.ecommerce.davivienda.models.user;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request para la solicitud de actualización parcial de usuarios.
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
public class UserUpdateRequest {

    /**
     * ID del usuario (OPCIONAL).
     * Si no se proporciona, se actualizará el usuario autenticado del token JWT.
     * Si se proporciona, se validará que el usuario tenga permisos para actualizar ese usuario.
     */
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
     * Nombre o código del tipo de documento (opcional).
     * Ejemplos: "Cédula de Ciudadanía", "CC", "Pasaporte", "PA"
     * Si es null, se mantiene el valor existente.
     * Si se envía documentTypeId, este campo es opcional.
     */
    @JsonProperty("documentType")
    private String documentType;

    /**
     * ID del tipo de documento (formato antiguo - DEPRECADO - opcional).
     * Se mantiene por compatibilidad. Usar documentType cuando sea posible.
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
     * Lista de nombres de roles del usuario (opcional).
     * Ejemplos: ["Administrador"], ["Cliente"], ["Cliente", "Vendedor"]
     * Si es null, se mantienen los roles existentes.
     * Si se envía roleIds, este campo es opcional.
     */
    @JsonProperty("roles")
    private java.util.List<String> roles;

    /**
     * Lista de IDs de roles del usuario (formato antiguo - DEPRECADO - opcional).
     * Se mantiene por compatibilidad. Usar roles cuando sea posible.
     * Si es null, se mantienen los roles existentes.
     */
    @JsonProperty("roleIds")
    private List<Integer> roleIds;

    /**
     * Nombre del estado del usuario (opcional).
     * Ejemplos: "Activo", "Inactivo", "Suspendido", "Bloqueado"
     * Si es null, se mantiene el estado existente.
     * Si se envía statusId, este campo es opcional.
     */
    @JsonProperty("status")
    private String status;

    /**
     * ID del estado del usuario (formato antiguo - DEPRECADO - opcional).
     * Se mantiene por compatibilidad. Usar status cuando sea posible.
     * Si es null, se mantiene el estado existente.
     */
    @JsonProperty("statusId")
    private Integer statusId;
}

