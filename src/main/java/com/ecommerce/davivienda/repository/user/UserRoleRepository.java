package com.ecommerce.davivienda.repository.user;

import com.ecommerce.davivienda.entity.user.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para operaciones CRUD sobre la entidad UserRole.
 * Gestiona la relación muchos a muchos entre usuarios y roles.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {

    /**
     * Busca todas las relaciones usuario-rol para un usuario específico.
     *
     * @param usuarioId ID del usuario
     * @return Lista de relaciones usuario-rol
     */
    List<UserRole> findByUsuarioId(Integer usuarioId);

    /**
     * Busca una relación específica usuario-rol.
     *
     * @param usuarioId ID del usuario
     * @param rolId ID del rol
     * @return Optional con la relación encontrada, o vacío si no existe
     */
    Optional<UserRole> findByUsuarioIdAndRole_RolId(Integer usuarioId, Integer rolId);

    /**
     * Verifica si existe una relación usuario-rol específica.
     *
     * @param usuarioId ID del usuario
     * @param rolId ID del rol
     * @return true si existe la relación, false en caso contrario
     */
    boolean existsByUsuarioIdAndRole_RolId(Integer usuarioId, Integer rolId);
}

