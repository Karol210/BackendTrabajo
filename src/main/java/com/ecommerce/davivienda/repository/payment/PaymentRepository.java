package com.ecommerce.davivienda.repository.payment;

import com.ecommerce.davivienda.entity.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad Payment.
 * Proporciona operaciones CRUD y consultas personalizadas.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    /**
     * Busca un pago por número de referencia.
     *
     * @param referenceNumber Número de referencia del pago
     * @return Optional con el pago si existe
     */
    @Query("SELECT p FROM Payment p JOIN FETCH p.reference r WHERE r.referenceNumber = :referenceNumber")
    Optional<Payment> findByReferenceNumber(@Param("referenceNumber") String referenceNumber);

    /**
     * Busca todos los pagos de un carrito específico.
     *
     * @param cartId ID del carrito
     * @return Lista de pagos del carrito
     */
    @Query("SELECT p FROM Payment p WHERE p.cart.carritoId = :cartId ORDER BY p.paymentDate DESC")
    List<Payment> findByCartId(@Param("cartId") Integer cartId);

    /**
     * Busca todos los pagos por estado.
     *
     * @param statusId ID del estado de pago
     * @return Lista de pagos con ese estado
     */
    @Query("SELECT p FROM Payment p WHERE p.paymentStatus.paymentStatusId = :statusId")
    List<Payment> findByPaymentStatus(@Param("statusId") Integer statusId);
}

