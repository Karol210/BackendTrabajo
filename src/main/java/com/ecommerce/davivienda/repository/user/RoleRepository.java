package com.ecommerce.davivienda.repository.user;

import com.ecommerce.davivienda.entity.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para operaciones CRUD sobre la entidad Role.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    /**
     * Busca un rol por su nombre.
     *
     * @param nombreRol Nombre del rol a buscar
     * @return Optional con el rol encontrado, o vacío si no existe
     */
    Optional<Role> findByNombreRol(String nombreRol);

    /**
     * Verifica si existe un rol con el nombre especificado.
     *
     * @param nombreRol Nombre del rol a verificar
     * @return true si existe el rol, false en caso contrario
     */
    boolean existsByNombreRol(String nombreRol);
}

