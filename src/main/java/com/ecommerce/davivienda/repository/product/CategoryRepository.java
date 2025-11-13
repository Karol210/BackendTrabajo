package com.ecommerce.davivienda.repository.product;

import com.ecommerce.davivienda.entity.product.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
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
     * Verifica si existe una categoría con el nombre dado.
     *
     * @param nombre Nombre de la categoría
     * @return true si existe, false en caso contrario
     */
    boolean existsByNombre(String nombre);
}

