package com.ecommerce.davivienda.entity.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa los roles de usuario en el sistema.
 * Mapea la tabla 'roles' en la base de datos.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Role {

    /**
     * Identificador único del rol.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rol_id")
    private Integer rolId;

    /**
     * Nombre del rol.
     * Ejemplo: "Administrador", "Cliente", "Vendedor"
     */
    @Column(name = "nombre", nullable = false, unique = true, length = 100)
    private String nombreRol;
}

