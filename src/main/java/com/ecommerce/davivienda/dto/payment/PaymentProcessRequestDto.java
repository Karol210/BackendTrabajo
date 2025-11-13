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
 * DTO para la solicitud de procesamiento de pago.
 * Contiene los datos de la tarjeta encriptados en Base64.
 * El cartId es OPCIONAL - si no se proporciona, se usa el carrito del usuario autenticado.
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
public class PaymentProcessRequestDto {

    /**
     * ID del carrito a pagar.
     * Campo OPCIONAL - Si no se proporciona, se usará el carrito activo del usuario autenticado.
     */
    @JsonProperty("cartId")
    private Integer cartId;

    /**
     * Datos de la tarjeta encriptados en Base64.
     * Contiene JSON con: cardNumber, cardHolderName, expirationDate (opcional), 
     * cvv (opcional), installments (opcional), paymentType (debito/credito).
     * Campo OBLIGATORIO.
     */
    @NotBlank(message = "Los datos encriptados de la tarjeta son obligatorios")
    @JsonProperty("encryptedCardData")
    private String encryptedCardData;
}

