package com.ecommerce.davivienda.service.user;

import com.ecommerce.davivienda.dto.user.UserRequestDto;
import com.ecommerce.davivienda.dto.user.UserResponseDto;
import com.ecommerce.davivienda.dto.user.UserUpdateRequestDto;
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
    public UserResponseDto updateUser(Integer id, UserUpdateRequestDto request) {
        log.info("Actualizando usuario ID: {} con campos parciales", id);

        User user = validationService.findUserByIdOrThrow(id);

        // Validar email solo si viene en el request y es diferente
        if (request.getEmail() != null && !user.getCorreo().equals(request.getEmail())) {
            validationService.validateEmailNotExists(request.getEmail());
        }

        // Validar combinación documento solo si alguno de los dos campos viene
        if (request.getDocumentTypeId() != null || request.getDocumentNumber() != null) {
            Integer documentTypeId = request.getDocumentTypeId() != null 
                    ? request.getDocumentTypeId() 
                    : user.getDocumentType().getDocumentoId();
            String documentNumber = request.getDocumentNumber() != null 
                    ? request.getDocumentNumber() 
                    : user.getNumeroDeDoc();
            
            validationService.validateDocumentCombination(documentTypeId, documentNumber, id);
        }

        // Validar y obtener documentType solo si viene en el request
        DocumentType documentType = request.getDocumentTypeId() != null
                ? validationService.validateDocumentType(request.getDocumentTypeId())
                : user.getDocumentType();

        // Actualizar roles solo si vienen en el request
        if (request.getRoleIds() != null && !request.getRoleIds().isEmpty()) {
            java.util.List<Role> newRoles = validationService.validateAndFindRolesByIds(request.getRoleIds());
            validationService.validateRolesCombination(newRoles);

            userRoleRepository.deleteAll(user.getRoles());
            user.getRoles().clear();

            java.util.List<UserRole> userRoles = builderService.buildUserRoles(user.getUsuarioId(), newRoles);
            java.util.List<UserRole> savedUserRoles = userRoleRepository.saveAll(userRoles);
            user.setRoles(savedUserRoles);
            
            log.info("Roles actualizados: {} roles asignados", savedUserRoles.size());
        }

        // Actualizar userStatus solo si viene en el request
        UserStatus userStatus = request.getStatusId() != null
                ? userRepository.findById(request.getStatusId())
                        .map(User::getUserStatus)
                        .orElse(user.getUserStatus())
                : user.getUserStatus();

        // Actualizar solo los campos que vienen en el request
        if (request.getNombre() != null) {
            user.setNombre(request.getNombre());
            log.debug("Nombre actualizado a: {}", request.getNombre());
        }
        if (request.getApellido() != null) {
            user.setApellido(request.getApellido());
            log.debug("Apellido actualizado a: {}", request.getApellido());
        }
        if (request.getDocumentTypeId() != null) {
            user.setDocumentType(documentType);
            log.debug("DocumentType actualizado");
        }
        if (request.getDocumentNumber() != null) {
            user.setNumeroDeDoc(request.getDocumentNumber());
            log.debug("Número de documento actualizado");
        }
        if (request.getEmail() != null && user.getCredenciales() != null) {
            user.getCredenciales().setCorreo(request.getEmail());
            log.debug("Email actualizado a: {}", request.getEmail());
        }
        if (request.getStatusId() != null) {
            user.setUserStatus(userStatus);
            log.debug("Status actualizado");
        }

        User updatedUser = userRepository.save(user);

        log.info("Usuario actualizado exitosamente: ID={}", updatedUser.getUsuarioId());
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

