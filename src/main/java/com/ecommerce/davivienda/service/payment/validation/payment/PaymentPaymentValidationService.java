package com.ecommerce.davivienda.service.payment.validation.payment;

import com.ecommerce.davivienda.entity.payment.PaymentStatus;
import com.ecommerce.davivienda.entity.payment.PaymentType;

/**
 * Interfaz de servicio de validación de pagos para procesamiento de pagos.
 * Define operaciones de validación relacionadas con tipos de pago, estados y cuotas.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface PaymentPaymentValidationService {

    /**
     * Valida el tipo de pago y retorna la entidad correspondiente.
     *
     * @param paymentTypeStr Tipo de pago ("debito" o "credito")
     * @return PaymentType encontrado
     */
    PaymentType validatePaymentType(String paymentTypeStr);

    /**
     * Busca el estado de pago "Pendiente".
     *
     * @return PaymentStatus con estado Pendiente
     */
    PaymentStatus findPendingStatus();

    /**
     * Valida el número de cuotas según el tipo de pago.
     *
     * @param installments Número de cuotas (puede ser null)
     * @param paymentType Tipo de pago
     * @return Número de cuotas validado (1 si es null para débito)
     */
    Integer validateInstallments(Integer installments, String paymentType);
}

