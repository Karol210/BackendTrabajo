package com.ecommerce.davivienda.service.payment.transactional.payment;

import com.ecommerce.davivienda.entity.payment.Payment;
import com.ecommerce.davivienda.entity.payment.PaymentCredit;
import com.ecommerce.davivienda.entity.payment.PaymentDebit;

/**
 * Servicio transaccional para operaciones de consulta y persistencia de Payment.
 * Maneja todas las operaciones de acceso a datos relacionadas con pagos principales,
 * débito y crédito.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface PaymentPaymentTransactionalService {

    /**
     * Guarda un pago principal.
     *
     * @param payment Pago a guardar
     * @return Payment guardado
     */
    Payment savePayment(Payment payment);

    /**
     * Guarda los detalles de un pago débito.
     *
     * @param paymentDebit Pago débito a guardar
     * @return PaymentDebit guardado
     */
    PaymentDebit savePaymentDebit(PaymentDebit paymentDebit);

    /**
     * Guarda los detalles de un pago crédito.
     *
     * @param paymentCredit Pago crédito a guardar
     * @return PaymentCredit guardado
     */
    PaymentCredit savePaymentCredit(PaymentCredit paymentCredit);
}

