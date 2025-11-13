package com.ecommerce.davivienda.entity.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad que representa un usuario del sistema.
 * Mapea la tabla 'usuarios' en la base de datos.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuarios", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"documento_id", "numero_de_doc"})
})
public class User {

    /**
     * Identificador único del usuario.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id")
    private Integer usuarioId;

    /**
     * Nombre del usuario.
     */
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    /**
     * Apellido del usuario.
     */
    @Column(name = "apellido", nullable = false, length = 100)
    private String apellido;

    /**
     * Tipo de documento del usuario.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "documento_id", nullable = false)
    private DocumentType documentType;

    /**
     * Número de documento del usuario.
     */
    @Column(name = "numero_de_doc", nullable = false, length = 50)
    private String numeroDeDoc;

    /**
     * Credenciales de acceso del usuario (correo y contraseña).
     */
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "credenciales_id", unique = true)
    private Credentials credenciales;

    /**
     * Estado actual del usuario (Activo, Inactivo, Suspendido, etc.).
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "estado_usuario_id")
    private UserStatus userStatus;

    /**
     * ID de la relación usuario-rol principal.
     * Este campo se actualiza automáticamente con el primer rol asignado.
     */
    @Column(name = "usuario_rol_id")
    private Integer usuarioRolId;

    /**
     * Relación con los roles del usuario.
     * Un usuario puede tener múltiples roles a través de la tabla usuario_rol.
     */
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "usuario_id")
    private java.util.List<UserRole> roles;

    /**
     * Fecha de creación del usuario.
     */
    @Column(name = "creation_date", nullable = false, updatable = false)
    private LocalDateTime creationDate;

    /**
     * Hook ejecutado antes de persistir la entidad.
     * Establece la fecha de creación si no está definida.
     */
    @PrePersist
    protected void onCreate() {
        if (creationDate == null) {
            creationDate = LocalDateTime.now();
        }
    }

    /**
     * Verifica si el usuario está activo.
     *
     * @return true si el estado del usuario es "Activo", false en caso contrario
     */
    public boolean isActive() {
        return userStatus != null && "Activo".equalsIgnoreCase(userStatus.getNombre());
    }

    /**
     * Obtiene el correo electrónico del usuario desde sus credenciales.
     *
     * @return Correo del usuario, o null si no tiene credenciales
     */
    public String getCorreo() {
        return credenciales != null ? credenciales.getCorreo() : null;
    }

    /**
     * Obtiene la contraseña del usuario desde sus credenciales.
     *
     * @return Contraseña del usuario, o null si no tiene credenciales
     */
    public String getContrasena() {
        return credenciales != null ? credenciales.getContrasena() : null;
    }
}

