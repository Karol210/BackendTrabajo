package com.ecommerce.davivienda.service.user.validation.role;

import com.ecommerce.davivienda.entity.user.Role;
import com.ecommerce.davivienda.entity.user.User;
import com.ecommerce.davivienda.entity.user.UserRole;
import com.ecommerce.davivienda.exception.user.UserException;
import com.ecommerce.davivienda.mapper.user.UserMapper;
import com.ecommerce.davivienda.service.user.transactional.role.UserRoleTransactionalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.ecommerce.davivienda.constants.Constants.*;

import java.util.List;

/**
 * Implementación del servicio de validación para Role.
 * Capacidad interna que NO debe ser expuesta como API REST.
 * 
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserRoleValidationServiceImpl implements UserRoleValidationService {

    private final UserRoleTransactionalService transactionalService;
    private final UserMapper userMapper;

    @Override
    public Role findRoleById(Integer roleId) {
        return transactionalService.findRoleById(roleId)
                .orElseThrow(() -> {
                    log.error("Rol no encontrado con ID: {}", roleId);
                    return new UserException(ERROR_ROLE_NOT_FOUND, CODE_ROLE_NOT_FOUND);
                });
    }

    @Override
    public Role findRoleByName(String nombreRol) {
        return transactionalService.findRoleByNombre(nombreRol)
                .orElseThrow(() -> {
                    log.error("Rol no encontrado con nombre: {}", nombreRol);
                    return new UserException(ERROR_ROLE_NOT_FOUND, CODE_ROLE_NOT_FOUND);
                });
    }

    @Override
    public java.util.List<Role> validateAndFindRolesByIds(java.util.List<Integer> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            log.error("Lista de roles vacía o nula");
            throw new UserException(ERROR_ROLES_EMPTY, CODE_ROLES_EMPTY);
        }

        java.util.Set<Integer> uniqueRoleIds = new java.util.HashSet<>(roleIds);
        if (uniqueRoleIds.size() != roleIds.size()) {
            log.warn("Se detectaron roles duplicados en la solicitud: {}", roleIds);
            throw new UserException(ERROR_ROLES_DUPLICATED, CODE_ROLES_DUPLICATED);
        }

        java.util.List<Role> roles = new java.util.ArrayList<>();
        for (Integer roleId : roleIds) {
            Role role = findRoleById(roleId);
            roles.add(role);
        }

        log.info("Validados {} roles correctamente por ID", roles.size());
        return roles;
    }

    @Override
    public java.util.List<Role> validateAndFindRolesByNames(java.util.List<String> roleNames) {
        if (roleNames == null || roleNames.isEmpty()) {
            log.error("Lista de roles vacía o nula");
            throw new UserException(ERROR_ROLES_EMPTY, CODE_ROLES_EMPTY);
        }

        java.util.Set<String> uniqueRoleNames = new java.util.HashSet<>(roleNames);
        if (uniqueRoleNames.size() != roleNames.size()) {
            log.warn("Se detectaron roles duplicados en la solicitud: {}", roleNames);
            throw new UserException(ERROR_ROLES_DUPLICATED, CODE_ROLES_DUPLICATED);
        }

        java.util.List<Role> roles = new java.util.ArrayList<>();
        for (String roleName : roleNames) {
            Role role = findRoleByName(roleName);
            roles.add(role);
        }

        log.info("Validados {} roles correctamente por nombre", roles.size());
        return roles;
    }

    @Override
    public List<UserRole> validateAndUpdateUserRoles(
            Integer userId,
            List<UserRole> currentUserRoles,
            List<Integer> newRoleIds) {
        
        List<Role> newRoles = validateAndFindRolesByIds(newRoleIds);

        if (!newRoles.isEmpty()) {
            transactionalService.deleteAllUserRolesByUserId(userId);
            
            List<UserRole> userRoles = userMapper.buildUserRoles(userId, newRoles);
            List<UserRole> savedUserRoles = transactionalService.saveAllUserRoles(userRoles);

            log.info("Roles actualizados por ID: {} roles asignados al usuario {}", savedUserRoles.size(), userId);
            return savedUserRoles;
        }

        log.debug("No se proporcionaron roles nuevos, manteniendo roles actuales");
        return currentUserRoles;
    }

    @Override
    public List<UserRole> validateAndUpdateUserRolesByNames(
            Integer userId,
            List<UserRole> currentUserRoles,
            List<String> newRoleNames) {
        
        List<Role> newRoles = validateAndFindRolesByNames(newRoleNames);


        if (!newRoles.isEmpty()) {
            // Elimina de BD
            transactionalService.deleteAllUserRolesByUserId(userId);
            
            // Crea y guarda nuevos roles
            List<UserRole> userRoles = userMapper.buildUserRoles(userId, newRoles);
            List<UserRole> savedUserRoles = transactionalService.saveAllUserRoles(userRoles);

            log.info("Roles actualizados por nombre: {} roles asignados al usuario {}", savedUserRoles.size(), userId);
            return savedUserRoles;
        }

        log.debug("No se proporcionaron roles nuevos, manteniendo roles actuales");
        return currentUserRoles;
    }

    @Override
    public boolean processUserRolesUpdate(User user, List<Integer> requestRoleIds, UserMapper mapper) {
        if (requestRoleIds == null || requestRoleIds.isEmpty()) {
            return false;
        }

        List<UserRole> savedUserRoles = validateAndUpdateUserRoles(
                user.getUsuarioId(),
                user.getRoles(),
                requestRoleIds
        );
        
        if (savedUserRoles != null && !savedUserRoles.isEmpty()) {
            mapper.assignPrimaryRole(user, savedUserRoles);
            log.debug("Rol primario actualizado para usuario {}", user.getUsuarioId());
            return true;
        }
        
        log.warn("No se pudieron actualizar los roles del usuario {}", user.getUsuarioId());
        return false;
    }

    @Override
    public boolean processUserRolesUpdateByNames(User user, List<String> requestRoleNames, UserMapper mapper) {
        if (requestRoleNames == null || requestRoleNames.isEmpty()) {
            return false;
        }

        List<UserRole> savedUserRoles = validateAndUpdateUserRolesByNames(
                user.getUsuarioId(),
                user.getRoles(),
                requestRoleNames
        );
        
        if (savedUserRoles != null && !savedUserRoles.isEmpty()) {
            mapper.assignPrimaryRole(user, savedUserRoles);
            log.debug("Rol primario actualizado para usuario {}", user.getUsuarioId());
            return true;
        }
        
        log.warn("No se pudieron actualizar los roles del usuario {}", user.getUsuarioId());
        return false;
    }
}

