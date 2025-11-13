package com.ecommerce.davivienda.dto.cart;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa un item individual dentro de un batch de productos.
 * Contiene el ID del producto y la cantidad a agregar.
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
public class CartItemBatchItemDto {

    /**
     * ID del producto a agregar.
     */
    @NotNull(message = "El ID del producto es obligatorio")
    @JsonProperty("productId")
    private Integer productId;

    /**
     * Cantidad del producto a agregar.
     */
    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    @JsonProperty("quantity")
    private Integer quantity;
}

