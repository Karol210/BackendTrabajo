package com.ecommerce.davivienda.repository.payment;

import com.ecommerce.davivienda.entity.payment.PaymentCredit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio JPA para la entidad PaymentCredit.
 * Proporciona operaciones CRUD y consultas personalizadas para pagos crédito.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Repository
public interface PaymentCreditRepository extends JpaRepository<PaymentCredit, Integer> {

}

