package com.ecommerce.davivienda.dto.cart;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO que contiene los cálculos detallados de un item del carrito.
 * Incluye subtotal, IVA y precio total.
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
public class CartItemCalculationDto {

    /**
     * Valor unitario del producto (sin IVA).
     */
    @JsonProperty("unitValue")
    private BigDecimal unitValue;

    /**
     * Porcentaje de IVA aplicado.
     */
    @JsonProperty("ivaPercentage")
    private BigDecimal ivaPercentage;

    /**
     * Cantidad del producto en el carrito.
     */
    @JsonProperty("quantity")
    private Integer quantity;

    /**
     * Subtotal sin IVA (unitValue * quantity).
     */
    @JsonProperty("subtotal")
    private BigDecimal subtotal;

    /**
     * Monto del IVA calculado.
     */
    @JsonProperty("ivaAmount")
    private BigDecimal ivaAmount;

    /**
     * Precio total con IVA incluido (subtotal + ivaAmount).
     */
    @JsonProperty("totalPrice")
    private BigDecimal totalPrice;
}

