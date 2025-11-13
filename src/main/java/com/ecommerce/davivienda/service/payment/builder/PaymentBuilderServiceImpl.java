package com.ecommerce.davivienda.service.payment.builder;

import com.ecommerce.davivienda.dto.payment.CardDataDto;
import com.ecommerce.davivienda.dto.payment.PaymentProcessResponseDto;
import com.ecommerce.davivienda.entity.cart.Cart;
import com.ecommerce.davivienda.entity.payment.*;
import com.ecommerce.davivienda.exception.payment.PaymentException;
import com.ecommerce.davivienda.repository.payment.PaymentReferenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.ecommerce.davivienda.constants.Constants.*;

/**
 * Implementación del servicio de construcción para procesamiento de pagos.
 * Contiene toda la lógica de construcción de entidades y respuestas.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentBuilderServiceImpl implements PaymentBuilderService {

    private static final int MAX_REFERENCE_GENERATION_ATTEMPTS = 5;

    private final PaymentReferenceRepository paymentReferenceRepository;

    @Override
    public PaymentReference generatePaymentReference() {
        log.debug("Generando número de referencia de pago");

        for (int attempt = 1; attempt <= MAX_REFERENCE_GENERATION_ATTEMPTS; attempt++) {
            String referenceNumber = UUID.randomUUID().toString().toUpperCase();

            if (!paymentReferenceRepository.existsByReferenceNumber(referenceNumber)) {
                PaymentReference reference = PaymentReference.builder()
                        .referenceNumber(referenceNumber)
                        .build();

                PaymentReference savedReference = paymentReferenceRepository.save(reference);
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

    @Override
    public Payment buildPayment(Cart cart, PaymentType paymentType, PaymentReference reference, PaymentStatus status) {
        log.debug("Construyendo entidad Payment para carrito ID: {}", cart.getCarritoId());

        Payment payment = Payment.builder()
                .cart(cart)
                .paymentType(paymentType)
                .reference(reference)
                .paymentStatus(status)
                .paymentDate(LocalDateTime.now())
                .build();

        log.debug("Payment construido exitosamente");
        return payment;
    }

    @Override
    public PaymentDebit buildPaymentDebit(Payment payment, CardDataDto cardData) {
        log.debug("Construyendo entidad PaymentDebit");

        LocalDate expirationDate = parseExpirationDate(cardData.getExpirationDate());
        String maskedCardNumber = maskCardNumber(cardData.getCardNumber());

        PaymentDebit paymentDebit = PaymentDebit.builder()
                .payment(payment)
                .cardHolderName(cardData.getCardHolderName())
                .cardNumber(maskedCardNumber)
                .expirationDate(expirationDate)
                .build();

        log.debug("PaymentDebit construido exitosamente");
        return paymentDebit;
    }

    @Override
    public PaymentCredit buildPaymentCredit(Payment payment, CardDataDto cardData, Integer installments) {
        log.debug("Construyendo entidad PaymentCredit con {} cuotas", installments);

        LocalDate expirationDate = parseExpirationDate(cardData.getExpirationDate());
        String maskedCardNumber = maskCardNumber(cardData.getCardNumber());

        PaymentCredit paymentCredit = PaymentCredit.builder()
                .payment(payment)
                .installments(installments)
                .cardHolderName(cardData.getCardHolderName())
                .cardNumber(maskedCardNumber)
                .expirationDate(expirationDate)
                .build();

        log.debug("PaymentCredit construido exitosamente");
        return paymentCredit;
    }

    @Override
    public PaymentProcessResponseDto buildPaymentResponse(Payment payment, CardDataDto cardData, Integer installments) {
        log.debug("Construyendo respuesta de pago para Payment ID: {}", payment.getPaymentId());

        String last4Digits = getLastFourDigits(cardData.getCardNumber());

        PaymentProcessResponseDto.PaymentProcessResponseDtoBuilder builder = PaymentProcessResponseDto.builder()
                .paymentId(payment.getPaymentId())
                .referenceNumber(payment.getReference().getReferenceNumber())
                .status(payment.getPaymentStatus().getName())
                .paymentType(payment.getPaymentType().getPaymentType())
                .cardLast4Digits(last4Digits);

        if (installments != null && installments > 1) {
            builder.installments(installments);
        }

        PaymentProcessResponseDto response = builder.build();
        log.info("Respuesta de pago construida - ID: {}, Referencia: {}", 
                payment.getPaymentId(), payment.getReference().getReferenceNumber());

        return response;
    }

    @Override
    public String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            log.warn("Número de tarjeta inválido para enmascarar, usando valor por defecto");
            return "****";
        }

        String cleanCardNumber = cardNumber.replaceAll("\\s", "");
        String last4Digits = cleanCardNumber.substring(cleanCardNumber.length() - 4);
        String masked = "************" + last4Digits;

        log.debug("Número de tarjeta enmascarado exitosamente");
        return masked;
    }

    /**
     * Parsea la fecha de vencimiento desde formato MM/YY a LocalDate.
     *
     * @param expirationDateStr Fecha en formato MM/YY
     * @return LocalDate parseada (último día del mes)
     */
    private LocalDate parseExpirationDate(String expirationDateStr) {
        if (expirationDateStr == null || expirationDateStr.trim().isEmpty()) {
            log.debug("Fecha de vencimiento no proporcionada, usando valor por defecto");
            return LocalDate.now().plusYears(5);
        }

        try {
            String[] parts = expirationDateStr.split("/");
            int month = Integer.parseInt(parts[0]);
            int year = 2000 + Integer.parseInt(parts[1]);

            LocalDate date = LocalDate.of(year, month, 1).withDayOfMonth(
                    LocalDate.of(year, month, 1).lengthOfMonth()
            );

            log.debug("Fecha de vencimiento parseada: {}", date);
            return date;

        } catch (Exception e) {
            log.warn("Error al parsear fecha de vencimiento: {}, usando valor por defecto", expirationDateStr);
            return LocalDate.now().plusYears(5);
        }
    }

    /**
     * Obtiene los últimos 4 dígitos del número de tarjeta.
     *
     * @param cardNumber Número completo de la tarjeta
     * @return Últimos 4 dígitos
     */
    private String getLastFourDigits(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "****";
        }

        String cleanCardNumber = cardNumber.replaceAll("\\s", "");
        return cleanCardNumber.substring(cleanCardNumber.length() - 4);
    }
}

