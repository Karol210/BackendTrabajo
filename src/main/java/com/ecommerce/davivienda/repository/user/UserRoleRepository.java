package com.ecommerce.davivienda.repository.user;

import com.ecommerce.davivienda.entity.user.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

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
     * Elimina todas las relaciones usuario-rol para un usuario específico.
     *
     * @param usuarioId ID del usuario
     */
    void deleteByUsuarioId(Integer usuarioId);

}

