package com.ecommerce.davivienda.repository.product;

import com.ecommerce.davivienda.entity.product.Product;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Especificaciones para consultas dinámicas sobre productos.
 * Permite construir queries complejas usando Criteria API.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public class ProductSpecification {

    /**
     * Constructor privado para evitar instanciación.
     */
    private ProductSpecification() {
        throw new IllegalStateException("Utility class - No se puede instanciar");
    }

    /**
     * Construye una especificación combinada con múltiples filtros.
     *
     * @param categoryId ID de la categoría (opcional)
     * @param minPrice Precio mínimo (opcional)
     * @param maxPrice Precio máximo (opcional)
     * @param active Estado activo/inactivo (opcional)
     * @param searchTerm Término de búsqueda en nombre (opcional)
     * @return Specification combinada
     */
    public static Specification<Product> withFilters(
            Integer categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Boolean active,
            String searchTerm) {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (categoryId != null) {
                predicates.add(criteriaBuilder.equal(root.get("categoria").get("categoriaId"), categoryId));
            }

            if (minPrice != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("valorUnitario"), minPrice));
            }

            if (maxPrice != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("valorUnitario"), maxPrice));
            }

            if (active != null) {
                // active=true → estadoProductoId=1, active=false → estadoProductoId≠1
                if (active) {
                    predicates.add(criteriaBuilder.equal(root.get("estadoProductoId"), 1));
                } else {
                    predicates.add(criteriaBuilder.notEqual(root.get("estadoProductoId"), 1));
                }
            }

            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                String likePattern = "%" + searchTerm.trim().toLowerCase() + "%";
                Predicate namePredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("nombre")), likePattern);
                Predicate descriptionPredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("descripcion")), likePattern);
                predicates.add(criteriaBuilder.or(namePredicate, descriptionPredicate));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

