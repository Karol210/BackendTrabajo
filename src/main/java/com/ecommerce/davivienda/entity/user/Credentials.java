package com.ecommerce.davivienda.entity.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa las credenciales de acceso de un usuario.
 * Mapea la tabla 'credenciales' en la base de datos.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "credenciales")
public class Credentials {

    /**
     * Identificador único de las credenciales.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "credenciales_id")
    private Integer credencialesId;

    /**
     * Correo electrónico del usuario (usado como username para autenticación).
     */
    @Column(name = "correo", nullable = false, unique = true)
    private String correo;

    /**
     * Contraseña hasheada del usuario.
     * Debe ser encriptada con BCrypt antes de almacenarla.
     */
    @Column(name = "contrasena", nullable = false)
    private String contrasena;
}

