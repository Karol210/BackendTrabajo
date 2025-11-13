package com.ecommerce.davivienda.entity.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa los estados de un usuario en el sistema.
 * Mapea la tabla 'estado_usuario' en la base de datos.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "estado_usuario")
public class UserStatus {

    /**
     * Identificador único del estado de usuario.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "estado_usuario_id")
    private Integer estadoUsuarioId;

    /**
     * Nombre del estado.
     * Ejemplo: "Activo", "Inactivo", "Suspendido", "Bloqueado"
     */
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;
}

