package com.ecommerce.davivienda.service.user.transactional.status;

import com.ecommerce.davivienda.entity.user.UserStatus;
import com.ecommerce.davivienda.repository.user.UserStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Implementación del servicio transaccional para UserStatus.
 * Centraliza operaciones de acceso a datos de estados de usuario.
 * Capacidad interna que NO debe ser expuesta como API REST.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserStatusTransactionalServiceImpl implements UserStatusTransactionalService {

    private final UserStatusRepository userStatusRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<UserStatus> findUserStatusByName(String statusName) {
        log.debug("Buscando estado de usuario: {}", statusName);
        return userStatusRepository.findByNombre(statusName);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserStatus> findUserStatusById(Integer statusId) {
        log.debug("Buscando estado de usuario por ID: {}", statusId);
        return userStatusRepository.findById(statusId);
    }
}

