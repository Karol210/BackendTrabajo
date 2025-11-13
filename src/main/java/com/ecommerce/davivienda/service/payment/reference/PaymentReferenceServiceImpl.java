package com.ecommerce.davivienda.service.payment.reference;

import com.ecommerce.davivienda.entity.payment.PaymentReference;
import com.ecommerce.davivienda.exception.payment.PaymentException;
import com.ecommerce.davivienda.service.payment.transactional.reference.PaymentReferenceTransactionalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.ecommerce.davivienda.constants.Constants.CODE_PAYMENT_REFERENCE_GENERATION_FAILED;
import static com.ecommerce.davivienda.constants.Constants.ERROR_PAYMENT_REFERENCE_GENERATION_FAILED;

/**
 * Implementación del servicio para generación de referencias únicas de pago.
 * Genera UUIDs únicos con verificación en base de datos.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentReferenceServiceImpl implements PaymentReferenceService {

    private static final int MAX_REFERENCE_GENERATION_ATTEMPTS = 5;

    private final PaymentReferenceTransactionalService referenceTransactionalService;

    @Override
    public PaymentReference generateUniqueReference() {
        log.debug("Generando número de referencia de pago");

        for (int attempt = 1; attempt <= MAX_REFERENCE_GENERATION_ATTEMPTS; attempt++) {
            String referenceNumber = UUID.randomUUID().toString().toUpperCase();

            if (!referenceTransactionalService.existsByReferenceNumber(referenceNumber)) {
                PaymentReference reference = PaymentReference.builder()
                        .referenceNumber(referenceNumber)
                        .build();

                PaymentReference savedReference = referenceTransactionalService.savePaymentReference(reference);
                log.info("Referencia de pago generada exitosamente: {}", referenceNumber);
                return savedReference;
            }

            log.warn("Referencia duplicada en intento {}: {}", attempt, referenceNumber);
        }

        log.error("Falló la generación de referencia después de {} intentos", MAX_REFERENCE_GENERATION_ATTEMPTS);
        throw new PaymentException(
                ERROR_PAYMENT_REFERENCE_GENERATION_FAILED,
                CODE_PAYMENT_REFERENCE_GENERATION_FAILED
        );
    }
}

