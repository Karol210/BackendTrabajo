package com.ecommerce.davivienda.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test para verificar que el hash de contraseña BCrypt es correcto.
 */
class PasswordEncoderTest {

    @Test
    void testAdminPasswordHash() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        String plainPassword = "admin123";
        String hashedPassword = "$2a$10$N9qo8uLOickgx2ZMRZoMy.fdjKIHIqI1OxCzUVGOq9VXv2mMQhvDO";
        
        boolean matches = encoder.matches(plainPassword, hashedPassword);
        
        System.out.println("==============================================");
        System.out.println("Test de Verificación de Contraseña BCrypt");
        System.out.println("==============================================");
        System.out.println("Contraseña plana: " + plainPassword);
        System.out.println("Hash en BD:       " + hashedPassword);
        System.out.println("¿Coincide?:       " + matches);
        System.out.println("==============================================");
        
        assertTrue(matches, "La contraseña admin123 debe coincidir con el hash");
    }
    
    @Test
    void testUserPasswordHash() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        String plainPassword = "user123";
        String hashedPassword = "$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi";
        
        boolean matches = encoder.matches(plainPassword, hashedPassword);
        
        System.out.println("==============================================");
        System.out.println("Test de Verificación de Contraseña BCrypt");
        System.out.println("==============================================");
        System.out.println("Contraseña plana: " + plainPassword);
        System.out.println("Hash en BD:       " + hashedPassword);
        System.out.println("¿Coincide?:       " + matches);
        System.out.println("==============================================");
        
        assertTrue(matches, "La contraseña user123 debe coincidir con el hash");
    }
    
    @Test
    void testGenerateNewHash() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        String password = "admin123";
        String newHash = encoder.encode(password);
        
        System.out.println("==============================================");
        System.out.println("Generar Nuevo Hash BCrypt");
        System.out.println("==============================================");
        System.out.println("Contraseña: " + password);
        System.out.println("Nuevo Hash: " + newHash);
        System.out.println("==============================================");
    }
}

