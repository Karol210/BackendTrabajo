package com.ecommerce.davivienda.repository.payment;

import com.ecommerce.davivienda.entity.payment.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio JPA para la entidad PaymentStatus.
 * Proporciona operaciones CRUD y consultas personalizadas para estados de pago.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Repository
public interface PaymentStatusRepository extends JpaRepository<PaymentStatus, Integer> {

    /**
     * Busca un estado de pago por nombre.
     *
     * @param name Nombre del estado (ej: "Pendiente", "Aprobado")
     * @return Optional con el estado si existe
     */
    Optional<PaymentStatus> findByName(String name);
}

