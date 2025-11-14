package com.ecommerce.davivienda.controller.user;

import com.ecommerce.davivienda.models.Response;
import com.ecommerce.davivienda.models.user.PasswordChangeAuthenticatedRequest;
import com.ecommerce.davivienda.models.user.PasswordRecoveryRequest;
import com.ecommerce.davivienda.models.user.UserRequest;
import com.ecommerce.davivienda.models.user.UserResponse;
import com.ecommerce.davivienda.models.user.UserUpdateRequest;
import com.ecommerce.davivienda.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para operaciones CRUD sobre usuarios.
 * Expone endpoints para gestión completa de usuarios del sistema.
 * 
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Crea un nuevo usuario.
     *
     * @param request Datos del usuario a crear
     * @return Response con mensaje de éxito
     */
    @PostMapping("/create")
    public ResponseEntity<Response<String>> createUser(
            @Valid @RequestBody UserRequest request) {
        log.info("POST /api/v1/users/create - Crear usuario: {}", request.getEmail());

        Response<String> response = userService.createUser(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    /**
     * Actualiza un usuario existente de forma parcial.
     * Si no se proporciona ID en el body, se actualiza el usuario autenticado del token JWT.
     * Si se proporciona ID, se valida ownership (solo puede actualizar sus propios datos).
     * Los campos que sean null no se actualizarán (se mantendrá el valor existente).
     *
     * @param request Datos a actualizar del usuario (solo campos no-null)
     * @return Response con mensaje de éxito
     */
    @PutMapping("/update")
    public ResponseEntity<Response<String>> updateUser(
            @Valid @RequestBody UserUpdateRequest request) {
        
        Integer userId = request.getId();
        
        if (userId == null) {
            log.info("PUT /api/v1/users/update - Actualizar usuario autenticado (ID del token JWT)");
        } else {
            log.info("PUT /api/v1/users/update - Actualizar usuario con ID: {}", userId);
        }

        Response<String> response = userService.updateUser(userId, request);

        return ResponseEntity.ok(response);
    }

    /**
     * Recuperación de contraseña (PÚBLICO - sin autenticación).
     * Cambia la contraseña y envía correo de notificación si envioCorreo=true.
     * Este endpoint NO requiere token JWT.
     *
     * @param request Request con email, nueva contraseña y bandera envioCorreo
     * @return Response con mensaje de éxito
     */
    @PatchMapping("/change-password")
    public ResponseEntity<Response<String>> recoverPassword(
            @Valid @RequestBody PasswordRecoveryRequest request) {
        log.info("PATCH /api/v1/users/change-password - Recuperación de contraseña para: {} (envioCorreo={})", 
                request.getEmail(), request.getEnvioCorreo());

        Response<String> response = userService.recoverPassword(
                request.getEmail(),
                request.getNewPassword(),
                request.getEnvioCorreo()
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Cambio de contraseña (PRIVADO - requiere autenticación).
     * Valida que el usuario autenticado sea el dueño del email.
     * Este endpoint REQUIERE token JWT válido.
     *
     * @param request Request con email y nueva contraseña
     * @return Response con mensaje de éxito
     */
    @PatchMapping("/change-password-authenticated")
    public ResponseEntity<Response<String>> changePasswordAuthenticated(
            @Valid @RequestBody PasswordChangeAuthenticatedRequest request) {
        log.info("PATCH /api/v1/users/change-password-authenticated - Cambio de contraseña autenticado para: {}", 
                request.getEmail());

        Response<String> response = userService.changePasswordAuthenticated(
                request.getEmail(),
                request.getNewPassword()
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene todos los usuarios del sistema.
     * Retorna información pública sin datos sensibles como contraseñas.
     *
     * @return Response con lista de usuarios
     */
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('Administrador')")
    public ResponseEntity<Response<List<UserResponse>>> getAllUsers() {
        log.info("GET /api/v1/users/all - Obtener todos los usuarios");

        Response<List<UserResponse>> response = userService.getAllUsers();

        return ResponseEntity.ok(response);
    }

    /**
     * Busca un usuario por su ID.
     *
     * @param id ID del usuario
     * @return Response con datos del usuario
     */
    @GetMapping("/{id}")
    public ResponseEntity<Response<UserResponse>> getUserById(@PathVariable Integer id) {
        log.info("GET /api/v1/users/{} - Buscar usuario por ID", id);

        Response<UserResponse> response = userService.getUserById(id);

        return ResponseEntity.ok(response);
    }
    
    /**
     * Obtiene el usuario autenticado desde el token JWT.
     * No requiere parámetros, usa el userRoleId extraído del token automáticamente.
     *
     * @return Response con datos del usuario autenticado
     */
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Response<UserResponse>> getAuthenticatedUser() {
        log.info("GET /api/v1/users/me - Obtener usuario autenticado");

        Response<UserResponse> response = userService.getAuthenticatedUser();

        return ResponseEntity.ok(response);
    }
}

