# 🔐 Guía de Validación y Extracción de Información de Tokens JWT

## 📋 TL;DR

Ya tienes implementado un sistema completo de validación JWT con dos enfoques:
- ✅ **Validación Automática**: En filtros (para todos los endpoints protegidos)
- ✅ **Validación Manual**: Servicio `TokenInfoService` (para lógica de negocio)

---

## 🎯 Dos Formas de Trabajar con Tokens JWT

### 1️⃣ Validación Automática (Filtros - Ya funciona)

El token se valida automáticamente en **todos los endpoints protegidos**:

```java
// Automático en JwtValidationFilter.java
Claims claims = tokenValidator.validateAndParseToken(token);
String userName = claims.getSubject();
Collection<? extends GrantedAuthority> authorities = tokenValidator.extractAuthorities(claims);
```

**Endpoints protegidos:**
- `GET /api/v1/users`
- `POST /api/v1/products`
- `PUT /api/v1/payments/{id}`
- Todos excepto `/api/v1/auth/login`

---

### 2️⃣ Validación Manual (Servicios - Nuevo)

Para extraer información del token en tu lógica de negocio:

#### ✅ Opción A: Inyectar `TokenInfoService` (Recomendado)

```java
@Service
@RequiredArgsConstructor
public class MiServicio {
    
    private final TokenInfoService tokenInfoService;

    public void procesarDatos(@RequestHeader("Authorization") String authorization) {
        // Extraer token del header
        String token = authorization.substring(7); // Quitar "Bearer "

        // 1. Obtener toda la información
        Map<String, Object> tokenInfo = tokenInfoService.getTokenInfo(token);
        System.out.println(tokenInfo);
        // {
        //   "username": "john@example.com",
        //   "authorities": ["ROLE_ADMIN", "ROLE_USER"],
        //   "issuedAt": "2025-11-13T10:00:00",
        //   "expiration": "2025-11-13T18:00:00",
        //   "isExpired": false
        // }

        // 2. Solo extraer el username
        String username = tokenInfoService.extractUsername(token);
        // → "john@example.com"

        // 3. Solo extraer los roles/authorities
        Collection<? extends GrantedAuthority> authorities = tokenInfoService.extractAuthorities(token);
        // → [ROLE_ADMIN, ROLE_USER]

        // 4. Validar si el token es válido
        boolean isValid = tokenInfoService.isTokenValid(token);
        // → true o false

        // 5. Obtener todos los claims
        Claims claims = tokenInfoService.extractClaims(token);
        String email = (String) claims.get("email"); // Claim personalizado
    }
}
```

#### ✅ Opción B: Usar `JwtTokenValidator` directamente

```java
@Service
@RequiredArgsConstructor
public class MiServicio {
    
    private final JwtTokenValidator jwtTokenValidator;

    public void procesarDatos(String token) {
        try {
            // Validar y extraer claims
            Claims claims = jwtTokenValidator.validateAndParseToken(token);
            
            // Obtener username
            String username = claims.getSubject();
            
            // Obtener authorities
            Collection<? extends GrantedAuthority> authorities = 
                jwtTokenValidator.extractAuthorities(claims);
            
            // Obtener claims personalizados
            Object authoritiesRaw = claims.get("authorities");
            Date expiration = claims.getExpiration();
            Date issuedAt = claims.getIssuedAt();
            
        } catch (JwtException e) {
            // Token inválido o expirado
            log.error("Token inválido: {}", e.getMessage());
        } catch (IOException e) {
            // Error al deserializar authorities
            log.error("Error al procesar authorities: {}", e.getMessage());
        }
    }
}
```

---

## 🔗 Endpoints Disponibles (TokenInfoController)

### 1. Obtener Información Completa del Token

```bash
GET /api/v1/token/info
Authorization: Bearer {tu_token_jwt}
```

**Response 200 OK:**
```json
{
  "failure": false,
  "code": 200,
  "message": "Información del token obtenida exitosamente",
  "body": {
    "username": "john@example.com",
    "authorities": ["ROLE_ADMIN", "ROLE_USER"],
    "issuedAt": "2025-11-13T10:00:00.000+00:00",
    "expiration": "2025-11-13T18:00:00.000+00:00",
    "isExpired": false
  },
  "timestamp": "1699875600000"
}
```

**Response 401 Unauthorized:**
```json
{
  "failure": true,
  "code": 401,
  "message": "[ED-JWT-01] Token JWT inválido o expirado",
  "timestamp": "1699875600000"
}
```

---

### 2. Extraer Solo el Username

```bash
GET /api/v1/token/username
Authorization: Bearer {tu_token_jwt}
```

**Response 200 OK:**
```json
{
  "failure": false,
  "code": 200,
  "message": "Username extraído exitosamente",
  "body": "john@example.com",
  "timestamp": "1699875600000"
}
```

---

### 3. Extraer Solo los Authorities/Roles

```bash
GET /api/v1/token/authorities
Authorization: Bearer {tu_token_jwt}
```

**Response 200 OK:**
```json
{
  "failure": false,
  "code": 200,
  "message": "Authorities extraídas exitosamente",
  "body": [
    {
      "authority": "ROLE_ADMIN"
    },
    {
      "authority": "ROLE_USER"
    }
  ],
  "timestamp": "1699875600000"
}
```

---

### 4. Validar si el Token es Válido

```bash
GET /api/v1/token/validate
Authorization: Bearer {tu_token_jwt}
```

**Response 200 OK:**
```json
{
  "failure": false,
  "code": 200,
  "message": "Token válido",
  "body": true,
  "timestamp": "1699875600000"
}
```

---

## 📊 Comparación: ¿Cuándo usar cada opción?

| Escenario | Recomendación | Razón |
|-----------|---------------|-------|
| Endpoint protegido estándar | Validación Automática | Ya funciona, no requiere código adicional |
| Extraer username en servicio | `TokenInfoService.extractUsername()` | Método específico, fácil de usar |
| Extraer authorities en servicio | `TokenInfoService.extractAuthorities()` | Método específico, fácil de usar |
| Validar token en lógica personalizada | `TokenInfoService.isTokenValid()` | Valida sin lanzar excepciones |
| Obtener claims personalizados | `JwtTokenValidator.validateAndParseToken()` | Acceso completo a todos los claims |
| Debugging/inspección de tokens | `GET /api/v1/token/info` | Endpoint HTTP para testing |

---

## ⚠️ Diferencia: Base64 vs JWT

| Característica | Base64 (`Base64DecryptionService`) | JWT (`JwtTokenValidator`) |
|----------------|-----------------------------------|---------------------------|
| **Seguridad** | ❌ Solo codificación (inseguro) | ✅ Firma digital verificable |
| **Puede desencriptar?** | ✅ Sí (cualquiera puede) | ⚠️ Solo validar firma |
| **Uso** | Decodificar datos simples | Autenticación y autorización |
| **Ejemplo** | `SGVsbG8=` → `"Hello"` | Token firmado con `SECRET_KEY` |

---

## 🚀 Ejemplo Completo: Usar en tu Servicio

```java
package com.ecommerce.davivienda.service.example;

import com.ecommerce.davivienda.service.token.TokenInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExampleServiceImpl implements ExampleService {

    private final TokenInfoService tokenInfoService;

    @Override
    public void procesarPedido(String authorization, Long orderId) {
        // 1. Extraer token del header
        String token = extractToken(authorization);

        // 2. Validar que el token es válido
        if (!tokenInfoService.isTokenValid(token)) {
            throw new RuntimeException("Token inválido o expirado");
        }

        // 3. Extraer información del usuario
        String username = tokenInfoService.extractUsername(token);
        Collection<? extends GrantedAuthority> authorities = tokenInfoService.extractAuthorities(token);

        log.info("🔍 Usuario {} con roles {} procesando pedido {}", 
                username, authorities, orderId);

        // 4. Validar permisos específicos
        boolean isAdmin = authorities.stream()
                .anyMatch(auth -> "ROLE_ADMIN".equals(auth.getAuthority()));

        if (isAdmin) {
            log.info("✅ Usuario {} tiene permisos de administrador", username);
            // Lógica especial para admins
        }

        // 5. Procesar lógica de negocio
        // ...
    }

    private String extractToken(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Header Authorization inválido");
        }
        return authorization.substring(7);
    }
}
```

---

## 🎯 Resumen

✅ **Ya tienes todo implementado:**
- `JwtTokenValidator`: Validación y extracción de claims
- `TokenInfoService`: Servicio de alto nivel para casos de uso comunes
- `TokenInfoController`: Endpoints HTTP para inspeccionar tokens

✅ **Cómo usar:**
1. **Endpoints protegidos**: Automático (no requiere código)
2. **Lógica de negocio**: Inyecta `TokenInfoService` y llama métodos específicos
3. **Acceso a claims personalizados**: Usa `JwtTokenValidator` directamente
4. **Testing/Debugging**: Usa endpoints `/api/v1/token/*`

✅ **Base64 vs JWT:**
- Base64: Solo codificación (inseguro)
- JWT: Firma digital + claims (seguro)

---

## 📌 Archivos Creados

| Archivo | Descripción |
|---------|-------------|
| `TokenInfoService.java` | Interface con métodos para extraer información del token |
| `TokenInfoServiceImpl.java` | Implementación que usa `JwtTokenValidator` |
| `TokenInfoController.java` | Endpoints HTTP para inspeccionar tokens |
| `Constants.java` | Constantes de error agregadas |

**Ubicación:**
- `src/main/java/com/ecommerce/davivienda/service/token/`
- `src/main/java/com/ecommerce/davivienda/controller/token/`

