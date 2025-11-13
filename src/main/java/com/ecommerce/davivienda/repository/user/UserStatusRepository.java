package com.ecommerce.davivienda.repository.user;

import com.ecommerce.davivienda.entity.user.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para operaciones CRUD sobre la entidad UserStatus.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Repository
public interface UserStatusRepository extends JpaRepository<UserStatus, Integer> {

    /**
     * Busca un estado de usuario por su nombre.
     *
     * @param nombre Nombre del estado (ej: "Activo", "Inactivo")
     * @return Optional con el estado encontrado, o vacío si no existe
     */
    Optional<UserStatus> findByNombre(String nombre);

    /**
     * Verifica si existe un estado de usuario con el nombre especificado.
     *
     * @param nombre Nombre del estado a verificar
     * @return true si existe el estado, false en caso contrario
     */
    boolean existsByNombre(String nombre);
}

