package com.ecommerce.davivienda.entity.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa la relación entre usuarios y roles (tabla intermedia).
 * Mapea la tabla 'usuario_rol' en la base de datos.
 * Un usuario puede tener múltiples roles a través de esta tabla.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuario_rol", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"usuario_id", "rol_id"})
})
public class UserRole {

    /**
     * Identificador único de la relación usuario-rol.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_rol_id")
    private Integer usuarioRolId;

    /**
     * ID del usuario asociado.
     */
    @Column(name = "usuario_id", nullable = false)
    private Integer usuarioId;

    /**
     * Rol asociado al usuario.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rol_id", nullable = false)
    private Role role;
}

