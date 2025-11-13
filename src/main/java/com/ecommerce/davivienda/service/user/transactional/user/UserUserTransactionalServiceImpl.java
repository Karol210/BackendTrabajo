package com.ecommerce.davivienda.service.user.transactional.user;

import com.ecommerce.davivienda.entity.user.User;
import com.ecommerce.davivienda.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Implementación del servicio transaccional para entidad User.
 * Centraliza operaciones de acceso a datos de usuarios.
 * Capacidad interna que NO debe ser expuesta como API REST.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserUserTransactionalServiceImpl implements UserUserTransactionalService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        log.debug("Verificando existencia de email: {}", email);
        return userRepository.existsByCredenciales_Correo(email);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findUserById(Integer userId) {
        log.debug("Buscando usuario: {}", userId);
        return userRepository.findById(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findUserByEmail(String email) {
        log.debug("Buscando usuario por email: {}", email);
        return userRepository.findByCredenciales_Correo(email);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findUserByUserRoleId(Integer userRoleId) {
        log.debug("Buscando usuario por userRoleId: {}", userRoleId);
        return userRepository.findByUsuarioRolId(userRoleId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByDocumentTypeAndNumber(Integer documentTypeId, String documentNumber) {
        log.debug("Buscando usuario por documento: tipo={}, número={}", documentTypeId, documentNumber);
        return userRepository.findByDocumentType_DocumentoIdAndNumeroDeDoc(documentTypeId, documentNumber);
    }

    @Override
    @Transactional
    public User saveUser(User user) {
        log.debug("Guardando usuario: {}", user.getCorreo());
        return userRepository.save(user);
    }
}

