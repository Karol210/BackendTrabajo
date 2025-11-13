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
 * DTO para la solicitud de creación y actualización de items del carrito.
 * Contiene el producto y la cantidad a agregar/actualizar.
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
public class CartItemRequestDto {

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
}

