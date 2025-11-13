package com.ecommerce.davivienda.service.user.validation.status;

import com.ecommerce.davivienda.entity.user.UserStatus;
import com.ecommerce.davivienda.exception.user.UserException;
import com.ecommerce.davivienda.service.user.transactional.status.UserStatusTransactionalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.ecommerce.davivienda.constants.Constants.*;

/**
 * Implementación del servicio de validación para UserStatus.
 * Capacidad interna que NO debe ser expuesta como API REST.
 * 
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserStatusValidationServiceImpl implements UserStatusValidationService {

    private final UserStatusTransactionalService transactionalService;

    @Override
    public UserStatus findUserStatusByName(String statusName) {
        return transactionalService.findUserStatusByName(statusName)
                .orElseThrow(() -> {
                    log.error("Estado de usuario no encontrado: {}", statusName);
                    return new UserException(ERROR_STATUS_NOT_FOUND, CODE_STATUS_NOT_FOUND);
                });
    }

    @Override
    public UserStatus findUserStatusById(Integer statusId) {
        return transactionalService.findUserStatusById(statusId)
                .orElseThrow(() -> {
                    log.error("Estado de usuario no encontrado con ID: {}", statusId);
                    return new UserException(ERROR_STATUS_NOT_FOUND, CODE_STATUS_NOT_FOUND);
                });
    }

    @Override
    public UserStatus getUpdatedUserStatus(Integer requestStatusId, UserStatus currentUserStatus) {
        if (requestStatusId == null) {
            return currentUserStatus;
        }
        
        log.debug("Validando nuevo estado de usuario por ID: {}", requestStatusId);
        return findUserStatusById(requestStatusId);
    }

    @Override
    public UserStatus getUpdatedUserStatusByName(String requestStatusName, UserStatus currentUserStatus) {
        if (requestStatusName == null || requestStatusName.trim().isEmpty()) {
            return currentUserStatus;
        }
        
        log.debug("Validando nuevo estado de usuario por nombre: {}", requestStatusName);
        return findUserStatusByName(requestStatusName);
    }
}

