package com.ecommerce.davivienda.service.payment.validation.payment;

import com.ecommerce.davivienda.entity.payment.PaymentStatus;
import com.ecommerce.davivienda.entity.payment.PaymentType;
import com.ecommerce.davivienda.exception.payment.PaymentException;
import com.ecommerce.davivienda.repository.payment.PaymentStatusRepository;
import com.ecommerce.davivienda.repository.payment.PaymentTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.ecommerce.davivienda.constants.Constants.*;

/**
 * Implementación del servicio de validación de pagos para procesamiento de pagos.
 * Contiene toda la lógica de validación relacionada con tipos de pago, estados y cuotas.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentPaymentValidationServiceImpl implements PaymentPaymentValidationService {

    private static final String PAYMENT_TYPE_DEBIT = "debito";
    private static final String PAYMENT_TYPE_CREDIT = "credito";
    private static final String PAYMENT_STATUS_PENDING = "Pendiente";

    private final PaymentTypeRepository paymentTypeRepository;
    private final PaymentStatusRepository paymentStatusRepository;

    @Override
    public PaymentType validatePaymentType(String paymentTypeStr) {
        log.debug("Validando tipo de pago: {}", paymentTypeStr);

        if (paymentTypeStr == null || paymentTypeStr.trim().isEmpty()) {
            log.error("Tipo de pago vacío");
            throw new PaymentException(ERROR_INVALID_PAYMENT_TYPE, CODE_INVALID_PAYMENT_TYPE);
        }

        String normalizedType = paymentTypeStr.trim().toLowerCase();

        if (!PAYMENT_TYPE_DEBIT.equals(normalizedType) && !PAYMENT_TYPE_CREDIT.equals(normalizedType)) {
            log.error("Tipo de pago inválido: {}", paymentTypeStr);
            throw new PaymentException(ERROR_INVALID_PAYMENT_TYPE, CODE_INVALID_PAYMENT_TYPE);
        }

        PaymentType paymentType = paymentTypeRepository.findByPaymentType(normalizedType)
                .orElseThrow(() -> {
                    log.error("Tipo de pago no encontrado en BD: {}", normalizedType);
                    return new PaymentException(ERROR_INVALID_PAYMENT_TYPE, CODE_INVALID_PAYMENT_TYPE);
                });

        log.info("Tipo de pago validado: {}", normalizedType);
        return paymentType;
    }

    @Override
    public PaymentStatus findPendingStatus() {
        log.debug("Buscando estado de pago 'Pendiente'");

        PaymentStatus status = paymentStatusRepository.findByName(PAYMENT_STATUS_PENDING)
                .orElseThrow(() -> {
                    log.error("Estado de pago 'Pendiente' no encontrado en BD");
                    return new PaymentException(ERROR_PAYMENT_STATUS_NOT_FOUND, CODE_PAYMENT_STATUS_NOT_FOUND);
                });

        log.debug("Estado 'Pendiente' encontrado con ID: {}", status.getPaymentStatusId());
        return status;
    }

    @Override
    public Integer validateInstallments(Integer installments, String paymentType) {
        log.debug("Validando cuotas: {} para tipo: {}", installments, paymentType);

        String normalizedType = paymentType.trim().toLowerCase();

        if (PAYMENT_TYPE_DEBIT.equals(normalizedType)) {
            if (installments != null && installments > 1) {
                log.warn("Pagos débito solo permiten 1 cuota, ignorando valor: {}", installments);
            }
            return 1;
        }

        if (PAYMENT_TYPE_CREDIT.equals(normalizedType)) {
            if (installments == null) {
                log.debug("No se especificaron cuotas para crédito, usando 1 por defecto");
                return 1;
            }

            if (installments <= 0) {
                log.error("Número de cuotas inválido: {}", installments);
                throw new PaymentException(ERROR_INVALID_INSTALLMENTS, CODE_INVALID_INSTALLMENTS);
            }

            log.info("Cuotas validadas para crédito: {}", installments);
            return installments;
        }

        return 1;
    }
}

