package com.ecommerce.davivienda.service.cartitem.validation.user;

import com.ecommerce.davivienda.entity.user.DocumentType;
import com.ecommerce.davivienda.entity.user.User;
import com.ecommerce.davivienda.entity.user.UserRole;
import com.ecommerce.davivienda.exception.cart.CartException;
import com.ecommerce.davivienda.service.cartitem.transactional.user.CartItemUserTransactionalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.ecommerce.davivienda.constants.Constants.*;

/**
 * Implementación del servicio de validación de usuarios para items del carrito.
 * Aplica reglas de negocio relacionadas con usuarios, roles y autenticación.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CartItemUserValidationServiceImpl implements CartItemUserValidationService {

    private final CartItemUserTransactionalService transactionalService;

    @Override
    public void validateUserRoleExists(Integer userRoleId) {
        log.debug("Validando existencia del userRoleId: {}", userRoleId);
        
        if (userRoleId == null) {
            throw new CartException(ERROR_USER_ROLE_NOT_FOUND, CODE_USER_ROLE_NOT_FOUND);
        }
        
        if (!transactionalService.existsUserRoleById(userRoleId)) {
            log.warn("UserRoleId no encontrado: {}", userRoleId);
            throw new CartException(ERROR_USER_ROLE_NOT_FOUND, CODE_USER_ROLE_NOT_FOUND);
        }
    }
    
    @Override
    public Integer getUserRoleIdFromDocument(String documentType, String documentNumber) {
        log.debug("Obteniendo userRoleId para documento: {} {}", documentType, documentNumber);
        
        DocumentType docType = transactionalService.findDocumentTypeByCodigo(documentType)
                .orElseThrow(() -> {
                    log.warn("Tipo de documento no encontrado: {}", documentType);
                    return new CartException(ERROR_DOCUMENT_TYPE_NOT_FOUND, CODE_DOCUMENT_TYPE_NOT_FOUND);
                });
        
        User user = transactionalService.findUserByDocumentTypeAndNumber(
                docType.getDocumentoId(), documentNumber)
                .orElseThrow(() -> {
                    log.warn("Usuario no encontrado con documento: {} {}", documentType, documentNumber);
                    return new CartException(ERROR_USER_NOT_FOUND_BY_DOCUMENT, CODE_USER_NOT_FOUND_BY_DOCUMENT);
                });
        
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            log.warn("Usuario sin roles asignados: {} {}", documentType, documentNumber);
            throw new CartException(ERROR_USER_WITHOUT_ROLES, CODE_USER_WITHOUT_ROLES);
        }
        
        UserRole userRole = user.getRoles().get(0);
        Integer userRoleId = userRole.getUsuarioRolId();
        
        log.info("UserRoleId {} encontrado para usuario: {} {}", userRoleId, documentType, documentNumber);
        
        return userRoleId;
    }
    
    @Override
    public Integer getUserRoleIdFromEmail(String email) {
        log.debug("Obteniendo userRoleId para email: {}", email);
        
        User user = transactionalService.findUserByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Usuario no encontrado con email: {}", email);
                    return new CartException(ERROR_USER_NOT_FOUND_BY_DOCUMENT, CODE_USER_NOT_FOUND_BY_DOCUMENT);
                });
        
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            log.warn("Usuario sin roles asignados: {}", email);
            throw new CartException(ERROR_USER_WITHOUT_ROLES, CODE_USER_WITHOUT_ROLES);
        }
        
        UserRole userRole = user.getRoles().get(0);
        Integer userRoleId = userRole.getUsuarioRolId();
        
        log.info("UserRoleId {} encontrado para usuario: {}", userRoleId, email);
        
        return userRoleId;
    }
    
    @Override
    public void validateUserHasClientRole(Integer userRoleId) {
        log.debug("Validando que userRoleId {} tenga rol de Cliente", userRoleId);
        
        if (userRoleId == null) {
            throw new CartException(ERROR_USER_NOT_CLIENT_ROLE, CODE_USER_NOT_CLIENT_ROLE);
        }
        
        UserRole userRole = transactionalService.findUserRoleById(userRoleId)
                .orElseThrow(() -> {
                    log.warn("UserRoleId no encontrado: {}", userRoleId);
                    return new CartException(ERROR_USER_ROLE_NOT_FOUND, CODE_USER_ROLE_NOT_FOUND);
                });
        
        String roleName = userRole.getRole().getNombreRol();
        
        if (!"Cliente".equalsIgnoreCase(roleName)) {
            log.warn("Usuario con rol '{}' intentó agregar producto al carrito. Solo permitido para 'Cliente'", roleName);
            throw new CartException(ERROR_USER_NOT_CLIENT_ROLE, CODE_USER_NOT_CLIENT_ROLE);
        }
        
        log.info("Validación exitosa: UserRoleId {} tiene rol de Cliente", userRoleId);
    }
}

