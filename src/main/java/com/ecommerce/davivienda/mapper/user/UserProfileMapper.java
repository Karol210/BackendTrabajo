package com.ecommerce.davivienda.mapper.user;

import com.ecommerce.davivienda.dto.user.UserProfileDto;
import com.ecommerce.davivienda.entity.user.User;
import com.ecommerce.davivienda.entity.user.UserRole;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entidades User a UserProfileDto.
 * Transforma información del usuario a formato de respuesta sin datos sensibles.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Component
public class UserProfileMapper {

    /**
     * Convierte una entidad User a UserProfileDto para respuesta de login.
     * NO incluye información sensible como contraseña ni IDs internos.
     *
     * @param user Entidad User
     * @return UserProfileDto con información del perfil (sin IDs)
     */
    public UserProfileDto toProfileDto(User user) {
        if (user == null) {
            return null;
        }

        return UserProfileDto.builder()
                .nombre(user.getNombre())
                .apellido(user.getApellido())
                .correo(user.getCorreo())
                .tipoDocumento(user.getDocumentType() != null ? user.getDocumentType().getNombre() : null)
                .codigoDocumento(user.getDocumentType() != null ? user.getDocumentType().getCodigo() : null)
                .numeroDocumento(user.getNumeroDeDoc())
                .estado(user.getUserStatus() != null ? user.getUserStatus().getNombre() : null)
                .roles(extractRoleNames(user.getRoles()))
                .build();
    }

    /**
     * Extrae los nombres de los roles del usuario.
     *
     * @param userRoles Lista de UserRole
     * @return Lista de nombres de roles
     */
    private List<String> extractRoleNames(List<UserRole> userRoles) {
        if (userRoles == null || userRoles.isEmpty()) {
            return List.of();
        }

        return userRoles.stream()
                .map(userRole -> userRole.getRole().getNombreRol())
                .collect(Collectors.toList());
    }
}

