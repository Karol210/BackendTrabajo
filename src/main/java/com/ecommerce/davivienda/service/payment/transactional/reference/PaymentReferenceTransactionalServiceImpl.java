package com.ecommerce.davivienda.service.payment.transactional.reference;

import com.ecommerce.davivienda.entity.payment.PaymentReference;
import com.ecommerce.davivienda.repository.payment.PaymentReferenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del servicio transaccional para operaciones de consulta y persistencia de PaymentReference.
 * Centraliza todas las operaciones de acceso a datos de referencias de pago.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentReferenceTransactionalServiceImpl implements PaymentReferenceTransactionalService {

    private final PaymentReferenceRepository paymentReferenceRepository;

    @Override
    @Transactional(readOnly = true)
    public boolean existsByReferenceNumber(String referenceNumber) {
        log.debug("Verificando existencia de referencia: {}", referenceNumber);
        
        boolean exists = paymentReferenceRepository.existsByReferenceNumber(referenceNumber);
        
        log.debug("Referencia {} - Existe: {}", referenceNumber, exists);
        return exists;
    }

    @Override
    @Transactional
    public PaymentReference savePaymentReference(PaymentReference reference) {
        log.debug("Guardando referencia de pago: {}", reference.getReferenceNumber());
        
        PaymentReference savedReference = paymentReferenceRepository.save(reference);
        
        log.info("Referencia de pago guardada exitosamente - ID: {}, Número: {}", 
                savedReference.getReferenceId(), 
                savedReference.getReferenceNumber());
        return savedReference;
    }
}

