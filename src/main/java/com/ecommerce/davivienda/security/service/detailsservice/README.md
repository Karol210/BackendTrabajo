# 📋 UserDetails Service - Servicios de Autenticación

## 📂 Estructura de Carpetas

```
security/service/detailsservice/
├── validation/                                    ✅ Capacidad: Validación de Usuario
│   ├── JpaUserValidationService.java             (Interface)
│   └── JpaUserValidationServiceImpl.java         (Implementación)
│
├── builder/                                       🔨 Capacidad: Construcción de UserDetails
│   ├── UserDetailsBuilderService.java            (Interface)
│   └── UserDetailsBuilderServiceImpl.java        (Implementación)
│
├── JpaUserDetailsService.java                    (Interface principal)
└── JpaUserDetailsServiceImpl.java                (Servicio coordinador - Implementación)
```

## 🎯 Responsabilidades por Capacidad

### 1️⃣ Validation - Validación de Usuario

**Ubicación**: `validation/`

**Responsabilidad**: Validar el estado del usuario y su rol antes de autenticación.

**Clases**:
- `JpaUserValidationService` (Interface)
- `JpaUserValidationServiceImpl` (Implementación)

**Métodos**:
- `validateUserStatus(User user, String email)` - Valida usuario y rol activos
- `validateUserActive(User user, String email)` - Valida usuario activo (privado)
- `validateUserRole(User user, String email)` - Valida rol activo (privado)

**Excepciones lanzadas**:
- `UsernameNotFoundException` - Usuario inactivo (`CODE_USER_INACTIVE`)
- `UsernameNotFoundException` - Sin rol activo (`CODE_USER_NO_ACTIVE_ROLE`)

---

### 2️⃣ Builder - Construcción de UserDetails

**Ubicación**: `builder/`

**Responsabilidad**: Transformar entidades `User` del dominio en objetos `UserDetails` de Spring Security.

**Clases**:
- `UserDetailsBuilderService` (Interface)
- `UserDetailsBuilderServiceImpl` (Implementación)

**Métodos**:
- `buildUserDetails(User user)` - Construye UserDetails completo
- `buildAuthorities(User user)` - Construye lista de autoridades (privado)

**Configuración UserDetails**:
- `username` → `user.getEmail()`
- `password` → `user.getPassword()`
- `authorities` → Rol del usuario (`SimpleGrantedAuthority`)
- `disabled` → `!user.getActive()`
- `accountExpired` → `false`
- `accountLocked` → `false`
- `credentialsExpired` → `false`

---

### 3️⃣ Coordinador Principal - JpaUserDetailsService

**Ubicación**: Raíz de `detailsservice/`

**Responsabilidad**: Coordinar el flujo completo de carga de usuario para Spring Security.

**Clases**:
- `JpaUserDetailsService` (Interface propia)
- `JpaUserDetailsServiceImpl` (Implementación + implementa `UserDetailsService` de Spring)

**Flujo de ejecución**:
```java
loadUserByUsername(email)
    ↓
1. findUserByEmail(email)           // Búsqueda en BD
    ↓
2. validationService.validateUserStatus(user, email)  // Delega a Validation
    ↓
3. builderService.buildUserDetails(user)              // Delega a Builder
    ↓
4. return UserDetails
```

**Métodos**:
- `loadUserByUsername(String email)` - Método principal (implementa `UserDetailsService`)
- `findUserByEmail(String email)` - Busca usuario en BD (privado)

**Excepciones lanzadas**:
- `UsernameNotFoundException` - Usuario no encontrado (`CODE_USER_NOT_FOUND`)

---

## 🔄 Flujo de Autenticación

```
Usuario envía credenciales
    ↓
JwtAuthenticationFilter
    ↓
AuthenticationManager
    ↓
JpaUserDetailsService.loadUserByUsername()
    ↓
┌─────────────────────────────────────────────┐
│ 1. Buscar usuario en BD                     │
│    ├─ Éxito → Continuar                     │
│    └─ Error → UsernameNotFoundException     │
└─────────────────────────────────────────────┘
    ↓
┌─────────────────────────────────────────────┐
│ 2. Validar estado (Validation Service)      │
│    ├─ Usuario activo? → Continuar           │
│    ├─ Usuario inactivo? → Exception         │
│    ├─ Rol activo? → Continuar               │
│    └─ Sin rol activo? → Exception           │
└─────────────────────────────────────────────┘
    ↓
┌─────────────────────────────────────────────┐
│ 3. Construir UserDetails (Builder Service)  │
│    ├─ Extraer email, password               │
│    ├─ Construir autoridades (roles)         │
│    └─ Configurar estado de cuenta           │
└─────────────────────────────────────────────┘
    ↓
Retorna UserDetails
    ↓
AuthenticationManager valida password
    ↓
JwtTokenGenerator crea token JWT
    ↓
Response con token
```

---

## 📊 Códigos de Error

| Código | Constante | Descripción | Servicio |
|--------|-----------|-------------|----------|
| `ED-USR-01` | `CODE_USER_NOT_FOUND` | Usuario no encontrado en BD | Coordinador |
| `ED-USR-02` | `CODE_USER_INACTIVE` | Usuario inactivo | Validation |
| `ED-USR-03` | `CODE_USER_NO_ACTIVE_ROLE` | Usuario sin rol activo | Validation |

---

## 🧪 Testing

### Tests Unitarios Recomendados

#### `JpaUserValidationServiceImplTest`
```java
- validateUserStatus_userActive_success()
- validateUserStatus_userInactive_throwsException()
- validateUserStatus_userWithoutRole_throwsException()
- validateUserStatus_userWithInactiveRole_throwsException()
```

#### `UserDetailsBuilderServiceImplTest`
```java
- buildUserDetails_activeUser_success()
- buildUserDetails_inactiveUser_disabledAccount()
- buildUserDetails_userWithRole_correctAuthorities()
```

#### `JpaUserDetailsServiceImplTest`
```java
- loadUserByUsername_existingUser_success()
- loadUserByUsername_nonExistingUser_throwsException()
- loadUserByUsername_inactiveUser_throwsException()
```

---

## 📦 Dependencias Inyectadas

### En JpaUserDetailsServiceImpl
```java
private final UserRepository userRepository;           // Búsqueda en BD
private final JpaUserValidationService validationService;  // Capacidad: Validación
private final UserDetailsBuilderService builderService;    // Capacidad: Builder
```

---

## ✅ Principios Aplicados

| Principio | Implementación |
|-----------|----------------|
| **Single Responsibility** | Cada servicio tiene una responsabilidad única |
| **Open/Closed** | Fácil extender sin modificar código existente |
| **Dependency Inversion** | Inyección por interfaces |
| **Alta Cohesión** | Código relacionado agrupado por capacidad |
| **Bajo Acoplamiento** | Cambios en una capacidad no afectan otras |

---

## 🚀 Uso

### Inyección en Spring Security

El servicio se configura automáticamente en Spring Security gracias a la implementación de `UserDetailsService`:

```java
@Service
public class JpaUserDetailsServiceImpl implements UserDetailsService {
    // Spring Security detecta automáticamente esta implementación
}
```

### Configuración Adicional (Opcional)

Si necesitas configurar explícitamente:

```java
@Configuration
public class SecurityConfig {
    
    @Bean
    public DaoAuthenticationProvider authenticationProvider(
            JpaUserDetailsServiceImpl userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }
}
```

---

## 📝 Notas Importantes

1. **Transaccionalidad**: `loadUserByUsername()` está marcado como `@Transactional(readOnly = true)` para optimizar lecturas.

2. **Logs**: 
   - `DEBUG` - Flujo normal de autenticación
   - `WARN` - Usuarios no encontrados, inactivos o sin rol

3. **Performance**: La búsqueda por email debe tener un índice en la BD para optimizar consultas.

4. **Extensibilidad**: Para agregar nuevas validaciones, crear nueva capacidad en `validation/rules/` o `validation/advanced/`.

---

## 🔗 Referencias

- **Spring Security UserDetailsService**: Interface estándar de Spring Security
- **Constants.java**: Códigos y mensajes de error centralizados
- **User Entity**: `com.ecommerce.davivienda.entity.user.User`
- **Role Entity**: `com.ecommerce.davivienda.entity.role.Role`

---

**Última actualización**: 2025-01-12  
**Autor**: Team Ecommerce Davivienda  
**Versión**: 1.0.0

