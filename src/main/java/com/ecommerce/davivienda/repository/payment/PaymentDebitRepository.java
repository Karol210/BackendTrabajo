package com.ecommerce.davivienda.repository.payment;

import com.ecommerce.davivienda.entity.payment.PaymentDebit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio JPA para la entidad PaymentDebit.
 * Proporciona operaciones CRUD y consultas personalizadas para pagos débito.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Repository
public interface PaymentDebitRepository extends JpaRepository<PaymentDebit, Integer> {

}

