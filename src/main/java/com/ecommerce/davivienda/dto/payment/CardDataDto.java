package com.ecommerce.davivienda.dto.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa los datos de una tarjeta desencriptados.
 * Este DTO se obtiene después de desencriptar el campo encryptedCardData.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CardDataDto {

    /**
     * Número de tarjeta (16 dígitos).
     * Campo OBLIGATORIO.
     */
    @NotBlank(message = "El número de tarjeta es obligatorio")
    @JsonProperty("cardNumber")
    private String cardNumber;

    /**
     * Nombre del titular de la tarjeta.
     * Campo OBLIGATORIO.
     */
    @NotBlank(message = "El nombre del titular es obligatorio")
    @JsonProperty("cardHolderName")
    private String cardHolderName;

    /**
     * Fecha de vencimiento de la tarjeta.
     * Formato: MM/YY (ejemplo: "12/25")
     * Campo OPCIONAL.
     */
    @JsonProperty("expirationDate")
    private String expirationDate;

    /**
     * CVV de la tarjeta (3 o 4 dígitos).
     * Campo OPCIONAL.
     */
    @JsonProperty("cvv")
    private String cvv;

    /**
     * Número de cuotas (solo para crédito).
     * Campo OPCIONAL - Si no se proporciona, se asume 1 cuota.
     */
    @JsonProperty("installments")
    private Integer installments;

    /**
     * Tipo de pago: "debito" o "credito".
     * Campo OBLIGATORIO.
     */
    @NotBlank(message = "El tipo de pago es obligatorio")
    @JsonProperty("paymentType")
    private String paymentType;
}

