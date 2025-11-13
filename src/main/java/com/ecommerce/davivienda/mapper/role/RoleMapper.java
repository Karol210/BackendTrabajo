package com.ecommerce.davivienda.mapper.role;

import com.ecommerce.davivienda.dto.role.RoleRequestDto;
import com.ecommerce.davivienda.dto.role.RoleResponseDto;
import com.ecommerce.davivienda.entity.user.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * Mapper para conversiones entre Role y DTOs.
 * MapStruct genera la implementación automáticamente en tiempo de compilación.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Mapper(componentModel = "spring")
public interface RoleMapper {

    /**
     * Convierte RoleRequestDto a entidad Role.
     *
     * @param requestDto DTO con datos de la solicitud
     * @return Entidad Role
     */
    @Mapping(target = "rolId", ignore = true)
    @Mapping(source = "nombre", target = "nombreRol")
    Role toEntity(RoleRequestDto requestDto);

    /**
     * Convierte entidad Role a RoleResponseDto.
     *
     * @param role Entidad Role
     * @return DTO de respuesta
     */
    @Mapping(source = "nombreRol", target = "nombre")
    RoleResponseDto toResponseDto(Role role);

    /**
     * Convierte una lista de entidades Role a lista de DTOs de respuesta.
     *
     * @param roles Lista de entidades Role
     * @return Lista de DTOs de respuesta
     */
    List<RoleResponseDto> toResponseDtoList(List<Role> roles);

    /**
     * Actualiza campos de Role desde RoleRequestDto.
     *
     * @param requestDto DTO con datos nuevos
     * @param role Entidad existente a actualizar
     */
    @Mapping(target = "rolId", ignore = true)
    @Mapping(source = "nombre", target = "nombreRol")
    void updateEntityFromDto(RoleRequestDto requestDto, @MappingTarget Role role);
}

