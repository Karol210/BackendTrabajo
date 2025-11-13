package com.ecommerce.davivienda.repository.payment;

import com.ecommerce.davivienda.entity.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Repositorio JPA para la entidad Payment.
 * Proporciona operaciones CRUD y consultas personalizadas.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

}

