package com.ecommerce.davivienda.repository.payment;

import com.ecommerce.davivienda.entity.payment.PaymentReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio JPA para la entidad PaymentReference.
 * Proporciona operaciones CRUD y consultas personalizadas para referencias de pago.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Repository
public interface PaymentReferenceRepository extends JpaRepository<PaymentReference, Integer> {

    /**
     * Verifica si existe una referencia con el número dado.
     *
     * @param referenceNumber Número de referencia
     * @return true si existe, false en caso contrario
     */
    boolean existsByReferenceNumber(String referenceNumber);
}

