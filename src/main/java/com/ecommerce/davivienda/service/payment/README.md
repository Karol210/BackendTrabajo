# 💳 Módulo de Pagos - Procesamiento de Tarjetas

## 📋 Descripción

Módulo de procesamiento de pagos con tarjetas débito y crédito. Implementa arquitectura en capas con separación de responsabilidades (validation/transactional/reference) y mapeo con MapStruct. **Seguridad multi-capa**: encriptación Base64 en request + encriptación Base64 en base de datos para nombre titular y número de tarjeta.

## 🏗️ Arquitectura - Organización por Capacidades y Dominios

La arquitectura sigue el principio de **Separación de Responsabilidades (SRP)** organizando el código en capacidades especializadas y subcapacidades por dominio:

```
service/payment/
├── PaymentService.java                          (Interface principal)
├── PaymentServiceImpl.java                      (Coordinador - 278 líneas)
│   └── Coordina flujo completo delegando a subcapacidades específicas
│
├── validation/                                  📋 Capacidad: Validación (organizada por dominio)
│   ├── cart/
│   │   ├── PaymentCartValidationService.java
│   │   └── PaymentCartValidationServiceImpl.java
│   │       ├── validateCart()
│   │       └── validateCartByUserEmail()
│   │
│   ├── payment/
│   │   ├── PaymentPaymentValidationService.java
│   │   └── PaymentPaymentValidationServiceImpl.java
│   │       ├── validatePaymentType()
│   │       ├── findPendingStatus()
│   │       └── validateInstallments()
│   │
│   └── common/
│       ├── PaymentCommonValidationService.java
│       └── PaymentCommonValidationServiceImpl.java
│           ├── validateCardData()
│           ├── validateCardNumber()
│           └── validateExpirationDate()
│
├── transactional/                               💾 Capacidad: Transactional (organizada por dominio)
│   ├── payment/
│   │   ├── PaymentPaymentTransactionalService.java
│   │   └── PaymentPaymentTransactionalServiceImpl.java
│   │       ├── savePayment()
│   │       ├── savePaymentDebit()
│   │       └── savePaymentCredit()
│   │
│   ├── cart/
│   │   ├── PaymentCartTransactionalService.java
│   │   └── PaymentCartTransactionalServiceImpl.java
│   │       └── updateCartStatusToProcessing()   → ✅ Actualiza carrito a "Procesando"
│   │
│   ├── cartitem/
│   │   ├── PaymentCartItemTransactionalService.java
│   │   └── PaymentCartItemTransactionalServiceImpl.java
│   │       └── findByCartId()                   → Obtiene items del carrito
│   │
│   └── reference/
│       ├── PaymentReferenceTransactionalService.java
│       └── PaymentReferenceTransactionalServiceImpl.java
│           ├── existsByReferenceNumber()
│           └── savePaymentReference()

└── [Integración con Stock]                      📦 Integración: Gestión de Inventario
    └── StockStockTransactionalService           (inyectado desde módulo stock)
        └── decreaseStock()                      → ✅ Disminuye inventario por producto
│
└── reference/                                   🔑 Capacidad: Generación Referencias
    ├── PaymentReferenceService.java            (Interface)
    └── PaymentReferenceServiceImpl.java        (60 líneas)
        └── generateUniqueReference()           → UUID único con verificación BD

mapper/payment/
└── PaymentMapper.java                          🗺️ Mapper MapStruct (155 líneas)
    ├── toPayment()                             → Payment entity
    ├── toPaymentDebit()                        → PaymentDebit entity (con encriptación)
    ├── toPaymentCredit()                       → PaymentCredit entity (con encriptación)
    ├── toPaymentProcessResponseDto()           → Response DTO
    ├── encryptCardHolderName()                 → Encripta nombre titular (Base64)
    ├── encryptCardNumber()                     → Encripta número tarjeta (Base64)
    ├── parseExpirationDate()                   → Parseo fecha
    └── getLastFourDigits()                     → Últimos 4 dígitos
```

## 🎯 Flujo de Procesamiento de Pago

```
1. POST /api/v1/payments/process
   └─ PaymentController

2. PaymentServiceImpl.processPayment()
   ├─ Obtener usuario autenticado (AuthenticatedUserUtil)
   ├─ Desencripta datos de tarjeta (Base64DecryptionService)
   ├─ Parsea JSON (JsonUtils)
   └─ Delega a subcapacidades específicas:
      │
      ├─ PaymentCartValidationService (cart domain)
      │  ├─ validateCart() o
      │  └─ validateCartByUserEmail()
      │
      ├─ PaymentCommonValidationService (common validations)
      │  ├─ validateCardData()
      │  ├─ validateCardNumber()
      │  └─ validateExpirationDate()
      │
      ├─ PaymentPaymentValidationService (payment domain)
      │  ├─ validatePaymentType()
      │  ├─ validateInstallments()
      │  └─ findPendingStatus()
      │
      ├─ PaymentReferenceService (reference service)
      │  └─ generateUniqueReference()     → UUID único con verificación BD
      │
       ├─ PaymentMapper (MapStruct)
       │  ├─ toPayment()                   → Payment entity
       │  ├─ toPaymentDebit() o            → PaymentDebit/Credit (con encriptación)
       │  │  toPaymentCredit()
       │  └─ toPaymentProcessResponseDto() → PaymentProcessResponseDto
       │
       ├─ PaymentPaymentTransactionalService (payment domain)
       │  ├─ savePayment()                 → Guarda pago principal
       │  ├─ savePaymentDebit()            → Guarda detalles débito
       │  └─ savePaymentCredit()           → Guarda detalles crédito
       │
       ├─ StockStockTransactionalService (stock integration)
       │  └─ decreaseStock()               → ✅ Disminuye inventario por producto
       │
       └─ PaymentCartTransactionalService (cart domain)
          └─ updateCartStatusToProcessing() → ✅ Actualiza carrito a "Procesando"

3. Respuesta exitosa con número de referencia
```

## 📊 Entidades JPA

### Payment (Pago Principal)
```sql
pago (
    pago_id            SERIAL PRIMARY KEY
    carrito_id         INTEGER → carrito
    tipo_pago_id       VARCHAR(20) → tipo_pago
    fecha_pago         TIMESTAMP DEFAULT NOW()
    referencia_id      INTEGER → referencias
    estado_pago_id     INTEGER → estado_pago
)
```

### PaymentDebit
```sql
pago_debito (
    pago_debito_id       SERIAL PRIMARY KEY
    pago_id              INTEGER → pago
    fecha_vencimiento    DATE
    nombre_titular       VARCHAR(200)
    numero_tarjeta       VARCHAR(20)  -- Solo últimos 4 dígitos
)
```

### PaymentCredit
```sql
pago_credito (
    pago_credito_id      SERIAL PRIMARY KEY
    pago_id              INTEGER → pago
    numero_de_cuotas     INTEGER
    nombre_titular       VARCHAR(200)
    numero_tarjeta       VARCHAR(20)  -- Solo últimos 4 dígitos
    fecha_vencimiento    DATE
)
```

### PaymentReference
```sql
referencias (
    referencia_id    SERIAL PRIMARY KEY
    numero           VARCHAR(100) UNIQUE  -- UUID generado
)
```

## 🔐 Seguridad

### Encriptación de Datos en Tránsito (Request)

Los datos de tarjeta deben enviarse encriptados en Base64:

**JSON Original (NO enviar así):**
```json
{
  "cardNumber": "1234567812345678",
  "cardHolderName": "Juan Pérez",
  "expirationDate": "12/25",
  "cvv": "123",
  "installments": 3,
  "paymentType": "credito"
}
```

**Base64 Encriptado (enviar así):**
```
eyJjYXJkTnVtYmVyIjoiMTIzNDU2NzgxMjM0NTY3OCIsImNhcmRIb2xkZXJOYW1lIjoiSnVhbiBQw6lyZXoiLCJleHBpcmF0aW9uRGF0ZSI6IjEyLzI1IiwiY3Z2IjoiMTIzIiwiaW5zdGFsbG1lbnRzIjozLCJwYXltZW50VHlwZSI6ImNyZWRpdG8ifQ==
```

### Encriptación de Datos en Reposo (Base de Datos)

**Los siguientes campos se almacenan ENCRIPTADOS en Base64:**
- ✅ `nombre_titular` - Nombre del titular de la tarjeta
- ✅ `numero_tarjeta` - Número completo de la tarjeta (16 dígitos)

**Proceso de encriptación:**
1. **Entrada**: `"Juan Pérez"` y `"1234567812345678"`
2. **Almacenado en BD**: `"SnVhbiBQw6lyZXo="` y `"MTIzNDU2NzgxMjM0NTY3OA=="`
3. **Respuesta al cliente**: Solo últimos 4 dígitos sin encriptar (`"5678"`)

### Protección Multi-Capa

| Capa | Dato | Protección |
|---|---|---|
| **Request** | Datos completos tarjeta | Base64 encriptado |
| **Base de Datos** | nombre_titular + numero_tarjeta | Base64 encriptado |
| **Response** | Solo últimos 4 dígitos | Sin encriptar |
| **Logs** | Sin datos sensibles | No se logea info de tarjeta |

## 📝 Ejemplos de Uso

### Request - Procesar Pago

**Endpoint**: `POST /api/v1/payments/process`

**Headers**:
```
Content-Type: application/json
Authorization: Bearer {token}
```

**Body**:
```json
{
  "cartId": 1,
  "encryptedCardData": "eyJjYXJkTnVtYmVyIjoiMTIzNDU2NzgxMjM0NTY3OCIsImNhcmRIb2xkZXJOYW1lIjoiSnVhbiBQw6lyZXoiLCJleHBpcmF0aW9uRGF0ZSI6IjEyLzI1IiwiY3Z2IjoiMTIzIiwiaW5zdGFsbG1lbnRzIjozLCJwYXltZW50VHlwZSI6ImNyZWRpdG8ifQ=="
}
```

### Response - Éxito (200 OK)

```json
{
  "failure": false,
  "code": 200,
  "message": "Pago procesado exitosamente",
  "body": {
    "paymentId": 15,
    "referenceNumber": "F47AC10B-58CC-4372-A567-0E02B2C3D479",
    "status": "Pendiente",
    "paymentType": "credito",
    "cardLast4Digits": "5678",
    "installments": 3
  },
  "timestamp": "1731506400000"
}
```

### Response - Error Carrito No Encontrado (400 Bad Request)

```json
{
  "failure": true,
  "code": 400,
  "errorCode": "ED-CAR-01",
  "message": "[ED-CAR-01] Carrito no encontrado",
  "timestamp": "1731506400000"
}
```

### Response - Error Datos Encriptados Inválidos (400 Bad Request)

```json
{
  "failure": true,
  "code": 400,
  "errorCode": "ED-PAY-02",
  "message": "[ED-PAY-02] Los datos encriptados de la tarjeta son inválidos",
  "timestamp": "1731506400000"
}
```

### Response - Error Tipo de Pago Inválido (400 Bad Request)

```json
{
  "failure": true,
  "code": 400,
  "errorCode": "ED-PAY-04",
  "message": "[ED-PAY-04] Tipo de pago inválido. Debe ser 'debito' o 'credito'",
  "timestamp": "1731506400000"
}
```

## 🚨 Códigos de Error

| Código | Mensaje | HTTP Status |
|--------|---------|-------------|
| `ED-PAY-01` | Pago no encontrado | 400 |
| `ED-PAY-02` | Datos encriptados inválidos | 400 |
| `ED-PAY-03` | Formato de datos de tarjeta inválido | 400 |
| `ED-PAY-04` | Tipo de pago inválido (debe ser 'debito' o 'credito') | 400 |
| `ED-PAY-05` | Estado de pago no encontrado | 400 |
| `ED-PAY-06` | Número de cuotas inválido (debe ser > 0) | 400 |
| `ED-PAY-07` | Cuotas requeridas para crédito | 400 |
| `ED-PAY-08` | Carrito vacío (no se puede procesar pago) | 400 |
| `ED-PAY-09` | Error al generar número de referencia | 400 |
| `ED-PAY-10` | Error al procesar pago | 400 |
| `ED-PAY-11` | Fecha de vencimiento inválida (formato MM/YY) | 400 |
| `ED-PAY-12` | Número de tarjeta inválido (debe tener 16 dígitos) | 400 |

## ✅ Validaciones

### Campos Obligatorios
- ✅ `cardNumber` (16 dígitos)
- ✅ `cardHolderName`
- ✅ `paymentType` ("debito" o "credito")

### Campos Opcionales
- ⚠️ `expirationDate` (formato MM/YY, ejemplo: "12/25")
- ⚠️ `cvv` (3 o 4 dígitos)
- ⚠️ `installments` (solo para crédito, default: 1)

### Reglas de Negocio

| Regla | Descripción |
|-------|-------------|
| **Débito** | Solo admite 1 cuota (ignora valor de `installments`) |
| **Crédito** | Admite múltiples cuotas (min: 1, default: 1) |
| **Referencia** | UUID único generado automáticamente con reintentos |
| **Estado Inicial** | Todos los pagos inician en estado "Pendiente" |
| **Carrito** | Debe existir y tener al menos 1 producto |

## 🔧 Tecnologías Utilizadas

- **Spring Boot 3.x** - Framework principal
- **Spring Data JPA** - Persistencia
- **PostgreSQL** - Base de datos
- **Lombok** - Reducción de boilerplate
- **Jackson** - Serialización JSON
- **Base64** - Encriptación de datos sensibles
- **UUID** - Generación de referencias únicas

## 📦 Dependencias

```java
// Validation subcapacidades por dominio
private final PaymentCartValidationService cartValidationService;
private final PaymentPaymentValidationService paymentValidationService;
private final PaymentCommonValidationService commonValidationService;

// Transactional subcapacidades
private final PaymentPaymentTransactionalService paymentTransactionalService;
private final PaymentCartTransactionalService cartTransactionalService;       // ✅ Nueva
private final PaymentCartItemTransactionalService cartItemTransactionalService; // ✅ Nueva
private final StockStockTransactionalService stockTransactionalService;       // ✅ Nueva (integración)

// Reference service (lógica de negocio)
private final PaymentReferenceService paymentReferenceService;

// Mapper (mapeo DTO ↔ Entity con MapStruct)
private final PaymentMapper paymentMapper;

// Utilities
private final Base64DecryptionService base64DecryptionService;
private final JsonUtils jsonUtils;
private final AuthenticatedUserUtil authenticatedUserUtil;
```

**Beneficios de la nueva estructura:**
- ✅ **Sin inyección directa de repositories** en PaymentServiceImpl (cumple regla 06 - Capa Transactional)
- ✅ **Validation organizada por dominios** (cart/, payment/, common/) según regla 09
- ✅ **Mapeo con MapStruct** en lugar de Builder (cumple regla 01 - TODO mapeo en Mapper)
- ✅ **Separación lógica de negocio vs mapeo**: Reference service para UUID, Mapper para transformaciones
- ✅ **Integración con módulo Stock**: Disminución automática de inventario al procesar pago
- ✅ **Gestión de estado del carrito**: Cambio automático a "Procesando" al finalizar pago
- ✅ **Alta cohesión**: Cada subcapacidad agrupa métodos relacionados
- ✅ **Bajo acoplamiento**: Cambios en un dominio no afectan otros
- ✅ **Testeable**: Tests específicos por dominio y mapeo compile-time safe
- ✅ **Escalable**: Agregar dominios sin modificar existentes

## 🎯 Beneficios de la Arquitectura

| Beneficio | Descripción |
|-----------|-------------|
| **Cumplimiento de Reglas** | Sigue reglas 06 (Transactional) y 09 (Organización por dominios) |
| **Alta Cohesión** | Cada subcapacidad agrupa métodos relacionados por dominio |
| **Bajo Acoplamiento** | Cambios en un dominio no afectan otros (cart, payment, common) |
| **Sin Acceso Directo a BD** | PaymentServiceImpl NO inyecta repositories (usa transactional) |
| **Testeable** | Tests específicos por dominio y capacidad |
| **Escalable** | Agregar dominios/capacidades sin modificar existentes |
| **Mantenible** | Lógica organizada por responsabilidad y dominio |
| **Seguro** | Encriptación Base64 en tránsito + encriptación en BD + protección multi-capa |

## 📏 Métricas de Mejora

| Aspecto | Antes | Después | Mejora |
|---|---|---|---|
| **Inyecciones ServiceImpl** | 7 (3 repositories + 4 servicios) | 10 (0 repositories + 10 subcapacidades) | ✅ Sin acceso directo a BD |
| **Validation monolítico** | 218 líneas | 3 servicios (cart, payment, common) | ✅ Organizado por dominio |
| **Builder con repository** | 1 repository inyectado | 0 repositories (usa transactional) | ✅ Delegado a transactional |
| **Builder → Mapper** | Builder Service (198 líneas) | Mapper MapStruct (145 líneas) + Reference Service (60 líneas) | ✅ Separación lógica negocio/mapeo |
| **Mapeo type-safe** | Manual con `.builder()` | MapStruct compile-time | ✅ Seguro y rápido |
| **Gestión inventario** | ❌ No implementado | ✅ Disminuye stock automáticamente | ✅ Integrado con módulo Stock |
| **Estado de carrito** | ❌ No cambia | ✅ Actualiza a "Procesando" automáticamente | ✅ Workflow completo |
| **Responsabilidades** | Mixtas | Separadas por dominio | ✅ SRP aplicado |

## 📖 Referencias

- [servicios-01-creacion-servicios.mdc](../../../../.cursor/rules/servicios-01-creacion-servicios.mdc) - Arquitectura en capas
- [servicios-04-excepciones.mdc](../../../../.cursor/rules/servicios-04-excepciones.mdc) - Manejo de excepciones
- [servicios-05-dtos.mdc](../../../../.cursor/rules/servicios-05-dtos.mdc) - Estructura de DTOs
- [servicios-06-transactional.mdc](../../../../.cursor/rules/servicios-06-transactional.mdc) - Capa Transactional (regla aplicada)
- [servicios-09-organizacion-capacidades.mdc](../../../../.cursor/rules/servicios-09-organizacion-capacidades.mdc) - Organización por capacidades y dominios (regla aplicada)

---

**Autor**: Team Ecommerce Davivienda  
**Versión**: 1.0.0  
**Fecha**: Noviembre 2024

