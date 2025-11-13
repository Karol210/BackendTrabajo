# 🔐 Módulo de Seguridad - Autenticación JWT

## 📋 Descripción

Módulo de seguridad organizado por **capacidades** que implementa autenticación basada en JWT (JSON Web Tokens) con Spring Security.

## 🏗️ Arquitectura - Organización por Capacidades

La arquitectura sigue el principio de **Separación de Responsabilidades (SRP)** organizando el código en capacidades especializadas:

```
security/
├── filter/                          🔧 Filtros (Coordinadores)
│   ├── JwtAuthenticationFilter      • Coordina el flujo de autenticación
│   └── JwtValidationFilter          • Valida tokens JWT en requests
│
├── credentials/                     📋 Capacidad: Extracción de Credenciales
│   └── CredentialsExtractor         • Extrae credenciales del request
│                                    • Crea tokens de autenticación
│
├── token/                           🔑 Capacidad: Generación de Tokens JWT
│   └── JwtTokenGenerator            • Genera tokens JWT
│                                    • Construye claims
│                                    • Serializa authorities
│
├── response/                        📤 Capacidad: Construcción de Respuestas
│   └── AuthenticationResponseBuilder • Construye respuestas HTTP (success/error)
│                                    • Agrega token a headers
│                                    • Serializa respuestas a JSON
│
├── service/                         👤 Servicios de Usuario
│   └── JpaUserDetailsService        • Carga detalles de usuario desde BD
│
└── util/                            ⚙️ Configuración
    └── TokenJwtConfig               • Configuración de JWT (SECRET_KEY, expiration)
```

## 🎯 Capacidades y Responsabilidades

### 1️⃣ Credentials - Extracción de Credenciales

**Responsabilidad**: Procesar credenciales del request HTTP

**Componente**: `CredentialsExtractor`

**Métodos**:
- `extractCredentials(HttpServletRequest)` → Extrae email y password del request
- `createAuthenticationToken(Map<String, String>)` → Crea token de Spring Security
- `extractEmail(Map<String, String>)` → Obtiene email de las credenciales

**Usado por**: `JwtAuthenticationFilter`

### 2️⃣ Token - Generación de Tokens JWT

**Responsabilidad**: Crear tokens JWT con claims y authorities

**Componente**: `JwtTokenGenerator`

**Métodos**:
- `generateToken(String userName, Collection<GrantedAuthority>)` → Genera token completo
- `serializeAuthorities(Collection)` → Convierte authorities a JSON
- `buildClaims(String, String)` → Construye claims del token
- `buildJwtToken(String, Claims)` → Construye y firma el token JWT

**Usado por**: `JwtAuthenticationFilter`

### 3️⃣ Response - Construcción de Respuestas HTTP

**Responsabilidad**: Crear respuestas HTTP para autenticación

**Componente**: `AuthenticationResponseBuilder`

**Métodos**:
- `writeSuccessResponse(HttpServletResponse, String, String)` → Escribe respuesta exitosa (200)
- `writeErrorResponse(HttpServletResponse, AuthenticationException)` → Escribe respuesta de error (401)
- `addTokenToHeader(HttpServletResponse, String)` → Agrega token al header Authorization
- `buildSuccessBody(String, String)` → Construye body de éxito
- `buildErrorBody(AuthenticationException)` → Construye body de error

**Usado por**: `JwtAuthenticationFilter`

## 🔄 Flujo de Autenticación

```
┌─────────────────────────────────────────────────────────────────────┐
│                    1. Usuario envía credenciales                    │
│                      POST /api/v1/auth/login                        │
│                   { "email": "...", "password": "..." }             │
└─────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────┐
│              JwtAuthenticationFilter (Coordinador)                  │
│  • attemptAuthentication() → Intenta autenticar                     │
└─────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────┐
│           📋 CredentialsExtractor (Capacidad)                       │
│  1. extractCredentials(request) → Lee JSON del request              │
│  2. createAuthenticationToken(credentials) → Crea token Spring      │
└─────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────┐
│                  AuthenticationManager                              │
│  • Valida credenciales contra UserDetailsService                    │
│  • Verifica password con BCryptPasswordEncoder                      │
└─────────────────────────────────────────────────────────────────────┘
                                    │
                        ┌───────────┴───────────┐
                        │                       │
                        ▼                       ▼
                    ✅ ÉXITO                ❌ ERROR
                        │                       │
                        ▼                       ▼
┌───────────────────────────────────┐  ┌───────────────────────────────────┐
│  successfulAuthentication()       │  │  unsuccessfulAuthentication()     │
├───────────────────────────────────┤  ├───────────────────────────────────┤
│  1. 🔑 JwtTokenGenerator          │  │  1. 📤 AuthenticationResponseBuilder│
│     • generateToken(userName,     │  │     • writeErrorResponse()        │
│       authorities)                │  │                                   │
│     • Serializa authorities       │  │  Response 401:                    │
│     • Construye claims            │  │  {                                │
│     • Firma token                 │  │    "message": "Error en la        │
│                                   │  │      autenticación: credenciales  │
│  2. 📤 AuthenticationResponseBuilder│  │      incorrectas",                │
│     • addTokenToHeader()          │  │    "error": "..."                 │
│     • writeSuccessResponse()      │  │  }                                │
│                                   │  └───────────────────────────────────┘
│  Response 200:                    │
│  {                                │
│    "token": "eyJhbGc...",         │
│    "username": "user@mail.com",   │
│    "message": "Hola user, has     │
│      iniciado sesión con éxito",  │
│    "expiresIn": 3600000           │
│  }                                │
└───────────────────────────────────┘
```

## 🔧 Configuración en SecurityConfig

```java
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    
    // ✅ Inyección de capacidades especializadas
    private final CredentialsExtractor credentialsExtractor;
    private final JwtTokenGenerator tokenGenerator;
    private final AuthenticationResponseBuilder responseBuilder;
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .addFilter(new JwtAuthenticationFilter(
                authenticationManager(),
                credentialsExtractor,      // 📋 Capacidad: Credentials
                tokenGenerator,            // 🔑 Capacidad: Token Generation
                responseBuilder            // 📤 Capacidad: Response Building
            ))
            .build();
    }
}
```

## ✅ Beneficios de la Organización por Capacidades

| Beneficio | Descripción |
|-----------|-------------|
| **Alta Cohesión** | Cada capacidad agrupa lógica relacionada (ej: todo lo de tokens en `token/`) |
| **Bajo Acoplamiento** | Capacidades independientes que se pueden modificar sin afectar otras |
| **Single Responsibility** | Cada clase tiene UNA responsabilidad única y bien definida |
| **Testeable** | Cada capacidad se puede testear de forma aislada con mocks |
| **Mantenible** | Fácil encontrar y modificar código específico por capacidad |
| **Escalable** | Agregar nuevas capacidades sin modificar código existente |
| **Reutilizable** | Capacidades se pueden usar en otros contextos si es necesario |

## 🧪 Testing por Capacidad

Cada capacidad tiene tests independientes:

```
test/security/
├── credentials/
│   └── CredentialsExtractorTest.java
│       • testExtractCredentials_Success()
│       • testExtractCredentials_IOException()
│       • testCreateAuthenticationToken()
│
├── token/
│   └── JwtTokenGeneratorTest.java
│       • testGenerateToken_Success()
│       • testSerializeAuthorities()
│       • testBuildClaims()
│
└── response/
    └── AuthenticationResponseBuilderTest.java
        • testWriteSuccessResponse()
        • testWriteErrorResponse()
        • testAddTokenToHeader()
```

## 📏 Métricas de Mejora

### Antes de la Refactorización

| Archivo | Líneas | Responsabilidades | Testeable |
|---------|--------|-------------------|-----------|
| `JwtAuthenticationFilter` | 186 | 6 (Credentials + Token + Response + Filtro) | ❌ Difícil |

### Después de la Refactorización

| Archivo | Líneas | Responsabilidades | Testeable |
|---------|--------|-------------------|-----------|
| `JwtAuthenticationFilter` | 101 | 1 (Solo coordinación) | ✅ Fácil |
| `CredentialsExtractor` | 69 | 1 (Extracción de credenciales) | ✅ Fácil |
| `JwtTokenGenerator` | 99 | 1 (Generación de tokens) | ✅ Fácil |
| `AuthenticationResponseBuilder` | 124 | 1 (Construcción de respuestas) | ✅ Fácil |

**Resultado**: 
- ✅ **46% reducción** en líneas del filtro principal (186 → 101)
- ✅ **4 componentes especializados** con responsabilidad única
- ✅ **100% testeable** de forma independiente

## 🚀 Uso en Desarrollo

### Modificar Estructura del Token

Editar solo: `token/JwtTokenGenerator.java`

```java
private Claims buildClaims(String userName, String authoritiesJson) {
    return Jwts.claims()
        .add(FIELD_AUTHORITIES, authoritiesJson)
        .add(FIELD_USERNAME, userName)
        .add("customField", "customValue")  // ✅ Agregar nuevo claim
        .build();
}
```

### Cambiar Formato de Respuesta

Editar solo: `response/AuthenticationResponseBuilder.java`

```java
private Map<String, Object> buildSuccessBody(String token, String userName) {
    Map<String, Object> body = new HashMap<>();
    body.put(FIELD_TOKEN, token);
    body.put(FIELD_USERNAME, userName);
    body.put("customField", "customValue");  // ✅ Agregar campo
    return body;
}
```

### Agregar Validación de Credenciales

Editar solo: `credentials/CredentialsExtractor.java`

```java
public Map<String, String> extractCredentials(HttpServletRequest request) {
    Map<String, String> credentials = /* ... */;
    
    // ✅ Agregar validación
    if (credentials.get(FIELD_EMAIL) == null) {
        throw new IllegalArgumentException("Email es obligatorio");
    }
    
    return credentials;
}
```

## 🔗 Referencias

> Ver también:
> - [servicios-01-creacion-servicios.mdc](../../../../../.cursor/rules/servicios-01-creacion-servicios.mdc) - Arquitectura en capas
> - [servicios-09-organizacion-capacidades.mdc](../../../../../.cursor/rules/servicios-09-organizacion-capacidades.mdc) - Organización por capacidades
> - [servicios-10-buenas-practicas.mdc](../../../../../.cursor/rules/servicios-10-buenas-practicas.mdc) - Clean Code y SRP

---

**Última actualización**: 2025-11-12  
**Autor**: Team Tienda Digital  
**Versión**: 2.0.0 (Refactorizada con organización por capacidades)

