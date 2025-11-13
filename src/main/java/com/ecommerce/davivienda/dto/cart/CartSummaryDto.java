package com.ecommerce.davivienda.dto.cart;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO que representa un resumen completo del carrito.
 * Incluye todos los items y totales agregados.
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
public class CartSummaryDto {

    /**
     * ID del carrito.
     */
    @JsonProperty("cartId")
    private Integer cartId;

    /**
     * Lista de items en el carrito con sus cálculos.
     */
    @JsonProperty("items")
    @Builder.Default
    private List<CartItemResponseDto> items = new ArrayList<>();

    /**
     * Cantidad total de items en el carrito.
     */
    @JsonProperty("totalItems")
    private Integer totalItems;

    /**
     * Subtotal total sin IVA de todos los items.
     */
    @JsonProperty("totalSubtotal")
    private BigDecimal totalSubtotal;

    /**
     * Monto total del IVA de todos los items.
     */
    @JsonProperty("totalIva")
    private BigDecimal totalIva;

    /**
     * Precio total final con IVA incluido.
     */
    @JsonProperty("totalPrice")
    private BigDecimal totalPrice;

    /**
     * Verifica si el carrito está vacío.
     *
     * @return true si no tiene items
     */
    public boolean isEmpty() {
        return items == null || items.isEmpty();
    }

    /**
     * Obtiene la cantidad de items diferentes en el carrito.
     *
     * @return Número de items únicos
     */
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }
}

