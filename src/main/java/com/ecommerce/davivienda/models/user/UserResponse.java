package com.ecommerce.davivienda.models.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Modelo de respuesta para operaciones de consulta de usuarios.
 * Contiene los datos básicos de un usuario sin información sensible.
 * 
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    /**
     * ID del usuario.
     */
    private Integer usuarioId;

    /**
     * Nombre del usuario.
     */
    private String nombre;

    /**
     * Apellido del usuario.
     */
    private String apellido;

    /**
     * Tipo de documento.
     */
    private String documentType;

    /**
     * Número de documento.
     */
    private String documentNumber;

    /**
     * Correo electrónico.
     */
    private String email;

    /**
     * Estado del usuario (Activo, Inactivo, etc.).
     */
    private String status;

    /**
     * Roles asignados al usuario.
     */
    private List<String> roles;

    /**
     * ID de la relación usuario-rol principal.
     */
    private Integer usuarioRolId;

}

