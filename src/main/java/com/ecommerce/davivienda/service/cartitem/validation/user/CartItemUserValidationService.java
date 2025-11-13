package com.ecommerce.davivienda.service.cartitem.validation.user;

/**
 * Servicio de validación de usuarios para items del carrito.
 * Responsabilidad: Validar userRoleId, documentos, emails y roles de usuario.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface CartItemUserValidationService {

    /**
     * Valida que el userRoleId exista en el sistema.
     *
     * @param userRoleId ID del UserRole a validar
     * @throws com.ecommerce.davivienda.exception.CartException si el userRoleId no existe
     */
    void validateUserRoleExists(Integer userRoleId);
    
    /**
     * Obtiene el userRoleId del usuario basado en su tipo y número de documento.
     * Valida que el tipo de documento exista, que el usuario exista y que tenga roles asignados.
     *
     * @param documentType Código del tipo de documento (ej: "CC", "TI", "CE")
     * @param documentNumber Número de documento del usuario
     * @return ID del UserRole del usuario
     * @throws com.ecommerce.davivienda.exception.CartException si el documento no existe o el usuario no tiene roles
     */
    Integer getUserRoleIdFromDocument(String documentType, String documentNumber);
    
    /**
     * Obtiene el userRoleId del usuario basado en su email (username).
     * Valida que el usuario exista y que tenga roles asignados.
     *
     * @param email Email del usuario (username del JWT)
     * @return ID del UserRole del usuario
     * @throws com.ecommerce.davivienda.exception.CartException si el usuario no existe o no tiene roles
     */
    Integer getUserRoleIdFromEmail(String email);
    
    /**
     * Valida que el userRoleId tenga el rol de "Cliente".
     * Solo los usuarios con rol de Cliente pueden agregar productos al carrito.
     *
     * @param userRoleId ID del UserRole a validar
     * @throws com.ecommerce.davivienda.exception.CartException si el usuario no tiene rol de Cliente
     */
    void validateUserHasClientRole(Integer userRoleId);
}

