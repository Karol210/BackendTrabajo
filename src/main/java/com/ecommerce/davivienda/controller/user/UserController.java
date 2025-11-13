package com.ecommerce.davivienda.controller.user;

import com.ecommerce.davivienda.constants.Constants;
import com.ecommerce.davivienda.dto.user.PasswordChangeRequestDto;
import com.ecommerce.davivienda.dto.user.UserRequestDto;
import com.ecommerce.davivienda.dto.user.UserResponseDto;
import com.ecommerce.davivienda.dto.user.UserUpdateRequestDto;
import com.ecommerce.davivienda.models.Response;
import com.ecommerce.davivienda.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Response<Void>> createUser(
            @Valid @RequestBody UserRequestDto request) {
        log.info("POST /api/v1/users/create - Crear usuario: {}", request.getEmail());

        userService.createUser(request);

        Response<Void> response = Response.<Void>builder()
                .failure(false)
                .code(HttpStatus.CREATED.value())
                .message(Constants.SUCCESS_USER_CREATED)
                .timestamp(String.valueOf(System.currentTimeMillis()))
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Obtiene un usuario por su ID.
     *
     * @param id ID del usuario
     * @return Response con el usuario encontrado
     */
    @GetMapping("/find-by-id/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response<UserResponseDto>> getUserById(@PathVariable Integer id) {
        log.info("GET /api/v1/users/find-by-id/{} - Consultar usuario", id);

        UserResponseDto user = userService.getUserById(id);

        Response<UserResponseDto> response = Response.<UserResponseDto>builder()
                .failure(false)
                .code(HttpStatus.OK.value())
                .message(Constants.SUCCESS_USER_FOUND)
                .body(user)
                .timestamp(String.valueOf(System.currentTimeMillis()))
                .build();

        return ResponseEntity.ok(response);
    }

    /**
     * Actualiza un usuario existente de forma parcial.
     * Solo el ID es obligatorio, los demás campos son opcionales.
     * Los campos que sean null no se actualizarán (se mantendrá el valor existente).
     *
     * @param request Datos a actualizar del usuario (solo campos no-null)
     * @return Response con el usuario actualizado
     */
    @PutMapping("/update")
    public ResponseEntity<Response<UserResponseDto>> updateUser(
            @Valid @RequestBody UserUpdateRequestDto request) {
        log.info("PUT /api/v1/users/update - Actualizar usuario con ID: {}", request.getId());

        UserResponseDto user = userService.updateUser(request.getId(), request);

        Response<UserResponseDto> response = Response.<UserResponseDto>builder()
                .failure(false)
                .code(HttpStatus.OK.value())
                .message(Constants.SUCCESS_USER_UPDATED)
                .body(user)
                .timestamp(String.valueOf(System.currentTimeMillis()))
                .build();

        return ResponseEntity.ok(response);
    }

    /**
     * Elimina un usuario (soft delete - marca como inactivo).
     *
     * @param id ID del usuario a eliminar
     * @return Response con el usuario eliminado
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response<UserResponseDto>> deleteUser(@PathVariable Integer id) {
        log.info("DELETE /api/v1/users/delete/{} - Eliminar usuario", id);

        UserResponseDto user = userService.deleteUser(id);

        Response<UserResponseDto> response = Response.<UserResponseDto>builder()
                .failure(false)
                .code(HttpStatus.OK.value())
                .message(Constants.SUCCESS_USER_DELETED)
                .body(user)
                .timestamp(String.valueOf(System.currentTimeMillis()))
                .build();

        return ResponseEntity.ok(response);
    }

    /**
     * Activa un usuario previamente desactivado.
     *
     * @param id ID del usuario a activar
     * @return Response con el usuario activado
     */
    @PatchMapping("/activate/{id}")
    public ResponseEntity<Response<UserResponseDto>> activateUser(@PathVariable Integer id) {
        log.info("PATCH /api/v1/users/activate/{} - Activar usuario", id);

        UserResponseDto user = userService.activateUser(id);

        Response<UserResponseDto> response = Response.<UserResponseDto>builder()
                .failure(false)
                .code(HttpStatus.OK.value())
                .message(Constants.SUCCESS_USER_ACTIVATED)
                .body(user)
                .timestamp(String.valueOf(System.currentTimeMillis()))
                .build();

        return ResponseEntity.ok(response);
    }

    /**
     * Cambia la contraseña de un usuario identificado por su email.
     * Valida que el usuario exista antes de cambiar la contraseña.
     *
     * @param passwordRequest Request con el email y la nueva contraseña
     * @return Response con confirmación
     */
    @PatchMapping("/change-password")
    public ResponseEntity<Response<UserResponseDto>> changePassword(
            @Valid @RequestBody PasswordChangeRequestDto passwordRequest) {
        log.info("PATCH /api/v1/users/change-password - Cambiar contraseña para: {}", 
                passwordRequest.getEmail());

        UserResponseDto user = userService.changePassword(
                passwordRequest.getEmail(),
                passwordRequest.getNewPassword()
        );

        Response<UserResponseDto> response = Response.<UserResponseDto>builder()
                .failure(false)
                .code(HttpStatus.OK.value())
                .message(Constants.SUCCESS_PASSWORD_CHANGED)
                .body(user)
                .timestamp(String.valueOf(System.currentTimeMillis()))
                .build();

        return ResponseEntity.ok(response);
    }
}

