# 🏪 Tienda Digital Davivienda - Backend

API REST para la plataforma de ecommerce de Davivienda, construida con **Clean Architecture** y **Spring Boot 3**.

## 📋 Tabla de Contenidos

- [Arquitectura](#-arquitectura)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Tecnologías](#-tecnologías)
- [Configuración](#-configuración)
- [Ejecución](#-ejecución)
- [Desarrollo](#-desarrollo)
- [Testing](#-testing)
- [Despliegue](#-despliegue)

---

## 🏗️ Arquitectura

Este proyecto sigue los principios de **Clean Architecture** con separación clara de responsabilidades en capas:

```
┌─────────────────────────────────────────────┐
│         Controller (Presentation)           │  ← API REST Endpoints
├─────────────────────────────────────────────┤
│     Service (Business Logic)                │  ← Lógica de Negocio
│     ├── Principal Services                  │
│     ├── External Services (APIs externas)   │
│     └── Organization Services (internos)    │
├─────────────────────────────────────────────┤
│     Repository (Data Access)                │  ← Acceso a Datos
│     ├── JPA (PostgreSQL)                    │
│     └── MongoDB                             │
└─────────────────────────────────────────────┘
```

### Flujo de Datos

```
Request → Controller → Service Principal → External Service → Feign Client → API Externa
                           ↓
                    Validación de Negocio
                           ↓
                       Repository → Database
                           ↓
Response ← DTO Mapper ← Entity/Document
```

---

## 📁 Estructura del Proyecto

```
src/main/java/com/ecommerce/davivienda/
│
├── 🌐 controller/              # Capa de Presentación (API REST)
│   └── {dominio}/              # Controladores por dominio
│       └── {Dominio}Controller.java
│
├── 💼 service/                 # Capa de Lógica de Negocio
│   └── {dominio}/
│       ├── {Dominio}Service.java           (Interface)
│       ├── {Dominio}ServiceImpl.java       (Implementación principal)
│       ├── external/                       (Servicios externos)
│       │   ├── {capacidad}/                (Organización por capacidad)
│       │   │   ├── External*Client.java    (Feign Client)
│       │   │   ├── External*Service.java   (Interface)
│       │   │   └── External*ServiceImpl.java
│       │   └── ...
│       └── organization/                   (Servicios internos - NO expuestos)
│           ├── *OrganizationService.java   (Interface)
│           └── *OrganizationServiceImpl.java
│
├── 📦 dto/                     # Data Transfer Objects
│   └── {dominio}/
│       ├── {capacidad}/        (Organización por capacidad)
│       │   ├── *RequestDto.java
│       │   ├── *ResponseDto.java
│       │   └── *DataDto.java
│       └── ...
│
├── 🗂️ entity/                  # Entidades JPA (PostgreSQL)
│   └── {dominio}/
│       └── {Entity}.java
│
├── 📄 documents/               # Documentos MongoDB
│   └── {dominio}/
│       └── {Document}.java
│
├── 🗄️ repository/              # Repositorios de Acceso a Datos
│   ├── jpa/                    # JPA Repositories
│   │   └── {Entity}Repository.java
│   └── mongodb/                # MongoDB Repositories
│       └── {Document}Repository.java
│
├── 🔄 mapper/                  # Mappers (DTO ↔ Entity/Document)
│   └── {dominio}/
│       └── {Dominio}Mapper.java
│
├── ⚠️ exception/               # Excepciones Personalizadas
│   ├── {modulo}/               (Excepciones específicas por módulo)
│   │   └── {Modulo}Exception.java
│   └── ExceptionHandlerController.java
│
├── ⚙️ config/                  # Configuraciones
│   ├── FeignClientConfig.java  (Configuración transversal Feign)
│   ├── SecurityConfig.java     (Spring Security)
│   └── JacksonConfig.java      (Serialización JSON)
│
├── 📊 models/                  # Modelos Genéricos
│   └── Response.java           (Respuesta estándar API)
│
├── 🔧 util/                    # Utilidades y Helpers
│   └── ...
│
└── 📌 constants/               # Constantes
    └── Constants.java          (Mensajes, códigos de error)
```

### Organización por Capacidades

Cuando un módulo tiene **≥2 capacidades independientes**, se organizan en subcarpetas:

```
service/{dominio}/external/
├── auth/                       # Capacidad: Autenticación
│   ├── CognitoAuthClient.java
│   └── CognitoAuthServiceImpl.java
└── questions/                  # Capacidad: Consultas
    ├── External*QuestionsClient.java
    └── External*QuestionsServiceImpl.java

dto/{dominio}/
├── auth/
│   └── TokenResponseDto.java
└── questions/
    ├── QuestionsRequestDto.java
    └── QuestionsResponseDto.java
```

**Beneficios:**
- ✅ **Alta cohesión**: Archivos relacionados juntos
- ✅ **Bajo acoplamiento**: Cambios aislados por capacidad
- ✅ **Escalabilidad**: Agregar nuevas capacidades sin afectar existentes

---

## 🛠️ Tecnologías

### Core
- **Java 17** - Lenguaje
- **Spring Boot 3.5.7** - Framework
- **Gradle** - Gestión de dependencias

### Frameworks y Librerías
- **Spring Data JPA** - Persistencia PostgreSQL
- **Spring Data MongoDB** - Persistencia MongoDB
- **Spring Security** - Seguridad
- **Spring Cloud OpenFeign** - Cliente HTTP para APIs externas
- **Lombok** - Reducción de boilerplate
- **MapStruct** - Mapeo de DTOs
- **Jackson** - Serialización JSON

### Bases de Datos
- **PostgreSQL** - Base de datos relacional
- **MongoDB** - Base de datos NoSQL

### Testing
- **JUnit 5** - Framework de testing
- **Mockito** - Mocking
- **Spring Boot Test** - Testing integrado

---

## ⚙️ Configuración

### Requisitos Previos

- Java 17+
- PostgreSQL 12+
- MongoDB 4.4+
- Gradle 8+ (incluido con wrapper)

### Variables de Entorno

Crear archivo `.env` en la raíz del proyecto:

```bash
# Database
DB_URL=jdbc:postgresql://localhost:5432/tienda_digital
DB_USERNAME=postgres
DB_PASSWORD=postgres

# MongoDB
MONGODB_URI=mongodb://localhost:27017/tienda_digital

# Active Profile
SPRING_PROFILES_ACTIVE=dev
```

### Configuración de Base de Datos

#### PostgreSQL

```sql
CREATE DATABASE tienda_digital_dev;
CREATE USER tienda_user WITH ENCRYPTED PASSWORD 'tienda_pass';
GRANT ALL PRIVILEGES ON DATABASE tienda_digital_dev TO tienda_user;
```

#### MongoDB

```bash
mongosh
use tienda_digital_dev
db.createUser({
  user: "tienda_user",
  pwd: "tienda_pass",
  roles: [{role: "readWrite", db: "tienda_digital_dev"}]
})
```

---

## 🚀 Ejecución

### Desarrollo Local

```bash
# Ejecutar con perfil de desarrollo
./gradlew bootRun --args='--spring.profiles.active=dev'

# O con variables de entorno
export SPRING_PROFILES_ACTIVE=dev
./gradlew bootRun
```

### Build

```bash
# Compilar proyecto
./gradlew build

# Compilar sin tests
./gradlew build -x test

# Generar JAR
./gradlew bootJar
```

### Ejecutar JAR

```bash
java -jar build/libs/davivienda-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
```

---

## 👨‍💻 Desarrollo

### Crear un Nuevo Módulo

Sigue estos pasos para crear un nuevo módulo funcional:

#### 1. Crear DTOs

```
dto/payment/
├── PaymentRequestDto.java
└── PaymentResponseDto.java
```

#### 2. Crear Servicios

**Servicio Principal:**
```
service/payment/
├── PaymentService.java           (Interface)
└── PaymentServiceImpl.java       (Implementación)
```

**Servicio External (si consume API externa):**
```
service/payment/external/
├── ExternalPaymentClient.java    (Feign Client)
├── ExternalPaymentService.java   (Interface)
└── ExternalPaymentServiceImpl.java
```

#### 3. Crear Controller (solo si se expone API)

```
controller/payment/
└── PaymentController.java
```

#### 4. Crear Excepción Personalizada

```
exception/payment/
└── PaymentException.java
```

#### 5. Agregar Constantes

En `constants/Constants.java`:
```java
// Payment - Error Messages
public static final String ERROR_PAYMENT_FAILED = "Error al procesar el pago";

// Payment - Error Codes
public static final String CODE_PAYMENT_FAILED = "TDTC-PY-0001";
```

### Reglas de Desarrollo

#### ✅ SIEMPRE

1. **Verificar existentes** antes de crear DTOs/servicios/endpoints
2. **Inyección por interfaz** con `@RequiredArgsConstructor`
3. **Nomenclatura consistente**: `{Dominio}Service`, `{Dominio}Dto`
4. **JavaDoc completo** en clases y métodos públicos
5. **Logs informativos** en operaciones críticas
6. **Excepciones con códigos** formato `TDTC-XX-NNNN`

#### ❌ NUNCA

1. **NO** inyectar implementaciones (`*ServiceImpl`)
2. **NO** usar `@Autowired` (usar constructor)
3. **NO** hardcodear mensajes de error
4. **NO** exponer servicios internos (Organization) sin aprobación
5. **NO** crear servicios sin URL, CURL y responses JSON

### Nomenclatura de Feign Clients

| Elemento | Convención | Ejemplo |
|---|---|---|
| Nombre en `@FeignClient` | `{nombre}-client` | `payment-client` |
| Nombre interfaz | `External{Nombre}Client` | `ExternalPaymentClient` |
| URL property | `${services.{nombre}.url}` | `${services.payment.url}` |

---

## 🧪 Testing

### Ejecutar Tests

```bash
# Todos los tests
./gradlew test

# Tests específicos
./gradlew test --tests "com.ecommerce.davivienda.service.*"

# Con reporte de cobertura
./gradlew test jacocoTestReport
```

### Estructura de Tests

```
src/test/java/com/ecommerce/davivienda/
├── controller/
│   └── {Dominio}ControllerTest.java
├── service/
│   └── {dominio}/
│       └── {Dominio}ServiceImplTest.java
└── repository/
    └── {Entity}RepositoryTest.java
```

---

## 📦 Despliegue

### Perfiles Disponibles

| Perfil | Uso | Configuración |
|---|---|---|
| `dev` | Desarrollo local | `application-dev.yml` |
| `stage` | Staging | `application-stage.yml` |
| `prod` | Producción | `application-prod.yml` |

### Build para Producción

```bash
# Build con perfil de producción
./gradlew clean build -Pspring.profiles.active=prod

# Ejecutar JAR
java -jar build/libs/davivienda-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

### Docker (Próximamente)

```bash
# Build imagen
docker build -t tienda-digital:latest .

# Ejecutar contenedor
docker run -p 8080:8080 -e SPRING_PROFILES_ACTIVE=prod tienda-digital:latest
```

---

## 📚 Documentación Adicional

### 🚀 Inicio Rápido

- 📖 **[Guía Rápida de APIs](docs/QUICK_START_APIS.md)** - Inicio rápido con ejemplos completos de Usuario y Productos

### 👥 API de Usuarios

- 📘 **[Documentación Completa](docs/USER_API_UPDATED.md)** - Guía completa con credenciales separadas (v2.0)
- 📝 **[Colección de Curls](README_USUARIOS_API.md)** - Ejemplos rápidos para Postman

### 📦 API de Productos

- 📝 **[Colección de Curls](README_PRODUCTOS_API.md)** - Ejemplos rápidos para Postman
- 📘 **[Documentación Completa](docs/PRODUCT-CRUD-API.md)** - CRUD completo con filtros y paginación

### 🔐 Autenticación y Seguridad

- 🔑 **[Login con Credenciales](docs/LOGIN-CREDENTIALS-UPDATE.md)** - Estructura actualizada con credenciales separadas
- 🛡️ **[Arquitectura de Seguridad](docs/SECURITY-ARCHITECTURE.md)** - JWT y Spring Security

### 📊 Base de Datos

- 🗄️ **[Script de Estructura](src/main/resources/db/init-ecommerce.sql)** - Esquema completo PostgreSQL
- 👤 **[Usuario de Prueba](src/main/resources/db/seed-test-user.sql)** - Script para crear usuario admin

### 🏗️ Desarrollo

- 📐 **Reglas de Cursor**: Ver carpeta `.cursor/rules/` para estándares de desarrollo
- 📄 **API Docs**: Swagger UI disponible en `/swagger-ui.html` (próximamente)
- ❤️ **Actuator**: Health checks en `/actuator/health`

---

## 🤝 Contribución

1. Crear rama desde `develop`: `git checkout -b feature/GD917-XXXX-descripcion`
2. Desarrollar siguiendo las reglas de `.cursor/rules/`
3. Hacer commit con mensaje descriptivo
4. Push a rama remota
5. Crear Pull Request a `develop`

---

## 📝 Licencia

Propiedad de Davivienda - Todos los derechos reservados

---

## 👥 Equipo

**Team Tienda Digital**  
📧 Email: tienda-digital@davivienda.com

---

**Última actualización**: 2025-01-12  
**Versión**: 2.0.0 - Credenciales Separadas

