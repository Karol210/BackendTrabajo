package com.ecommerce.davivienda.service.user.transactional.role;

import com.ecommerce.davivienda.entity.user.Role;
import com.ecommerce.davivienda.entity.user.UserRole;
import com.ecommerce.davivienda.repository.user.RoleRepository;
import com.ecommerce.davivienda.repository.user.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio transaccional para Role y UserRole.
 * Centraliza operaciones de acceso a datos de roles y relaciones usuario-rol.
 * Capacidad interna que NO debe ser expuesta como API REST.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserRoleTransactionalServiceImpl implements UserRoleTransactionalService {

    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<Role> findRoleById(Integer roleId) {
        log.debug("Buscando rol por ID: {}", roleId);
        return roleRepository.findById(roleId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Role> findRoleByNombre(String nombreRol) {
        log.debug("Buscando rol por nombre: {}", nombreRol);
        return roleRepository.findByNombreRol(nombreRol);
    }

    @Override
    @Transactional
    public List<UserRole> saveAllUserRoles(List<UserRole> userRoles) {
        log.debug("Guardando {} UserRoles", userRoles.size());
        return userRoleRepository.saveAll(userRoles);
    }

    @Override
    @Transactional
    public void deleteAllUserRoles(List<UserRole> userRoles) {
        log.debug("Eliminando {} UserRoles", userRoles.size());
        userRoleRepository.deleteAll(userRoles);
    }

    @Override
    @Transactional
    public void deleteAllUserRolesByUserId(Integer userId) {
        log.debug("Eliminando todos los UserRoles del usuario: {}", userId);
        userRoleRepository.deleteByUsuarioId(userId);
        log.info("Todos los roles del usuario {} eliminados exitosamente", userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserRole> findUserRolesByUserId(Integer userId) {
        log.debug("Recargando UserRoles del usuario: {}", userId);
        return userRoleRepository.findByUsuarioId(userId);
    }
}

