package com.ecommerce.davivienda.dto.user;

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
 * DTO para la solicitud de cambio de contraseña de un usuario.
 * Identifica al usuario por su correo electrónico.
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
public class PasswordChangeRequestDto {

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
}

