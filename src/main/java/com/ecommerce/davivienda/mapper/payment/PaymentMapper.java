package com.ecommerce.davivienda.mapper.payment;

import com.ecommerce.davivienda.dto.payment.CardDataDto;
import com.ecommerce.davivienda.dto.payment.PaymentProcessResponseDto;
import com.ecommerce.davivienda.entity.cart.Cart;
import com.ecommerce.davivienda.entity.payment.*;
import com.ecommerce.davivienda.util.Base64DecryptionService;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;

/**
 * Mapper para transformaciones de Payment, PaymentDebit, PaymentCredit y DTOs.
 * Utiliza MapStruct para mapeo compile-time type-safe.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Mapper(componentModel = "spring")
public interface PaymentMapper {

    /**
     * Construye la entidad Payment principal.
     *
     * @param cart Carrito asociado al pago
     * @param paymentType Tipo de pago
     * @param reference Referencia del pago
     * @param status Estado del pago
     * @return Payment construido
     */
    @Mapping(target = "paymentId", ignore = true)
    @Mapping(target = "cart", source = "cart")
    @Mapping(target = "paymentType", source = "paymentType")
    @Mapping(target = "reference", source = "reference")
    @Mapping(target = "paymentStatus", source = "status")
    @Mapping(target = "paymentDate", expression = "java(java.time.LocalDateTime.now())")
    Payment toPayment(Cart cart, PaymentType paymentType, PaymentReference reference, PaymentStatus status);

    /**
     * Construye la entidad PaymentDebit desde CardDataDto.
     * Encripta el nombre del titular y el número de tarjeta antes de guardar.
     *
     * @param payment Pago asociado
     * @param cardData Datos de la tarjeta
     * @param encryptionService Servicio de encriptación Base64
     * @return PaymentDebit construido con datos encriptados
     */
    @Mapping(target = "paymentDebitId", ignore = true)
    @Mapping(target = "payment", source = "payment")
    @Mapping(target = "cardHolderName", expression = "java(encryptCardHolderName(cardData.getCardHolderName(), encryptionService))")
    @Mapping(target = "cardNumber", expression = "java(encryptCardNumber(cardData.getCardNumber(), encryptionService))")
    @Mapping(target = "expirationDate", expression = "java(parseExpirationDate(cardData.getExpirationDate()))")
    PaymentDebit toPaymentDebit(Payment payment, CardDataDto cardData, @Context Base64DecryptionService encryptionService);

    /**
     * Construye la entidad PaymentCredit desde CardDataDto.
     * Encripta el nombre del titular y el número de tarjeta antes de guardar.
     *
     * @param payment Pago asociado
     * @param cardData Datos de la tarjeta
     * @param installments Número de cuotas
     * @param encryptionService Servicio de encriptación Base64
     * @return PaymentCredit construido con datos encriptados
     */
    @Mapping(target = "paymentCreditId", ignore = true)
    @Mapping(target = "payment", source = "payment")
    @Mapping(target = "installments", source = "installments")
    @Mapping(target = "cardHolderName", expression = "java(encryptCardHolderName(cardData.getCardHolderName(), encryptionService))")
    @Mapping(target = "cardNumber", expression = "java(encryptCardNumber(cardData.getCardNumber(), encryptionService))")
    @Mapping(target = "expirationDate", expression = "java(parseExpirationDate(cardData.getExpirationDate()))")
    PaymentCredit toPaymentCredit(Payment payment, CardDataDto cardData, Integer installments, @Context Base64DecryptionService encryptionService);

    /**
     * Construye el DTO de respuesta del pago procesado.
     *
     * @param payment Pago creado
     * @return PaymentProcessResponseDto
     */
    default PaymentProcessResponseDto toPaymentProcessResponseDto(Payment payment) {
        if (payment == null) {
            return null;
        }

        return PaymentProcessResponseDto.builder()
                .paymentId(payment.getPaymentId())
                .referenceNumber(payment.getReference().getReferenceNumber())
                .status(payment.getPaymentStatus().getName())
                .paymentType(payment.getPaymentType().getPaymentType())
                .build();
    }

    /**
     * Encripta el nombre del titular de la tarjeta usando Base64.
     *
     * @param cardHolderName Nombre del titular
     * @param encryptionService Servicio de encriptación
     * @return Nombre encriptado en Base64
     */
    default String encryptCardHolderName(String cardHolderName, Base64DecryptionService encryptionService) {
        if (cardHolderName == null || cardHolderName.trim().isEmpty()) {
            return "";
        }
        return encryptionService.encrypt(cardHolderName.trim());
    }

    /**
     * Encripta el número de tarjeta completo usando Base64.
     *
     * @param cardNumber Número completo de la tarjeta
     * @param encryptionService Servicio de encriptación
     * @return Número de tarjeta encriptado en Base64
     */
    default String encryptCardNumber(String cardNumber, Base64DecryptionService encryptionService) {
        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            return "";
        }
        String cleanCardNumber = cardNumber.replaceAll("\\s", "");
        return encryptionService.encrypt(cleanCardNumber);
    }

    /**
     * Parsea la fecha de vencimiento desde formato MM/YY a LocalDate.
     *
     * @param expirationDateStr Fecha en formato MM/YY
     * @return LocalDate parseada (último día del mes)
     */
    default LocalDate parseExpirationDate(String expirationDateStr) {
        if (expirationDateStr == null || expirationDateStr.trim().isEmpty()) {
            return LocalDate.now().plusYears(5);
        }

        try {
            String[] parts = expirationDateStr.split("/");
            int month = Integer.parseInt(parts[0]);
            int year = 2000 + Integer.parseInt(parts[1]);

            return LocalDate.of(year, month, 1).withDayOfMonth(
                    LocalDate.of(year, month, 1).lengthOfMonth()
            );

        } catch (Exception e) {
            return LocalDate.now().plusYears(5);
        }
    }

}

