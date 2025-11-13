package com.ecommerce.davivienda.service.role;

import com.ecommerce.davivienda.dto.role.RoleRequestDto;
import com.ecommerce.davivienda.dto.role.RoleResponseDto;
import com.ecommerce.davivienda.entity.user.Role;
import com.ecommerce.davivienda.exception.role.RoleException;
import com.ecommerce.davivienda.mapper.role.RoleMapper;
import com.ecommerce.davivienda.repository.user.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ecommerce.davivienda.constants.Constants.*;

/**
 * Implementación del servicio para operaciones CRUD sobre roles.
 * Gestiona la lógica de negocio para roles.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    @Transactional(readOnly = true)
    public List<RoleResponseDto> findAll() {
        log.info("Consultando todos los roles");
        
        List<Role> roles = roleRepository.findAll();
        
        log.info("Se encontraron {} roles", roles.size());
        return roleMapper.toResponseDtoList(roles);
    }

    @Override
    @Transactional(readOnly = true)
    public RoleResponseDto findById(Integer id) {
        log.info("Buscando rol con ID: {}", id);
        
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RoleException(
                        ERROR_ROLE_NOT_FOUND_BY_ID,
                        CODE_ROLE_NOT_FOUND_BY_ID
                ));
        
        log.info("Rol encontrado: {}", role.getNombreRol());
        return roleMapper.toResponseDto(role);
    }

    @Override
    @Transactional(readOnly = true)
    public RoleResponseDto findByName(String nombre) {
        log.info("Buscando rol con nombre: {}", nombre);
        
        Role role = roleRepository.findByNombreRol(nombre)
                .orElseThrow(() -> new RoleException(
                        ERROR_ROLE_NOT_FOUND_BY_NAME,
                        CODE_ROLE_NOT_FOUND_BY_NAME
                ));
        
        log.info("Rol encontrado: {} (ID: {})", role.getNombreRol(), role.getRolId());
        return roleMapper.toResponseDto(role);
    }

    @Override
    @Transactional
    public RoleResponseDto create(RoleRequestDto requestDto) {
        log.info("Creando nuevo rol: {}", requestDto.getNombre());
        
        validateNameNotExists(requestDto.getNombre());
        
        try {
            Role role = roleMapper.toEntity(requestDto);
            Role savedRole = roleRepository.save(role);
            
            log.info("Rol creado exitosamente con ID: {}", savedRole.getRolId());
            return roleMapper.toResponseDto(savedRole);
            
        } catch (DataIntegrityViolationException e) {
            log.error("Error de integridad al crear rol: {}", e.getMessage());
            throw new RoleException(
                    ERROR_ROLE_NAME_EXISTS,
                    CODE_ROLE_NAME_EXISTS,
                    e
            );
        }
    }

    @Override
    @Transactional
    public RoleResponseDto update(Integer id, RoleRequestDto requestDto) {
        log.info("Actualizando rol con ID: {}", id);
        
        Role existingRole = roleRepository.findById(id)
                .orElseThrow(() -> new RoleException(
                        ERROR_ROLE_NOT_FOUND_BY_ID,
                        CODE_ROLE_NOT_FOUND_BY_ID
                ));
        
        validateNameNotExistsForUpdate(requestDto.getNombre(), id);
        
        try {
            roleMapper.updateEntityFromDto(requestDto, existingRole);
            Role updatedRole = roleRepository.save(existingRole);
            
            log.info("Rol actualizado exitosamente: {}", updatedRole.getNombreRol());
            return roleMapper.toResponseDto(updatedRole);
            
        } catch (DataIntegrityViolationException e) {
            log.error("Error de integridad al actualizar rol: {}", e.getMessage());
            throw new RoleException(
                    ERROR_ROLE_NAME_EXISTS,
                    CODE_ROLE_NAME_EXISTS,
                    e
            );
        }
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        log.info("Eliminando rol con ID: {}", id);
        
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RoleException(
                        ERROR_ROLE_NOT_FOUND_BY_ID,
                        CODE_ROLE_NOT_FOUND_BY_ID
                ));
        
        try {
            roleRepository.delete(role);
            log.info("Rol eliminado exitosamente: {}", role.getNombreRol());
                    
        } catch (DataIntegrityViolationException e) {
            log.error("Error de integridad al eliminar rol: {}", e.getMessage());
            throw new RoleException(
                    "No se puede eliminar el rol porque está siendo usado por otros registros",
                    CODE_DATA_INTEGRITY_VIOLATION,
                    e
            );
        }
    }

    private void validateNameNotExists(String nombre) {
        if (roleRepository.existsByNombreRol(nombre)) {
            throw new RoleException(
                    ERROR_ROLE_NAME_EXISTS,
                    CODE_ROLE_NAME_EXISTS
            );
        }
    }

    private void validateNameNotExistsForUpdate(String nombre, Integer currentId) {
        roleRepository.findByNombreRol(nombre).ifPresent(existing -> {
            if (!existing.getRolId().equals(currentId)) {
                throw new RoleException(
                        ERROR_ROLE_NAME_EXISTS,
                        CODE_ROLE_NAME_EXISTS
                );
            }
        });
    }
}

