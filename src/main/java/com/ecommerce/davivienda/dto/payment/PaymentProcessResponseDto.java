package com.ecommerce.davivienda.dto.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la respuesta de procesamiento de pago.
 * Contiene información del pago creado exitosamente.
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
public class PaymentProcessResponseDto {

    /**
     * ID único del pago generado.
     */
    @JsonProperty("paymentId")
    private Integer paymentId;

    /**
     * Número de referencia único del pago.
     * Formato: UUID aleatorio
     */
    @JsonProperty("referenceNumber")
    private String referenceNumber;

    /**
     * Estado actual del pago.
     * Ejemplo: "Pendiente"
     */
    @JsonProperty("status")
    private String status;

    /**
     * Tipo de pago procesado.
     * Valores: "debito" o "credito"
     */
    @JsonProperty("paymentType")
    private String paymentType;
}

