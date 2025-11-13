package com.ecommerce.davivienda.models.cart;

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
 * Solicitud para la creación y actualización de items del carrito.
 * Contiene el producto y la cantidad a agregar/actualizar.
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
public class CartItemRequest {

    /**
     * ID del carrito al que pertenece el item.
     * Opcional: Si no se proporciona, el sistema usa/crea el carrito del usuario automáticamente.
     */
    @JsonProperty("cartId")
    private Integer cartId;

    /**
     * ID del producto a agregar al carrito.
     */
    @NotNull(message = "El ID del producto es obligatorio")
    @JsonProperty("productId")
    private Integer productId;

    /**
     * Cantidad del producto a agregar/actualizar.
     */
    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    @JsonProperty("quantity")
    private Integer quantity;
}

