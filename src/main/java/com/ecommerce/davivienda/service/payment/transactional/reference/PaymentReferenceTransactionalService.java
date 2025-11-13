package com.ecommerce.davivienda.service.payment.transactional.reference;

import com.ecommerce.davivienda.entity.payment.PaymentReference;

/**
 * Servicio transaccional para operaciones de consulta y persistencia de PaymentReference.
 * Maneja todas las operaciones de acceso a datos relacionadas con referencias de pago.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface PaymentReferenceTransactionalService {

    /**
     * Verifica si existe una referencia de pago por su número.
     *
     * @param referenceNumber Número de referencia a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existsByReferenceNumber(String referenceNumber);

    /**
     * Guarda una referencia de pago.
     *
     * @param reference Referencia a guardar
     * @return PaymentReference guardada
     */
    PaymentReference savePaymentReference(PaymentReference reference);
}

