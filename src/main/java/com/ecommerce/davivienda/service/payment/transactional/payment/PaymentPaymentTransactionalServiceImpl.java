package com.ecommerce.davivienda.service.payment.transactional.payment;

import com.ecommerce.davivienda.entity.payment.Payment;
import com.ecommerce.davivienda.entity.payment.PaymentCredit;
import com.ecommerce.davivienda.entity.payment.PaymentDebit;
import com.ecommerce.davivienda.repository.payment.PaymentCreditRepository;
import com.ecommerce.davivienda.repository.payment.PaymentDebitRepository;
import com.ecommerce.davivienda.repository.payment.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del servicio transaccional para operaciones de consulta y persistencia de Payment.
 * Centraliza todas las operaciones de acceso a datos de pagos.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentPaymentTransactionalServiceImpl implements PaymentPaymentTransactionalService {

    private final PaymentRepository paymentRepository;
    private final PaymentDebitRepository paymentDebitRepository;
    private final PaymentCreditRepository paymentCreditRepository;

    @Override
    @Transactional
    public Payment savePayment(Payment payment) {
        log.debug("Guardando pago - Carrito ID: {}, Tipo: {}", 
                payment.getCart().getCarritoId(), 
                payment.getPaymentType().getPaymentType());
        
        Payment savedPayment = paymentRepository.save(payment);
        
        log.info("Pago guardado exitosamente - ID: {}", savedPayment.getPaymentId());
        return savedPayment;
    }

    @Override
    @Transactional
    public PaymentDebit savePaymentDebit(PaymentDebit paymentDebit) {
        log.debug("Guardando detalles de pago débito - Payment ID: {}", 
                paymentDebit.getPayment().getPaymentId());
        
        PaymentDebit savedDebit = paymentDebitRepository.save(paymentDebit);
        
        log.info("Detalles de pago débito guardados exitosamente - ID: {}", 
                savedDebit.getPaymentDebitId());
        return savedDebit;
    }

    @Override
    @Transactional
    public PaymentCredit savePaymentCredit(PaymentCredit paymentCredit) {
        log.debug("Guardando detalles de pago crédito - Payment ID: {}, Cuotas: {}", 
                paymentCredit.getPayment().getPaymentId(), 
                paymentCredit.getInstallments());
        
        PaymentCredit savedCredit = paymentCreditRepository.save(paymentCredit);
        
        log.info("Detalles de pago crédito guardados exitosamente - ID: {}, Cuotas: {}", 
                savedCredit.getPaymentCreditId(), 
                savedCredit.getInstallments());
        return savedCredit;
    }
}

