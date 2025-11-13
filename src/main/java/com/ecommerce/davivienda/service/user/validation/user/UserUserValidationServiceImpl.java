package com.ecommerce.davivienda.service.user.validation.user;

import com.ecommerce.davivienda.entity.user.User;
import com.ecommerce.davivienda.exception.user.UserException;
import com.ecommerce.davivienda.service.user.transactional.user.UserUserTransactionalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.ecommerce.davivienda.constants.Constants.*;

/**
 * Implementación del servicio de validación para entidad User.
 * Capacidad interna que NO debe ser expuesta como API REST.
 * 
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserUserValidationServiceImpl implements UserUserValidationService {

    private final UserUserTransactionalService transactionalService;

    @Override
    public void validateEmailNotExists(String email) {
        if (transactionalService.existsByEmail(email)) {
            log.warn("Intento de registrar correo ya existente: {}", email);
            throw new UserException(ERROR_EMAIL_EXISTS, CODE_EMAIL_EXISTS);
        }
    }

    @Override
    public User findUserByIdOrThrow(Integer userId) {
        return transactionalService.findUserById(userId)
                .orElseThrow(() -> {
                    log.error("Usuario no encontrado: {}", userId);
                    return new UserException(ERROR_USER_NOT_FOUND, CODE_USER_NOT_FOUND);
                });
    }

    @Override
    public User findUserByEmailOrThrow(String email) {
        return transactionalService.findUserByEmail(email)
                .orElseThrow(() -> {
                    log.error("Usuario no encontrado con email: {}", email);
                    return new UserException(ERROR_USER_NOT_FOUND, CODE_USER_NOT_FOUND);
                });
    }

    @Override
    public User findUserByUserRoleId(Integer userRoleId) {
        return transactionalService.findUserByUserRoleId(userRoleId)
                .orElseThrow(() -> {
                    log.error("Usuario no encontrado con userRoleId: {}", userRoleId);
                    return new UserException(ERROR_USER_NOT_FOUND, CODE_USER_NOT_FOUND);
                });
    }

    @Override
    public void validateDocumentCombination(Integer documentTypeId, String documentNumber, Integer excludeUserId) {
        Optional<User> existingUser = transactionalService.findByDocumentTypeAndNumber(
                documentTypeId, documentNumber
        );

        if (existingUser.isPresent()) {
            boolean isDifferentUser = excludeUserId == null 
                    || !existingUser.get().getUsuarioId().equals(excludeUserId);

            if (isDifferentUser) {
                log.warn("Combinación de documento ya existe: tipo={}, número={}", 
                        documentTypeId, documentNumber);
                throw new UserException(
                        ERROR_DOCUMENT_COMBINATION_EXISTS, 
                        CODE_DOCUMENT_COMBINATION_EXISTS
                );
            }
        }
    }

    @Override
    public void validateEmailUpdate(String newEmail, String currentEmail) {
        if (newEmail != null && !currentEmail.equals(newEmail)) {
            validateEmailNotExists(newEmail);
            log.debug("Email validado para actualización: {} → {}", currentEmail, newEmail);
        }
    }

    @Override
    public void validateDocumentUpdateCombination(
            Integer requestDocumentTypeId,
            String requestDocumentNumber,
            Integer currentDocumentTypeId,
            String currentDocumentNumber,
            Integer userId) {
        
        if (requestDocumentTypeId != null || requestDocumentNumber != null) {
            Integer documentTypeId = requestDocumentTypeId != null 
                    ? requestDocumentTypeId 
                    : currentDocumentTypeId;
            String documentNumber = requestDocumentNumber != null 
                    ? requestDocumentNumber 
                    : currentDocumentNumber;
            
            validateDocumentCombination(documentTypeId, documentNumber, userId);
            log.debug("Combinación de documento validada para actualización: tipo={}, número={}", 
                    documentTypeId, documentNumber);
        }
    }

    @Override
    public void validateUserOwnership(Integer userId, Integer authenticatedUserRoleId) {
        User user = findUserByIdOrThrow(userId);
        
        if (!authenticatedUserRoleId.equals(user.getUsuarioRolId())) {
            log.warn("Usuario autenticado (userRoleId={}) intenta modificar otro usuario (userRoleId={})", 
                    authenticatedUserRoleId, user.getUsuarioRolId());
            throw new UserException(ERROR_USER_UNAUTHORIZED_UPDATE, CODE_USER_UNAUTHORIZED_UPDATE);
        }
        
        log.debug("Ownership validado: usuario {} es dueño del recurso", authenticatedUserRoleId);
    }

    @Override
    public void validateEmailOwnership(String email, Integer authenticatedUserRoleId) {
        User user = findUserByEmailOrThrow(email);
        
        if (!authenticatedUserRoleId.equals(user.getUsuarioRolId())) {
            log.warn("Usuario autenticado (userRoleId={}) intenta cambiar contraseña de otro usuario (userRoleId={})", 
                    authenticatedUserRoleId, user.getUsuarioRolId());
            throw new UserException(ERROR_USER_UNAUTHORIZED_PASSWORD_CHANGE, CODE_USER_UNAUTHORIZED_PASSWORD_CHANGE);
        }
        
        log.debug("Ownership validado para email: usuario {} es dueño del email", authenticatedUserRoleId);
    }
}

