package com.ecommerce.davivienda.controller.role;

import com.ecommerce.davivienda.dto.role.RoleRequestDto;
import com.ecommerce.davivienda.dto.role.RoleResponseDto;
import com.ecommerce.davivienda.models.Response;
import com.ecommerce.davivienda.service.role.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ecommerce.davivienda.constants.Constants.*;

/**
 * Controlador REST para gestionar roles del sistema.
 * Proporciona endpoints para operaciones CRUD sobre roles.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    /**
     * Obtiene todos los roles disponibles en el sistema.
     *
     * @return ResponseEntity con lista de roles
     */
    @GetMapping
    public ResponseEntity<Response<List<RoleResponseDto>>> findAll() {
        log.info("Solicitud GET: Listar todos los roles");
        
        List<RoleResponseDto> roles = roleService.findAll();
        
        Response<List<RoleResponseDto>> response = Response.<List<RoleResponseDto>>builder()
                .failure(false)
                .code(HttpStatus.OK.value())
                .message(SUCCESS_ROLES_LISTED)
                .body(roles)
                .timestamp(String.valueOf(System.currentTimeMillis()))
                .build();
        
        log.info("Respuesta exitosa: {} roles encontrados", roles.size());
        return ResponseEntity.ok(response);
    }

    /**
     * Busca un rol por su nombre.
     *
     * @param nombre Nombre del rol (ej: "Administrador", "Cliente")
     * @return ResponseEntity con el rol encontrado
     */
    @GetMapping("/name/{nombre}")
    public ResponseEntity<Response<RoleResponseDto>> findByName(@PathVariable String nombre) {
        log.info("Solicitud GET: Buscar rol con nombre: {}", nombre);
        
        RoleResponseDto role = roleService.findByName(nombre);
        
        Response<RoleResponseDto> response = Response.<RoleResponseDto>builder()
                .failure(false)
                .code(HttpStatus.OK.value())
                .message(SUCCESS_ROLE_FOUND)
                .body(role)
                .timestamp(String.valueOf(System.currentTimeMillis()))
                .build();
        
        log.info("Rol encontrado: {} (ID: {})", role.getNombre(), role.getRolId());
        return ResponseEntity.ok(response);
    }

}

