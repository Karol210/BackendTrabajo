package com.ecommerce.davivienda.service.role;

import com.ecommerce.davivienda.dto.role.RoleResponseDto;
import com.ecommerce.davivienda.entity.user.Role;
import com.ecommerce.davivienda.exception.role.RoleException;
import com.ecommerce.davivienda.mapper.role.RoleMapper;
import com.ecommerce.davivienda.repository.user.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        log.debug("Consultando todos los roles");
        
        List<Role> roles = roleRepository.findAll();
        
        log.debug("Se encontraron {} roles", roles.size());
        return roleMapper.toResponseDtoList(roles);
    }

  

    @Override
    @Transactional(readOnly = true)
    public RoleResponseDto findByName(String nombre) {
        log.debug("Buscando rol con nombre: {}", nombre);
        
        Role role = roleRepository.findByNombreRol(nombre)
                .orElseThrow(() -> new RoleException(
                        ERROR_ROLE_NOT_FOUND_BY_NAME,
                        CODE_ROLE_NOT_FOUND_BY_NAME
                ));
        
        log.debug("Rol encontrado: {} (ID: {})", role.getNombreRol(), role.getRolId());
        return roleMapper.toResponseDto(role);
    }


}

