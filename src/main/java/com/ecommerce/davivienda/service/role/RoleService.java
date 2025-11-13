package com.ecommerce.davivienda.service.role;

import com.ecommerce.davivienda.dto.role.RoleRequestDto;
import com.ecommerce.davivienda.dto.role.RoleResponseDto;

import java.util.List;

/**
 * Interface del servicio para operaciones CRUD sobre roles.
 * Define los contratos de negocio para la gestión de roles.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public interface RoleService {

    /**
     * Obtiene todos los roles disponibles en el sistema.
     *
     * @return Lista de roles
     */
    List<RoleResponseDto> findAll();

    /**
     * Busca un rol por su ID.
     *
     * @param id Identificador del rol
     * @return Rol encontrado
     * @throws com.ecommerce.davivienda.exception.role.RoleException si no existe
     */
    RoleResponseDto findById(Integer id);

    /**
     * Busca un rol por su nombre.
     *
     * @param nombre Nombre del rol (ej: "Administrador", "Cliente")
     * @return Rol encontrado
     * @throws com.ecommerce.davivienda.exception.role.RoleException si no existe
     */
    RoleResponseDto findByName(String nombre);

    /**
     * Crea un nuevo rol.
     *
     * @param requestDto DTO con los datos del rol a crear
     * @return Rol creado
     * @throws com.ecommerce.davivienda.exception.role.RoleException si el nombre ya existe
     */
    RoleResponseDto create(RoleRequestDto requestDto);

    /**
     * Actualiza un rol existente.
     *
     * @param id Identificador del rol a actualizar
     * @param requestDto DTO con los datos actualizados
     * @return Rol actualizado
     * @throws com.ecommerce.davivienda.exception.role.RoleException si no existe o hay duplicados
     */
    RoleResponseDto update(Integer id, RoleRequestDto requestDto);

    /**
     * Elimina un rol por su ID.
     *
     * @param id Identificador del rol a eliminar
     * @throws com.ecommerce.davivienda.exception.role.RoleException si no existe
     */
    void delete(Integer id);
}

