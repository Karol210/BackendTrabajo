package com.ecommerce.davivienda.repository.user;

import com.ecommerce.davivienda.entity.user.Credentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para operaciones CRUD sobre la entidad Credentials.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Repository
public interface CredentialsRepository extends JpaRepository<Credentials, Integer> {

    /**
     * Busca credenciales por correo electrónico.
     *
     * @param correo Correo electrónico a buscar
     * @return Optional con las credenciales encontradas, o vacío si no existen
     */
    Optional<Credentials> findByCorreo(String correo);

    /**
     * Verifica si existe un correo electrónico registrado.
     *
     * @param correo Correo electrónico a verificar
     * @return true si el correo existe, false en caso contrario
     */
    boolean existsByCorreo(String correo);
}

