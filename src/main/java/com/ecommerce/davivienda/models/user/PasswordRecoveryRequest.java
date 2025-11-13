package com.ecommerce.davivienda.models.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request para la solicitud de recuperación de contraseña (público).
 * Envía un correo al usuario con instrucciones para cambiar su contraseña.
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
public class PasswordRecoveryRequest {

    /**
     * Correo electrónico del usuario.
     */
    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El correo electrónico debe ser válido")
    @JsonProperty("email")
    private String email;

    /**
     * Nueva contraseña del usuario.
     */
    @NotBlank(message = "La nueva contraseña es obligatoria")
    @JsonProperty("newPassword")
    private String newPassword;

    /**
     * Bandera que indica si se debe enviar correo de notificación.
     * Valor por defecto: true
     */
    @JsonProperty("envioCorreo")
    @Builder.Default
    private Boolean envioCorreo = true;
}

