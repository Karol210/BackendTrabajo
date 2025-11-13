package com.ecommerce.davivienda.service.cart.validation;

import com.ecommerce.davivienda.entity.cart.Cart;
import com.ecommerce.davivienda.entity.product.Product;

/**
 * Servicio de validación para operaciones de items del carrito.
 * Define validaciones de negocio para carritos y productos.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface CartItemValidationService {

    /**
     * Valida que el carrito exista.
     *
     * @param cartId ID del carrito
     * @return Carrito validado
     */
    Cart validateCartExists(Integer cartId);
    
    /**
     * Valida que el carrito exista o lo crea automáticamente si se proporciona userRoleId.
     *
     * @param cartId ID del carrito
     * @param userRoleId ID del usuario_rol para crear carrito si no existe (opcional)
     * @return Carrito validado o creado
     */
    Cart validateOrCreateCart(Integer cartId, Integer userRoleId);

    /**
     * Valida que el producto exista.
     *
     * @param productId ID del producto
     * @return Producto validado
     */
    Product validateProductExists(Integer productId);

    /**
     * Valida que el producto esté activo.
     *
     * @param product Producto a validar
     */
    void validateProductActive(Product product);

    /**
     * Valida que la cantidad sea mayor a 0.
     *
     * @param quantity Cantidad a validar
     */
    void validateQuantity(Integer quantity);

    /**
     * Valida que el producto no exista ya en el carrito.
     *
     * @param cartId ID del carrito
     * @param productId ID del producto
     */
    void validateProductNotInCart(Integer cartId, Integer productId);
    
    /**
     * Valida que el userRoleId exista en el sistema.
     *
     * @param userRoleId ID del UserRole a validar
     */
    void validateUserRoleExists(Integer userRoleId);
    
    /**
     * Obtiene el userRoleId del usuario basado en su tipo y número de documento.
     * Valida que el tipo de documento exista, que el usuario exista y que tenga roles asignados.
     *
     * @param documentType Código del tipo de documento (ej: "CC", "TI", "CE")
     * @param documentNumber Número de documento del usuario
     * @return ID del UserRole del usuario
     */
    Integer getUserRoleIdFromDocument(String documentType, String documentNumber);
    
    /**
     * Valida que el userRoleId tenga el rol de "Cliente".
     * Solo los usuarios con rol de Cliente pueden agregar productos al carrito.
     *
     * @param userRoleId ID del UserRole a validar
     */
    void validateUserHasClientRole(Integer userRoleId);
}

