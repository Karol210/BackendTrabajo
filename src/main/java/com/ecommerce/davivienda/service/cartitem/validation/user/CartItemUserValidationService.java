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
     * Obtiene el userRoleId del usuario basado en su tipo y número de documento.
     * Valida que el tipo de documento exista, que el usuario exista y que tenga roles asignados.
     *
     * @param documentType Código del tipo de documento (ej: "CC", "TI", "CE")
     * @param documentNumber Número de documento del usuario
     * @return ID del UserRole del usuario
     * @throws com.ecommerce.davivienda.exception.CartException si el documento no existe o el usuario no tiene roles
     */
    Integer getUserRoleIdFromDocument(String documentType, String documentNumber);

}

