package com.ecommerce.davivienda.service.user;

import com.ecommerce.davivienda.entity.user.*;
import com.ecommerce.davivienda.exception.user.UserException;
import com.ecommerce.davivienda.mapper.user.UserMapper;
import com.ecommerce.davivienda.models.Response;
import com.ecommerce.davivienda.models.user.UserRequest;
import com.ecommerce.davivienda.models.user.UserResponse;
import com.ecommerce.davivienda.models.user.UserUpdateRequest;
import com.ecommerce.davivienda.service.auth.AuthUserService;
import com.ecommerce.davivienda.service.user.transactional.role.UserRoleTransactionalService;
import com.ecommerce.davivienda.service.user.transactional.user.UserUserTransactionalService;
import com.ecommerce.davivienda.service.user.validation.common.UserCommonValidationService;
import com.ecommerce.davivienda.service.user.validation.document.UserDocumentValidationService;
import com.ecommerce.davivienda.service.user.validation.role.UserRoleValidationService;
import com.ecommerce.davivienda.service.user.validation.status.UserStatusValidationService;
import com.ecommerce.davivienda.service.user.validation.user.UserUserValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ecommerce.davivienda.constants.Constants.*;

/**
 * Implementación del servicio principal para operaciones CRUD sobre usuarios.
 * Coordina las capacidades de validación y transaccional organizadas por dominios.
 * 
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    // Validation subcapacidades
    private final UserUserValidationService userValidationService;
    private final UserDocumentValidationService documentValidationService;
    private final UserRoleValidationService roleValidationService;
    private final UserStatusValidationService statusValidationService;
    private final UserCommonValidationService commonValidationService;

    // Transactional subcapacidades
    private final UserUserTransactionalService userTransactionalService;
    private final UserRoleTransactionalService roleTransactionalService;

    private final AuthUserService authUserService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Response<String> createUser(UserRequest request) {
        log.info("Creando usuario: {}", request.getEmail());

        validateUserCreateRequest(request);

        DocumentType documentType = resolveDocumentType(request.getDocumentType(), request.getDocumentTypeId());
        List<Role> roles = resolveRoles(request.getRoles(), request.getRoleIds());
        UserStatus userStatus = resolveUserStatus(request.getStatus(), request.getStatusId(), "Activo");
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        User user = userMapper.toEntity(request, documentType, userStatus, hashedPassword);
        User savedUser = userTransactionalService.saveUser(user);

        savedUser = createAndAssignUserRoles(savedUser, roles);

        log.info("Usuario creado exitosamente: ID={} con {} roles", savedUser.getUsuarioId(), savedUser.getRoles().size());
        return Response.success(SUCCESS_USER_CREATED);
    }

    private void validateUserCreateRequest(UserRequest request) {
        userValidationService.validateEmailNotExists(request.getEmail());
        commonValidationService.validatePasswordNotEmpty(request.getPassword());
        
        DocumentType documentType = resolveDocumentType(request.getDocumentType(), request.getDocumentTypeId());
        userValidationService.validateDocumentCombination(
                documentType.getDocumentoId(),
                request.getDocumentNumber(),
                null
        );
    }

    /**
     * Resuelve el tipo de documento desde nombre (preferido) o ID (compatibilidad).
     * Usa patrón Early Return para reducir complejidad cognitiva.
     * 
     * @param documentType Nombre o código del tipo de documento
     * @param documentTypeId ID del tipo de documento (formato antiguo)
     * @return DocumentType resuelto
     * @throws com.ecommerce.davivienda.exception.user.UserException si ambos parámetros están vacíos
     */
    private DocumentType resolveDocumentType(String documentType, Integer documentTypeId) {
        if (documentType != null && !documentType.trim().isEmpty()) {
            log.debug("Usando documentType (nombre): {}", documentType);
            return documentValidationService.validateDocumentTypeByName(documentType);
        }
        
        if (documentTypeId != null) {
            log.debug("Usando documentTypeId (ID): {}", documentTypeId);
            return documentValidationService.validateDocumentType(documentTypeId);
        }
        
        throw new com.ecommerce.davivienda.exception.user.UserException(
            ERROR_DOCUMENT_TYPE_NOT_FOUND,
            CODE_DOCUMENT_TYPE_NOT_FOUND
        );
    }

    /**
     * Resuelve los roles desde nombres (preferido) o IDs (compatibilidad).
     * Usa patrón Early Return para reducir complejidad cognitiva.
     * 
     * @param roleNames Lista de nombres de roles
     * @param roleIds Lista de IDs de roles (formato antiguo)
     * @return Lista de Role resueltos
     * @throws com.ecommerce.davivienda.exception.user.UserException si ambas listas están vacías
     */
    private List<Role> resolveRoles(List<String> roleNames, List<Integer> roleIds) {
        if (roleNames != null && !roleNames.isEmpty()) {
            log.debug("Usando roles (nombres): {}", roleNames);
            return roleValidationService.validateAndFindRolesByNames(roleNames);
        }
        
        if (roleIds != null && !roleIds.isEmpty()) {
            log.debug("Usando roleIds (IDs): {}", roleIds);
            return roleValidationService.validateAndFindRolesByIds(roleIds);
        }
        
        throw new com.ecommerce.davivienda.exception.user.UserException(
            ERROR_ROLES_EMPTY,
            CODE_ROLES_EMPTY
        );
    }

    /**
     * Resuelve el estado del usuario desde nombre (preferido) o ID (compatibilidad).
     * Usa patrón Early Return para reducir complejidad cognitiva.
     * 
     * @param statusName Nombre del estado
     * @param statusId ID del estado (formato antiguo)
     * @param defaultStatus Estado por defecto si ninguno está presente
     * @return UserStatus resuelto
     * @throws com.ecommerce.davivienda.exception.user.UserException si ninguna opción está presente
     */
    private UserStatus resolveUserStatus(String statusName, Integer statusId, String defaultStatus) {
        if (statusName != null && !statusName.trim().isEmpty()) {
            log.debug("Usando status (nombre): {}", statusName);
            return statusValidationService.findUserStatusByName(statusName);
        }
        
        if (statusId != null) {
            log.debug("Usando statusId (ID): {}", statusId);
            return statusValidationService.findUserStatusById(statusId);
        }
        
        if (defaultStatus != null) {
            log.debug("Usando estado por defecto: {}", defaultStatus);
            return statusValidationService.findUserStatusByName(defaultStatus);
        }
        
        throw new com.ecommerce.davivienda.exception.user.UserException(
            ERROR_STATUS_NOT_FOUND,
            CODE_STATUS_NOT_FOUND
        );
    }

    private User createAndAssignUserRoles(User savedUser, List<Role> roles) {
        List<UserRole> userRoles = userMapper.buildUserRoles(savedUser.getUsuarioId(), roles);
        List<UserRole> savedUserRoles = roleTransactionalService.saveAllUserRoles(userRoles);
        savedUser.setRoles(savedUserRoles);

        if (userMapper.assignPrimaryRole(savedUser, savedUserRoles)) {
            savedUser = userTransactionalService.saveUser(savedUser);
        }

        return savedUser;
    }

    @Override
    @Transactional
    public Response<String> updateUser(Integer id, UserUpdateRequest request) {
        Integer authenticatedUserRoleId = authUserService.getAuthenticatedUserRoleId();
        
        Integer userId;
        if (id == null) {
            log.info("Actualizando usuario autenticado (userRoleId: {})", authenticatedUserRoleId);
            User authenticatedUser = userValidationService.findUserByUserRoleId(authenticatedUserRoleId);
            userId = authenticatedUser.getUsuarioId();
        } else {
            log.info("Actualizando usuario ID: {}", id);
            userValidationService.validateUserOwnership(id, authenticatedUserRoleId);
            userId = id;
        }

        User user = userValidationService.findUserByIdOrThrow(userId);

        validateUserUpdateRequest(request, user, userId);

        DocumentType documentType = resolveUpdatedDocumentType(
                request.getDocumentType(),
                request.getDocumentTypeId(), 
                user.getDocumentType()
        );

        processUserRolesUpdate(user, request.getRoles(), request.getRoleIds());

        UserStatus userStatus = resolveUpdatedUserStatus(
                request.getStatus(),
                request.getStatusId(), 
                user.getUserStatus()
        );

        userMapper.updateUserFields(user, request, documentType, userStatus);

        User updatedUser = userTransactionalService.saveUser(user);

        log.info("Usuario actualizado exitosamente: ID={}", updatedUser.getUsuarioId());
        return Response.success(SUCCESS_USER_UPDATED);
    }

    private void validateUserUpdateRequest(UserUpdateRequest request, User user, Integer userId) {
        userValidationService.validateEmailUpdate(request.getEmail(), user.getCorreo());
        
        if (request.getDocumentType() != null || request.getDocumentTypeId() != null || request.getDocumentNumber() != null) {
            DocumentType requestDocumentType = resolveUpdatedDocumentType(
                    request.getDocumentType(),
                    request.getDocumentTypeId(),
                    user.getDocumentType()
            );
            
        userValidationService.validateDocumentUpdateCombination(
                    requestDocumentType.getDocumentoId(),
                request.getDocumentNumber(),
                user.getDocumentType().getDocumentoId(),
                user.getNumeroDeDoc(),
                userId
        );
    }
    }

    /**
     * Resuelve el tipo de documento actualizado desde nombre (preferido) o ID (compatibilidad).
     * Si ninguno está presente, retorna el tipo de documento actual.
     * 
     * @param documentType Nombre o código del tipo de documento
     * @param documentTypeId ID del tipo de documento (formato antiguo)
     * @param currentDocumentType Tipo de documento actual del usuario
     * @return DocumentType resuelto
     */
    private DocumentType resolveUpdatedDocumentType(
            String documentType,
            Integer documentTypeId,
            DocumentType currentDocumentType) {
        
        if (documentType != null && !documentType.trim().isEmpty()) {
            log.debug("Actualizando documentType (nombre): {}", documentType);
            return documentValidationService.getUpdatedDocumentTypeByName(documentType, currentDocumentType);
        } else if (documentTypeId != null) {
            log.debug("Actualizando documentTypeId (ID): {}", documentTypeId);
            return documentValidationService.getUpdatedDocumentType(documentTypeId, currentDocumentType);
        } else {
            return currentDocumentType;
        }
    }

    /**
     * Procesa la actualización de roles usando nombres (preferido) o IDs (compatibilidad).
     * 
     * @param user Usuario a actualizar
     * @param roleNames Lista de nombres de roles
     * @param roleIds Lista de IDs de roles (formato antiguo)
     */
    private void processUserRolesUpdate(User user, List<String> roleNames, List<Integer> roleIds) {
        if (roleNames != null && !roleNames.isEmpty()) {
            log.debug("Actualizando roles (nombres): {}", roleNames);
            roleValidationService.processUserRolesUpdateByNames(user, roleNames, userMapper);
        } else if (roleIds != null && !roleIds.isEmpty()) {
            log.debug("Actualizando roleIds (IDs): {}", roleIds);
            roleValidationService.processUserRolesUpdate(user, roleIds, userMapper);
        }
    }

    /**
     * Resuelve el estado del usuario actualizado desde nombre (preferido) o ID (compatibilidad).
     * Si ninguno está presente, retorna el estado actual.
     * 
     * @param statusName Nombre del estado
     * @param statusId ID del estado (formato antiguo)
     * @param currentUserStatus Estado actual del usuario
     * @return UserStatus resuelto
     */
    private UserStatus resolveUpdatedUserStatus(
            String statusName,
            Integer statusId,
            UserStatus currentUserStatus) {
        
        if (statusName != null && !statusName.trim().isEmpty()) {
            log.debug("Actualizando status (nombre): {}", statusName);
            return statusValidationService.getUpdatedUserStatusByName(statusName, currentUserStatus);
        } else if (statusId != null) {
            log.debug("Actualizando statusId (ID): {}", statusId);
            return statusValidationService.getUpdatedUserStatus(statusId, currentUserStatus);
        } else {
            return currentUserStatus;
        }
    }


    @Override
    @Transactional
    public Response<String> recoverPassword(String email, String newPassword, Boolean envioCorreo) {
        log.info("Recuperación de contraseña para usuario: {}", email);

        commonValidationService.validatePasswordNotEmpty(newPassword);
        User user = userValidationService.findUserByEmailOrThrow(email);

        String hashedPassword = passwordEncoder.encode(newPassword);
        user.getCredenciales().setContrasena(hashedPassword);

        userTransactionalService.saveUser(user);

        if (Boolean.TRUE.equals(envioCorreo)) {
            // TODO: Implementar servicio de envío de correo
            log.info("📧 Enviando correo de notificación de cambio de contraseña a: {}", email);
        }

        log.info("Contraseña recuperada exitosamente para: {}", email);
        return Response.success(envioCorreo ? SUCCESS_PASSWORD_RECOVERY_EMAIL_SENT : SUCCESS_PASSWORD_CHANGED);
    }

    @Override
    @Transactional
    public Response<String> changePasswordAuthenticated(String email, String newPassword) {
        log.info("Cambio de contraseña autenticado para usuario: {}", email);

        Integer authenticatedUserRoleId = authUserService.getAuthenticatedUserRoleId();
        userValidationService.validateEmailOwnership(email, authenticatedUserRoleId);

        commonValidationService.validatePasswordNotEmpty(newPassword);
        User user = userValidationService.findUserByEmailOrThrow(email);

        String hashedPassword = passwordEncoder.encode(newPassword);
        user.getCredenciales().setContrasena(hashedPassword);

        userTransactionalService.saveUser(user);

        log.info("Contraseña actualizada exitosamente para: {}", email);
        return Response.success(SUCCESS_PASSWORD_CHANGED);
    }

    @Override
    @Transactional(readOnly = true)
    public Response<List<UserResponse>> getAllUsers() {
        log.info("Obteniendo todos los usuarios del sistema");

        List<User> users = userTransactionalService.findAllUsers();
        List<UserResponse> userResponses = userMapper.toResponseList(users);

        log.info("Se encontraron {} usuarios", userResponses.size());
        return Response.<List<UserResponse>>builder()
                .failure(false)
                .code(200)
                .message(SUCCESS_USERS_FOUND)
                .body(userResponses)
                .timestamp(String.valueOf(System.currentTimeMillis()))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Response<UserResponse> getUserById(Integer userId) {
        log.info("Buscando usuario con ID: {}", userId);

        User user = userTransactionalService.findUserById(userId)
                .orElseThrow(() -> new UserException(
                        ERROR_USER_NOT_FOUND,
                        CODE_USER_NOT_FOUND
                ));

        UserResponse userResponse = userMapper.toResponse(user);

        log.info("Usuario encontrado: {}", userResponse.getEmail());
        return Response.<UserResponse>builder()
                .failure(false)
                .code(200)
                .message(SUCCESS_USER_FOUND)
                .body(userResponse)
                .timestamp(String.valueOf(System.currentTimeMillis()))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Response<UserResponse> getAuthenticatedUser() {
        Integer authenticatedUserRoleId = authUserService.getAuthenticatedUserRoleId();
        log.info("Obteniendo usuario autenticado con userRoleId: {}", authenticatedUserRoleId);

        User user = userTransactionalService.findUserByUserRoleId(authenticatedUserRoleId)
                .orElseThrow(() -> new UserException(
                        ERROR_USER_NOT_FOUND,
                        CODE_USER_NOT_FOUND
                ));

        UserResponse userResponse = userMapper.toResponse(user);

        log.info("Usuario autenticado encontrado: {}", userResponse.getEmail());
        return Response.<UserResponse>builder()
                .failure(false)
                .code(200)
                .message(SUCCESS_USER_FOUND)
                .body(userResponse)
                .timestamp(String.valueOf(System.currentTimeMillis()))
                .build();
    }
}

