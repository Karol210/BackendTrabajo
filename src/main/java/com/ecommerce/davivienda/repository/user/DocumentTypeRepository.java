package com.ecommerce.davivienda.repository.user;

import com.ecommerce.davivienda.entity.user.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para operaciones CRUD sobre la entidad DocumentType.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Repository
public interface DocumentTypeRepository extends JpaRepository<DocumentType, Integer> {

    /**
     * Busca un tipo de documento por su código.
     *
     * @param codigo Código del tipo de documento (ej: "CC", "PA", "CE")
     * @return Optional con el tipo de documento encontrado, o vacío si no existe
     */
    Optional<DocumentType> findByCodigo(String codigo);

    /**
     * Busca un tipo de documento por su nombre.
     *
     * @param nombre Nombre del tipo de documento
     * @return Optional con el tipo de documento encontrado, o vacío si no existe
     */
    Optional<DocumentType> findByNombre(String nombre);

}

