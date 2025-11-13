package com.ecommerce.davivienda.models.cart;

import com.ecommerce.davivienda.dto.cart.batch.CartItemBatchItemDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Solicitud para la creación de múltiples items del carrito de una sola vez.
 * Permite agregar varios productos al carrito en una única petición.
 * El usuario se obtiene automáticamente del token JWT.
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
public class CartItemBatchRequest {

    /**
     * ID del carrito al que se agregarán los items.
     * Obligatorio.
     */
    @NotNull(message = "El ID del carrito es obligatorio")
    @JsonProperty("cartId")
    private Integer cartId;

    /**
     * Lista de items a agregar al carrito.
     * Cada item contiene productId y quantity.
     */
    @NotEmpty(message = "La lista de items no puede estar vacía")
    @Valid
    @JsonProperty("items")
    private List<CartItemBatchItemDto> items;
}

