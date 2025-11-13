package com.ecommerce.davivienda.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller temporal para generar hashes BCrypt.
 * ELIMINAR después de resolver el problema de autenticación.
 */
@RestController
@RequestMapping("/api/v1/debug")
@RequiredArgsConstructor
public class PasswordGeneratorController {

    private final PasswordEncoder passwordEncoder;

    /**
     * Genera un hash BCrypt para una contraseña.
     * 
     * @param password Contraseña en texto plano
     * @return Hash BCrypt generado
     */
    @GetMapping("/generate-hash")
    public Map<String, Object> generateHash(@RequestParam String password) {
        Map<String, Object> response = new HashMap<>();
        
        String hash = passwordEncoder.encode(password);
        boolean matches = passwordEncoder.matches(password, hash);
        
        response.put("password", password);
        response.put("hash", hash);
        response.put("length", hash.length());
        response.put("matches", matches);
        
        return response;
    }
    
    /**
     * Verifica si una contraseña coincide con un hash.
     * 
     * @param password Contraseña en texto plano
     * @param hash Hash BCrypt
     * @return true si coincide, false si no
     */
    @PostMapping("/verify-hash")
    public Map<String, Object> verifyHash(@RequestParam String password, @RequestParam String hash) {
        Map<String, Object> response = new HashMap<>();
        
        boolean matches = passwordEncoder.matches(password, hash);
        
        response.put("password", password);
        response.put("hash", hash);
        response.put("matches", matches);
        
        return response;
    }
}

