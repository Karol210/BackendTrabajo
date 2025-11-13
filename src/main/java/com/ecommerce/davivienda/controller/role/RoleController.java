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
     * Busca un rol por su ID.
     *
     * @param id Identificador del rol
     * @return ResponseEntity con el rol encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<Response<RoleResponseDto>> findById(@PathVariable Integer id) {
        log.info("Solicitud GET: Buscar rol con ID: {}", id);
        
        RoleResponseDto role = roleService.findById(id);
        
        Response<RoleResponseDto> response = Response.<RoleResponseDto>builder()
                .failure(false)
                .code(HttpStatus.OK.value())
                .message(SUCCESS_ROLE_FOUND)
                .body(role)
                .timestamp(String.valueOf(System.currentTimeMillis()))
                .build();
        
        log.info("Rol encontrado: {}", role.getNombre());
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

    /**
     * Crea un nuevo rol.
     * Requiere rol de Administrador.
     *
     * @param requestDto DTO con los datos del rol a crear
     * @return ResponseEntity con el rol creado
     */
    @PostMapping
    @PreAuthorize("hasRole('Administrador')")
    public ResponseEntity<Response<RoleResponseDto>> create(
            @Valid @RequestBody RoleRequestDto requestDto) {
        log.info("Solicitud POST: Crear rol - nombre: {}", requestDto.getNombre());
        
        RoleResponseDto role = roleService.create(requestDto);
        
        Response<RoleResponseDto> response = Response.<RoleResponseDto>builder()
                .failure(false)
                .code(HttpStatus.CREATED.value())
                .message(SUCCESS_ROLE_CREATED)
                .body(role)
                .timestamp(String.valueOf(System.currentTimeMillis()))
                .build();
        
        log.info("Rol creado exitosamente con ID: {}", role.getRolId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Actualiza un rol existente.
     * Requiere rol de Administrador.
     *
     * @param id Identificador del rol a actualizar
     * @param requestDto DTO con los datos actualizados
     * @return ResponseEntity con el rol actualizado
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('Administrador')")
    public ResponseEntity<Response<RoleResponseDto>> update(
            @PathVariable Integer id,
            @Valid @RequestBody RoleRequestDto requestDto) {
        log.info("Solicitud PUT: Actualizar rol ID: {} - nombre: {}", id, requestDto.getNombre());
        
        RoleResponseDto role = roleService.update(id, requestDto);
        
        Response<RoleResponseDto> response = Response.<RoleResponseDto>builder()
                .failure(false)
                .code(HttpStatus.OK.value())
                .message(SUCCESS_ROLE_UPDATED)
                .body(role)
                .timestamp(String.valueOf(System.currentTimeMillis()))
                .build();
        
        log.info("Rol actualizado exitosamente: {}", role.getNombre());
        return ResponseEntity.ok(response);
    }

    /**
     * Elimina un rol por su ID.
     * Requiere rol de Administrador.
     *
     * @param id Identificador del rol a eliminar
     * @return ResponseEntity con mensaje de confirmación
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('Administrador')")
    public ResponseEntity<Response<Void>> delete(@PathVariable Integer id) {
        log.info("Solicitud DELETE: Eliminar rol con ID: {}", id);
        
        roleService.delete(id);
        
        Response<Void> response = Response.<Void>builder()
                .failure(false)
                .code(HttpStatus.OK.value())
                .message(SUCCESS_ROLE_DELETED)
                .timestamp(String.valueOf(System.currentTimeMillis()))
                .build();
        
        log.info("Rol eliminado exitosamente con ID: {}", id);
        return ResponseEntity.ok(response);
    }
}

