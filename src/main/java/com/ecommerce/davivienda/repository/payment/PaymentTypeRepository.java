package com.ecommerce.davivienda.repository.payment;

import com.ecommerce.davivienda.entity.payment.PaymentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio JPA para la entidad PaymentType.
 * Proporciona operaciones CRUD y consultas personalizadas para tipos de pago.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Repository
public interface PaymentTypeRepository extends JpaRepository<PaymentType, String> {

    /**
     * Busca un tipo de pago por su identificador.
     *
     * @param paymentType Identificador del tipo ("debito" o "credito")
     * @return Optional con el tipo de pago si existe
     */
    Optional<PaymentType> findByPaymentType(String paymentType);
}

