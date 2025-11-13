package com.ecommerce.davivienda.models.cart;

import com.ecommerce.davivienda.dto.cart.item.CartItemCalculationDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Respuesta para operaciones sobre items del carrito.
 * Contiene los datos del item, producto y cálculos detallados.
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
public class CartItemResponse {

    /**
     * ID del item del carrito.
     */
    @JsonProperty("id")
    private Integer id;

    /**
     * ID del carrito al que pertenece.
     * Campo interno - no se expone en JSON de respuesta.
     */
    @JsonIgnore
    private Integer cartId;

    /**
     * ID del producto.
     * Campo interno - no se expone en JSON de respuesta.
     */
    @JsonIgnore
    private Integer productId;

    /**
     * Nombre del producto.
     */
    @JsonProperty("productName")
    private String productName;

    /**
     * Descripción del producto.
     */
    @JsonProperty("productDescription")
    private String productDescription;

    /**
     * URL de la imagen del producto.
     */
    @JsonProperty("imageUrl")
    private String imageUrl;

    /**
     * Cálculos detallados del item (precio, IVA, total).
     */
    @JsonProperty("calculation")
    private CartItemCalculationDto calculation;

    /**
     * Verifica si el item tiene cálculos válidos.
     *
     * @return true si los cálculos no son nulos
     */
    public boolean hasValidCalculation() {
        return calculation != null 
                && calculation.getUnitValue() != null 
                && calculation.getQuantity() != null;
    }
}

