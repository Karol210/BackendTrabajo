package com.ecommerce.davivienda.mapper.user;

import com.ecommerce.davivienda.dto.user.UserResponseDto;
import com.ecommerce.davivienda.entity.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper para conversiones entre User y DTOs.
 * MapStruct genera la implementación automáticamente en tiempo de compilación.
 * 
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Convierte entidad User a UserResponseDto.
     * Mapea campos anidados de entidades relacionadas.
     *
     * @param user Entidad User
     * @return UserResponseDto con datos del usuario
     */
    @Mapping(target = "id", source = "usuarioId")
    @Mapping(target = "documentType", source = "documentType.codigo")
    @Mapping(target = "documentNumber", source = "numeroDeDoc")
    @Mapping(target = "email", source = "credenciales.correo")
    @Mapping(target = "usuarioRolId", source = "usuarioRolId")
    @Mapping(target = "roles", expression = "java(mapRolesToStrings(user.getRoles()))")
    @Mapping(target = "status", source = "userStatus.nombre")
    @Mapping(target = "createdAt", source = "creationDate")
    UserResponseDto toResponseDto(User user);

    /**
     * Convierte una lista de UserRole a una lista de nombres de roles.
     *
     * @param userRoles Lista de UserRole
     * @return Lista de nombres de roles
     */
    default java.util.List<String> mapRolesToStrings(java.util.List<com.ecommerce.davivienda.entity.user.UserRole> userRoles) {
        if (userRoles == null || userRoles.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        return userRoles.stream()
                .map(userRole -> userRole.getRole().getNombreRol())
                .collect(java.util.stream.Collectors.toList());
    }
}

