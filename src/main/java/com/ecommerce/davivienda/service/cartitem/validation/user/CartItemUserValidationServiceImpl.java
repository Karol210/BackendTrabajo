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
    

}

