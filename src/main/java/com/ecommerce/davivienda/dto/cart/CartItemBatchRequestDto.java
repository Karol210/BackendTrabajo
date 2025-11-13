package com.ecommerce.davivienda.dto.cart;

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
 * DTO para la solicitud de creación de múltiples items del carrito de una sola vez.
 * Permite agregar varios productos al carrito en una única petición.
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
public class CartItemBatchRequestDto {

    /**
     * ID del carrito al que se agregarán los items.
     * Obligatorio.
     */
    @NotNull(message = "El ID del carrito es obligatorio")
    @JsonProperty("cartId")
    private Integer cartId;

    /**
     * Tipo de documento del usuario (código).
     * Ejemplos: "CC", "TI", "CE", "PA", "NIT"
     * Obligatorio para identificar al usuario.
     */
    @NotNull(message = "El tipo de documento es obligatorio")
    @JsonProperty("documentType")
    private String documentType;
    
    /**
     * Número de documento del usuario.
     * Obligatorio para identificar al usuario.
     */
    @NotNull(message = "El número de documento es obligatorio")
    @JsonProperty("documentNumber")
    private String documentNumber;

    /**
     * Lista de items a agregar al carrito.
     * Cada item contiene productId y quantity.
     */
    @NotEmpty(message = "La lista de items no puede estar vacía")
    @Valid
    @JsonProperty("items")
    private List<CartItemBatchItemDto> items;
}

