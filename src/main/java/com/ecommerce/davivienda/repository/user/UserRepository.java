package com.ecommerce.davivienda.repository.user;

import com.ecommerce.davivienda.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para operaciones CRUD sobre la entidad User.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Busca un usuario por su correo electrónico (credenciales).
     * Utilizado principalmente para autenticación.
     *
     * @param correo Correo electrónico del usuario
     * @return Optional con el usuario encontrado, o vacío si no existe
     */
    Optional<User> findByCredenciales_Correo(String correo);

    /**
     * Verifica si existe un usuario con el correo especificado.
     *
     * @param correo Correo electrónico a verificar
     * @return true si existe un usuario con ese correo, false en caso contrario
     */
    boolean existsByCredenciales_Correo(String correo);

    /**
     * Busca un usuario por tipo de documento y número de documento.
     *
     * @param documentoId ID del tipo de documento
     * @param numeroDeDoc Número de documento
     * @return Optional con el usuario encontrado, o vacío si no existe
     */
    Optional<User> findByDocumentType_DocumentoIdAndNumeroDeDoc(Integer documentoId, String numeroDeDoc);

    /**
     * Busca un usuario por su userRoleId (ID del rol de usuario primario).
     *
     * @param usuarioRolId ID del rol de usuario
     * @return Optional con el usuario encontrado, o vacío si no existe
     */
    Optional<User> findByUsuarioRolId(Integer usuarioRolId);
}

