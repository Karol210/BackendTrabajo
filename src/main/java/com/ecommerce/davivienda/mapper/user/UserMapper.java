package com.ecommerce.davivienda.mapper.user;

import com.ecommerce.davivienda.entity.user.*;
import com.ecommerce.davivienda.models.user.UserRequest;
import com.ecommerce.davivienda.models.user.UserResponse;
import com.ecommerce.davivienda.models.user.UserUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper MapStruct para User.
 * Maneja construcción, actualización y conversión de entidades.
 * Este mapper reemplaza el UserBuilderService eliminado.
 * 
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    // ==================== CONSTRUCCIÓN ====================

    /**
     * Construye entidad User desde Request con entidades relacionadas.
     * Reemplaza: UserBuilderService.buildUserFromRequest()
     *
     * @param request Request con datos del usuario
     * @param documentType Tipo de documento validado
     * @param userStatus Estado del usuario
     * @param hashedPassword Contraseña ya encriptada
     * @return User construido
     */
    @Mapping(target = "usuarioId", ignore = true)
    @Mapping(target = "nombre", source = "request.nombre")
    @Mapping(target = "apellido", source = "request.apellido")
    @Mapping(target = "documentType", source = "documentType")
    @Mapping(target = "numeroDeDoc", source = "request.documentNumber")
    @Mapping(target = "credenciales", expression = "java(buildCredentials(request.getEmail(), hashedPassword))")
    @Mapping(target = "userStatus", source = "userStatus")
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "usuarioRolId", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    User toEntity(
            UserRequest request,
            DocumentType documentType,
            UserStatus userStatus,
            String hashedPassword
    );

    /**
     * Construye Credentials desde email y contraseña encriptada.
     * Reemplaza: UserBuilderService.buildCredentials()
     *
     * @param email Correo electrónico
     * @param hashedPassword Contraseña encriptada
     * @return Credentials construidas
     */
    default Credentials buildCredentials(String email, String hashedPassword) {
        return Credentials.builder()
                .correo(email)
                .contrasena(hashedPassword)
                .build();
    }

    /**
     * Construye lista de relaciones UserRole para un usuario.
     * Reemplaza: UserBuilderService.buildUserRoles()
     *
     * @param userId ID del usuario
     * @param roles Lista de roles a asignar
     * @return Lista de UserRole construidos
     */
    default java.util.List<UserRole> buildUserRoles(Integer userId, java.util.List<Role> roles) {
        if (roles == null || roles.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        
        java.util.List<UserRole> userRoles = new java.util.ArrayList<>();
        for (Role role : roles) {
            UserRole userRole = UserRole.builder()
                    .usuarioId(userId)
                    .role(role)
                    .build();
            userRoles.add(userRole);
        }
        return userRoles;
    }

    // ==================== HELPERS ====================

    // ==================== CONVERSIÓN A RESPONSE ====================

    /**
     * Convierte entidad User a UserResponse.
     * Excluye información sensible como contraseña.
     *
     * @param user Entidad User
     * @return UserResponse con datos públicos del usuario
     */
    default UserResponse toResponse(User user) {
        if (user == null) {
            return null;
        }

        return UserResponse.builder()
                .usuarioId(user.getUsuarioId())
                .nombre(user.getNombre())
                .apellido(user.getApellido())
                .documentType(user.getDocumentType() != null 
                        ? user.getDocumentType().getNombre() 
                        : null)
                .documentNumber(user.getNumeroDeDoc())
                .email(user.getCorreo())
                .status(user.getUserStatus() != null 
                        ? user.getUserStatus().getNombre() 
                        : null)
                .roles(extractRoleNames(user.getRoles()))
                .usuarioRolId(user.getUsuarioRolId())
                .build();
    }

    /**
     * Convierte lista de Users a lista de UserResponse.
     *
     * @param users Lista de entidades User
     * @return Lista de UserResponse
     */
    default java.util.List<UserResponse> toResponseList(java.util.List<User> users) {
        if (users == null || users.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        
        return users.stream()
                .map(this::toResponse)
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Extrae los nombres de los roles de la lista de UserRole.
     *
     * @param userRoles Lista de UserRole
     * @return Lista de nombres de roles
     */
    default java.util.List<String> extractRoleNames(java.util.List<UserRole> userRoles) {
        if (userRoles == null || userRoles.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        
        return userRoles.stream()
                .filter(ur -> ur.getRole() != null)
                .map(ur -> ur.getRole().getNombreRol())
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Asigna el usuarioRolId primario al usuario desde la lista de roles guardados.
     * Toma el primer UserRole de la lista como rol primario.
     *
     * @param user Usuario al que se le asignará el rol primario
     * @param savedUserRoles Lista de roles guardados
     * @return true si se asignó el rol primario, false si la lista está vacía
     */
    default boolean assignPrimaryRole(User user, java.util.List<UserRole> savedUserRoles) {
        if (savedUserRoles != null && !savedUserRoles.isEmpty()) {
            user.setUsuarioRolId(savedUserRoles.get(0).getUsuarioRolId());
            return true;
        }
        return false;
    }

    /**
     * Actualiza los campos básicos del usuario desde UserUpdateRequest.
     * Solo actualiza los campos que no sean null en el request.
     *
     * @param user Usuario a actualizar
     * @param request Request con los campos a actualizar
     * @param documentType DocumentType validado (si se actualiza)
     * @param userStatus UserStatus validado (si se actualiza)
     */
    default void updateUserFields(
            User user,
            UserUpdateRequest request,
            DocumentType documentType,
            UserStatus userStatus) {
        
        updateBasicInfo(user, request);
        updateDocumentInfo(user, request, documentType);
        updateEmail(user, request);
        updateStatus(user, userStatus, request);
    }

    /**
     * Actualiza información básica (nombre y apellido).
     *
     * @param user Usuario a actualizar
     * @param request Request con los campos
     */
    default void updateBasicInfo(User user, UserUpdateRequest request) {
        if (request.getNombre() != null) {
            user.setNombre(request.getNombre());
        }
        if (request.getApellido() != null) {
            user.setApellido(request.getApellido());
        }
    }

    /**
     * Actualiza información de documento.
     * Soporta ambos formatos: documentType (nombre) o documentTypeId (ID).
     *
     * @param user Usuario a actualizar
     * @param request Request con los campos
     * @param documentType Tipo de documento validado
     */
    default void updateDocumentInfo(User user, UserUpdateRequest request, DocumentType documentType) {
        boolean hasDocumentType = request.getDocumentType() != null && !request.getDocumentType().trim().isEmpty();
        boolean hasDocumentTypeId = request.getDocumentTypeId() != null;
        
        if (hasDocumentType || hasDocumentTypeId) {
            user.setDocumentType(documentType);
        }
        if (request.getDocumentNumber() != null) {
            user.setNumeroDeDoc(request.getDocumentNumber());
        }
    }

    /**
     * Actualiza email del usuario.
     *
     * @param user Usuario a actualizar
     * @param request Request con el email
     */
    default void updateEmail(User user, UserUpdateRequest request) {
        if (request.getEmail() != null && user.getCredenciales() != null) {
            user.getCredenciales().setCorreo(request.getEmail());
        }
    }

    /**
     * Actualiza estado del usuario.
     * Soporta ambos formatos: status (nombre) o statusId (ID).
     *
     * @param user Usuario a actualizar
     * @param userStatus Estado validado
     * @param request Request para validar si se debe actualizar
     */
    default void updateStatus(User user, UserStatus userStatus, UserUpdateRequest request) {
        boolean hasStatus = request.getStatus() != null && !request.getStatus().trim().isEmpty();
        boolean hasStatusId = request.getStatusId() != null;
        
        if (hasStatus || hasStatusId) {
            user.setUserStatus(userStatus);
        }
    }
}

