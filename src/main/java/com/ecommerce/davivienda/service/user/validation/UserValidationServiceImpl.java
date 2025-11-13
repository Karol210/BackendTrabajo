package com.ecommerce.davivienda.service.user.validation;

import com.ecommerce.davivienda.dto.user.UserRequestDto;
import com.ecommerce.davivienda.entity.user.DocumentType;
import com.ecommerce.davivienda.entity.user.Role;
import com.ecommerce.davivienda.entity.user.User;
import com.ecommerce.davivienda.entity.user.UserStatus;
import com.ecommerce.davivienda.exception.user.UserException;
import com.ecommerce.davivienda.repository.user.DocumentTypeRepository;
import com.ecommerce.davivienda.repository.user.RoleRepository;
import com.ecommerce.davivienda.repository.user.UserRepository;
import com.ecommerce.davivienda.repository.user.UserStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.ecommerce.davivienda.constants.Constants.*;

/**
 * Implementación del servicio de validación para usuarios.
 * Capacidad interna que NO debe ser expuesta como API REST.
 * 
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserValidationServiceImpl implements UserValidationService {

    private final UserRepository userRepository;
    private final DocumentTypeRepository documentTypeRepository;
    private final RoleRepository roleRepository;
    private final UserStatusRepository userStatusRepository;

    @Override
    public void validateEmailNotExists(String email) {
        if (userRepository.existsByCredenciales_Correo(email)) {
            log.warn("Intento de registrar correo ya existente: {}", email);
            throw new UserException(ERROR_EMAIL_EXISTS, CODE_EMAIL_EXISTS);
        }
    }

    @Override
    public DocumentType validateDocumentType(Integer documentTypeId) {
        return documentTypeRepository.findById(documentTypeId)
                .orElseThrow(() -> {
                    log.error("Tipo de documento no encontrado: {}", documentTypeId);
                    return new UserException(ERROR_DOCUMENT_TYPE_NOT_FOUND, CODE_DOCUMENT_TYPE_NOT_FOUND);
                });
    }

    @Override
    public void validatePasswordNotEmpty(String password) {
        if (password == null || password.trim().isEmpty()) {
            log.error("Intento de crear usuario sin contraseña");
            throw new UserException(ERROR_PASSWORD_EMPTY, CODE_PASSWORD_EMPTY);
        }
    }

    @Override
    public Role findRoleById(Integer roleId) {
        return roleRepository.findById(roleId)
                .orElseThrow(() -> {
                    log.error("Rol no encontrado: {}", roleId);
                    return new UserException(ERROR_ROLE_NOT_FOUND, CODE_ROLE_NOT_FOUND);
                });
    }

    @Override
    public User findUserByIdOrThrow(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("Usuario no encontrado: {}", userId);
                    return new UserException(ERROR_USER_NOT_FOUND, CODE_USER_NOT_FOUND);
                });
    }

    @Override
    public User findUserByEmailOrThrow(String email) {
        return userRepository.findByCredenciales_Correo(email)
                .orElseThrow(() -> {
                    log.error("Usuario no encontrado con email: {}", email);
                    return new UserException(ERROR_USER_NOT_FOUND, CODE_USER_NOT_FOUND);
                });
    }

    @Override
    public void validateUpdateRequest(UserRequestDto request) {
        if (request.getId() == null) {
            log.error("Intento de actualizar usuario sin ID");
            throw new UserException(ERROR_USER_ID_NULL, CODE_USER_ID_NULL);
        }
        findUserByIdOrThrow(request.getId());
    }

    @Override
    public UserStatus findUserStatusByName(String statusName) {
        return userStatusRepository.findByNombre(statusName)
                .orElseThrow(() -> {
                    log.error("Estado de usuario no encontrado: {}", statusName);
                    return new UserException(ERROR_STATUS_NOT_FOUND, CODE_STATUS_NOT_FOUND);
                });
    }

    @Override
    public void validateDocumentCombination(Integer documentTypeId, String documentNumber, Integer excludeUserId) {
        Optional<User> existingUser = userRepository.findByDocumentType_DocumentoIdAndNumeroDeDoc(
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

        log.info("Validados {} roles correctamente", roles.size());
        return roles;
    }

    @Override
    public void validateRolesCombination(java.util.List<Role> roles) {
        boolean hasClienteRole = roles.stream()
                .anyMatch(role -> "Cliente".equalsIgnoreCase(role.getNombreRol()));
        
        boolean hasAdministradorRole = roles.stream()
                .anyMatch(role -> "Administrador".equalsIgnoreCase(role.getNombreRol()));

        if (hasClienteRole && hasAdministradorRole) {
            log.warn("Intento de asignar rol Cliente y Administrador simultáneamente");
            throw new UserException(ERROR_CLIENT_CANNOT_BE_ADMIN, CODE_CLIENT_CANNOT_BE_ADMIN);
        }

        log.debug("Combinación de roles validada exitosamente");
    }
}

