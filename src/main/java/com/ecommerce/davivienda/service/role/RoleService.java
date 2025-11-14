package com.ecommerce.davivienda.service.role;

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
     * Busca un rol por su nombre.
     *
     * @param nombre Nombre del rol (ej: "Administrador", "Cliente")
     * @return Rol encontrado
     * @throws com.ecommerce.davivienda.exception.role.RoleException si no existe
     */
    RoleResponseDto findByName(String nombre);


}

