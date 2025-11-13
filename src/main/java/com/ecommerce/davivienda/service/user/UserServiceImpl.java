package com.ecommerce.davivienda.service.user;

import com.ecommerce.davivienda.dto.user.UserRequestDto;
import com.ecommerce.davivienda.dto.user.UserResponseDto;
import com.ecommerce.davivienda.entity.user.*;
import com.ecommerce.davivienda.mapper.user.UserMapper;
import com.ecommerce.davivienda.repository.user.UserRepository;
import com.ecommerce.davivienda.repository.user.UserRoleRepository;
import com.ecommerce.davivienda.service.user.builder.UserBuilderService;
import com.ecommerce.davivienda.service.user.validation.UserValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del servicio principal para operaciones CRUD sobre usuarios.
 * Coordina las capacidades de validación y construcción sin lógica auxiliar.
 * 
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserValidationService validationService;
    private final UserBuilderService builderService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponseDto createUser(UserRequestDto request) {
        log.info("Creando usuario: {}", request.getEmail());

        validationService.validateEmailNotExists(request.getEmail());
        validationService.validatePasswordNotEmpty(request.getPassword());
        validationService.validateDocumentCombination(
                request.getDocumentTypeId(), 
                request.getDocumentNumber(), 
                null
        );

        DocumentType documentType = validationService.validateDocumentType(request.getDocumentTypeId());
        java.util.List<Role> roles = validationService.validateAndFindRolesByIds(request.getRoleIds());
        validationService.validateRolesCombination(roles);
        UserStatus userStatus = validationService.findUserStatusByName("Activo");

        String hashedPassword = passwordEncoder.encode(request.getPassword());

        User user = builderService.buildUserFromRequest(
                request, documentType, roles, userStatus, hashedPassword
        );
        User savedUser = userRepository.save(user);

        java.util.List<UserRole> userRoles = builderService.buildUserRoles(savedUser.getUsuarioId(), roles);
        java.util.List<UserRole> savedUserRoles = userRoleRepository.saveAll(userRoles);
        savedUser.setRoles(savedUserRoles);

        log.info("Usuario creado exitosamente: ID={} con {} roles", savedUser.getUsuarioId(), savedUserRoles.size());
        return userMapper.toResponseDto(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUserById(Integer id) {
        log.info("Consultando usuario por ID: {}", id);

        User user = validationService.findUserByIdOrThrow(id);

        log.info("Usuario encontrado: {}", user.getCorreo());
        return userMapper.toResponseDto(user);
    }

    @Override
    @Transactional
    public UserResponseDto updateUser(Integer id, UserRequestDto request) {
        log.info("Actualizando usuario ID: {}", id);

        User user = validationService.findUserByIdOrThrow(id);

        if (!user.getCorreo().equals(request.getEmail())) {
            validationService.validateEmailNotExists(request.getEmail());
        }

        validationService.validateDocumentCombination(
                request.getDocumentTypeId(), 
                request.getDocumentNumber(), 
                id
        );

        DocumentType documentType = validationService.validateDocumentType(request.getDocumentTypeId());
        java.util.List<Role> newRoles = validationService.validateAndFindRolesByIds(request.getRoleIds());
        validationService.validateRolesCombination(newRoles);
        UserStatus userStatus = request.getStatusId() != null
                ? userRepository.findById(request.getStatusId())
                        .map(User::getUserStatus)
                        .orElse(user.getUserStatus())
                : user.getUserStatus();

        userRoleRepository.deleteAll(user.getRoles());
        user.getRoles().clear();

        java.util.List<UserRole> userRoles = builderService.buildUserRoles(user.getUsuarioId(), newRoles);
        java.util.List<UserRole> savedUserRoles = userRoleRepository.saveAll(userRoles);
        user.setRoles(savedUserRoles);

        builderService.updateUserFields(user, request, documentType, userStatus);
        User updatedUser = userRepository.save(user);

        log.info("Usuario actualizado exitosamente: ID={} con {} roles", updatedUser.getUsuarioId(), savedUserRoles.size());
        return userMapper.toResponseDto(updatedUser);
    }

    @Override
    @Transactional
    public UserResponseDto deleteUser(Integer id) {
        log.info("Eliminando usuario ID: {}", id);

        User user = validationService.findUserByIdOrThrow(id);
        UserStatus inactiveStatus = validationService.findUserStatusByName("Inactivo");
        user.setUserStatus(inactiveStatus);

        User deletedUser = userRepository.save(user);

        log.info("Usuario eliminado (soft delete): ID={}", deletedUser.getUsuarioId());
        return userMapper.toResponseDto(deletedUser);
    }

    @Override
    @Transactional
    public UserResponseDto activateUser(Integer id) {
        log.info("Activando usuario ID: {}", id);

        User user = validationService.findUserByIdOrThrow(id);
        UserStatus activeStatus = validationService.findUserStatusByName("Activo");
        user.setUserStatus(activeStatus);

        User activatedUser = userRepository.save(user);

        log.info("Usuario activado: ID={}", activatedUser.getUsuarioId());
        return userMapper.toResponseDto(activatedUser);
    }

    @Override
    @Transactional
    public UserResponseDto changePassword(String email, String newPassword) {
        log.info("Cambiando contraseña para usuario: {}", email);

        validationService.validatePasswordNotEmpty(newPassword);
        User user = validationService.findUserByEmailOrThrow(email);

        String hashedPassword = passwordEncoder.encode(newPassword);
        user.getCredenciales().setContrasena(hashedPassword);

        User updatedUser = userRepository.save(user);

        log.info("Contraseña actualizada exitosamente para: {}", email);
        return userMapper.toResponseDto(updatedUser);
    }
}

