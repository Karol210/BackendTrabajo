package com.ecommerce.davivienda.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Programa simple para verificar el hash BCrypt.
 * Ejecutar desde el IDE (Run 'SimplePasswordTest.main()')
 */
public class SimplePasswordTest {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        System.out.println("==============================================");
        System.out.println("Test de Verificación de Contraseña BCrypt");
        System.out.println("==============================================");
        System.out.println();
        
        // Test 1: Verificar hash del admin
        String adminPlainPassword = "admin123";
        String adminHashFromDB = "$2a$10$N9qo8uLOickgx2ZMRZoMy.fdjKIHIqI1OxCzUVGOq9VXv2mMQhvDO";
        
        boolean adminMatches = encoder.matches(adminPlainPassword, adminHashFromDB);
        
        System.out.println("🔐 TEST 1: Usuario Admin");
        System.out.println("   Contraseña plana: " + adminPlainPassword);
        System.out.println("   Hash en BD:       " + adminHashFromDB);
        System.out.println("   ¿Coincide?:       " + (adminMatches ? "✅ SÍ" : "❌ NO"));
        System.out.println();
        
        // Test 2: Verificar hash del user
        String userPlainPassword = "user123";
        String userHashFromDB = "$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi";
        
        boolean userMatches = encoder.matches(userPlainPassword, userHashFromDB);
        
        System.out.println("🔐 TEST 2: Usuario Normal");
        System.out.println("   Contraseña plana: " + userPlainPassword);
        System.out.println("   Hash en BD:       " + userHashFromDB);
        System.out.println("   ¿Coincide?:       " + (userMatches ? "✅ SÍ" : "❌ NO"));
        System.out.println();
        
        // Test 3: Generar nuevo hash
        String newHash = encoder.encode(adminPlainPassword);
        System.out.println("🔧 TEST 3: Generar Nuevo Hash");
        System.out.println("   Contraseña:       " + adminPlainPassword);
        System.out.println("   Nuevo Hash:       " + newHash);
        System.out.println();
        
        // Resumen
        System.out.println("==============================================");
        System.out.println("RESUMEN:");
        System.out.println("   Admin: " + (adminMatches ? "✅ HASH CORRECTO" : "❌ HASH INCORRECTO"));
        System.out.println("   User:  " + (userMatches ? "✅ HASH CORRECTO" : "❌ HASH INCORRECTO"));
        System.out.println("==============================================");
        
        if (adminMatches && userMatches) {
            System.out.println();
            System.out.println("✅ CONCLUSIÓN: Los hashes en la BD son correctos.");
            System.out.println("   El problema debe estar en la configuración de Spring Security.");
        } else {
            System.out.println();
            System.out.println("❌ PROBLEMA: Los hashes NO coinciden.");
            System.out.println("   Actualiza la BD con los nuevos hashes.");
        }
    }
}

