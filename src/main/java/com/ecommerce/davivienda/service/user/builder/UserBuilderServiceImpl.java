package com.ecommerce.davivienda.service.user.builder;

import com.ecommerce.davivienda.dto.user.UserRequestDto;
import com.ecommerce.davivienda.entity.user.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Implementación del servicio de construcción para usuarios.
 * Capacidad interna que NO debe ser expuesta como API REST.
 * 
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserBuilderServiceImpl implements UserBuilderService {

    @Override
    public User buildUserFromRequest(
            UserRequestDto request,
            DocumentType documentType,
            java.util.List<Role> roles,
            UserStatus userStatus,
            String hashedPassword
    ) {
        log.debug("Construyendo usuario desde request: {}", request.getEmail());

        Credentials credentials = buildCredentials(request.getEmail(), hashedPassword);

        User user = User.builder()
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .documentType(documentType)
                .numeroDeDoc(request.getDocumentNumber())
                .credenciales(credentials)
                .userStatus(userStatus)
                .build();

        log.debug("Usuario construido exitosamente con {} roles", roles.size());
        return user;
    }

    @Override
    public java.util.List<UserRole> buildUserRoles(Integer userId, java.util.List<Role> roles) {
        log.debug("Construyendo {} UserRoles para usuario: {}", roles.size(), userId);

        java.util.List<UserRole> userRoles = new java.util.ArrayList<>();
        for (Role role : roles) {
            UserRole userRole = UserRole.builder()
                    .usuarioId(userId)
                    .role(role)
                    .build();
            userRoles.add(userRole);
            log.debug("UserRole construido para rol: {}", role.getNombreRol());
        }

        log.debug("Se construyeron {} UserRoles exitosamente", userRoles.size());
        return userRoles;
    }

    @Override
    public Credentials buildCredentials(String email, String hashedPassword) {
        log.debug("Construyendo credenciales para: {}", email);

        return Credentials.builder()
                .correo(email)
                .contrasena(hashedPassword)
                .build();
    }

    @Override
    public void updateUserFields(
            User user,
            UserRequestDto request,
            DocumentType documentType,
            UserStatus userStatus
    ) {
        log.debug("Actualizando campos del usuario: {}", user.getUsuarioId());

        user.setNombre(request.getNombre());
        user.setApellido(request.getApellido());
        user.setDocumentType(documentType);
        user.setNumeroDeDoc(request.getDocumentNumber());
        user.setUserStatus(userStatus);

        if (user.getCredenciales() != null && request.getEmail() != null) {
            user.getCredenciales().setCorreo(request.getEmail());
        }

        log.debug("Campos actualizados exitosamente");
    }
}

