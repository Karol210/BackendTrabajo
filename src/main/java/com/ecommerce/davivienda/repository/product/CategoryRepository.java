package com.ecommerce.davivienda.repository.product;

import com.ecommerce.davivienda.entity.product.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para operaciones sobre la entidad Category.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    /**
     * Busca una categoría por su nombre.
     *
     * @param nombre Nombre de la categoría
     * @return Optional con la categoría si existe
     */
    Optional<Category> findByNombre(String nombre);

    /**
     * Busca una categoría por su nombre ignorando mayúsculas/minúsculas.
     *
     * @param nombre Nombre de la categoría (case insensitive)
     * @return Optional con la categoría si existe
     */
    Optional<Category> findByNombreIgnoreCase(String nombre);

}

