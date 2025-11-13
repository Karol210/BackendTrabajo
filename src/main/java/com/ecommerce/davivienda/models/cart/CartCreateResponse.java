package com.ecommerce.davivienda.models.cart;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Respuesta para la creación de un carrito.
 * Contiene el ID del carrito recién creado y el email del usuario asociado.
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
public class CartCreateResponse {

    /**
     * ID del carrito creado.
     */
    @JsonProperty("cartId")
    private Integer cartId;

    /**
     * Email del usuario asociado al carrito.
     */
    @JsonProperty("userEmail")
    private String userEmail;

    /**
     * Mensaje descriptivo sobre la creación del carrito.
     */
    @JsonProperty("message")
    private String message;
}

