package com.ecommerce.davivienda.service.cart.transactional.user;

import com.ecommerce.davivienda.entity.user.User;
import com.ecommerce.davivienda.entity.user.UserRole;
import com.ecommerce.davivienda.repository.user.UserRepository;
import com.ecommerce.davivienda.repository.user.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio transaccional para operaciones de consulta de User.
 * Centraliza operaciones de acceso a datos de usuarios para gestión de carritos.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CartUserTransactionalServiceImpl implements CartUserTransactionalService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findUserByEmail(String email) {
        log.debug("Buscando usuario con email: {}", email);
        return userRepository.findByCredenciales_Correo(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserRole> findUserRolesByUserId(Integer usuarioId) {
        log.debug("Buscando roles para usuario: {}", usuarioId);
        return userRoleRepository.findByUsuarioId(usuarioId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsUserRoleById(Integer userRoleId) {
        log.debug("Verificando existencia de userRoleId: {}", userRoleId);
        return userRoleRepository.existsById(userRoleId);
    }
}

