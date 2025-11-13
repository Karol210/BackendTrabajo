# 💳 Módulo de Pagos - Procesamiento de Tarjetas

## 📋 Descripción

Módulo de procesamiento de pagos con tarjetas débito y crédito. Implementa arquitectura en capas con separación de responsabilidades (validation/builder) y manejo seguro de datos sensibles mediante encriptación Base64.

## 🏗️ Arquitectura - Organización por Capacidades

La arquitectura sigue el principio de **Separación de Responsabilidades (SRP)** organizando el código en capacidades especializadas:

```
service/payment/
├── PaymentService.java                          (Interface principal)
├── PaymentServiceImpl.java                      (Coordinador - 150 líneas)
│   └── Coordina flujo completo de pago
│
├── validation/                                  📋 Capacidad: Validación
│   ├── PaymentValidationService.java           (Interface)
│   └── PaymentValidationServiceImpl.java       (170 líneas)
│       ├── Valida carrito y datos de tarjeta
│       ├── Valida tipos de pago y estados
│       └── Valida formato de datos sensibles
│
└── builder/                                     🔨 Capacidad: Construcción
    ├── PaymentBuilderService.java              (Interface)
    └── PaymentBuilderServiceImpl.java          (180 líneas)
        ├── Genera referencias UUID
        ├── Construye entidades de pago
        └── Construye respuestas DTO
```

## 🎯 Flujo de Procesamiento de Pago

```
1. POST /api/v1/payments/process
   └─ PaymentController

2. PaymentServiceImpl.processPayment()
   ├─ Desencripta datos de tarjeta (Base64DecryptionService)
   ├─ Parsea JSON (JsonUtils)
   └─ Delega a capacidades especializadas:
      │
      ├─ PaymentValidationService
      │  ├─ validateCart()
      │  ├─ validateCardData()
      │  ├─ validatePaymentType()
      │  ├─ validateInstallments()
      │  └─ findPendingStatus()
      │
      └─ PaymentBuilderService
         ├─ generatePaymentReference()  → UUID único
         ├─ buildPayment()              → Payment entity
         ├─ buildPaymentDebit() o       → PaymentDebit/Credit
         │  buildPaymentCredit()
         └─ buildPaymentResponse()      → PaymentProcessResponseDto

3. Respuesta exitosa
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

### Encriptación de Datos

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

### Enmascaramiento de Tarjetas

- **Entrada**: `1234567812345678`
- **Almacenado**: `************5678` (solo últimos 4 dígitos)
- **Respuesta**: `5678` (últimos 4 dígitos)

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
// Service
private final PaymentValidationService validationService;
private final PaymentBuilderService builderService;
private final Base64DecryptionService base64DecryptionService;
private final JsonUtils jsonUtils;

// Repositories
private final PaymentRepository paymentRepository;
private final PaymentDebitRepository paymentDebitRepository;
private final PaymentCreditRepository paymentCreditRepository;
private final CartRepository cartRepository;
private final PaymentTypeRepository paymentTypeRepository;
private final PaymentStatusRepository paymentStatusRepository;
private final PaymentReferenceRepository paymentReferenceRepository;
```

## 🎯 Beneficios de la Arquitectura

| Beneficio | Descripción |
|-----------|-------------|
| **Alta Cohesión** | Cada capacidad agrupa código relacionado |
| **Bajo Acoplamiento** | Cambios en validación no afectan construcción |
| **Testeable** | Tests unitarios específicos por capacidad |
| **Escalable** | Agregar capacidades sin modificar existentes |
| **Mantenible** | Lógica organizada y fácil de encontrar |
| **Seguro** | Encriptación Base64 + enmascaramiento de tarjetas |

## 📖 Referencias

- [servicios-01-creacion-servicios.mdc](../../../../.cursor/rules/servicios-01-creacion-servicios.mdc) - Arquitectura en capas
- [servicios-04-excepciones.mdc](../../../../.cursor/rules/servicios-04-excepciones.mdc) - Manejo de excepciones
- [servicios-05-dtos.mdc](../../../../.cursor/rules/servicios-05-dtos.mdc) - Estructura de DTOs
- [servicios-09-organizacion-capacidades.mdc](../../../../.cursor/rules/servicios-09-organizacion-capacidades.mdc) - Organización por capacidades

---

**Autor**: Team Ecommerce Davivienda  
**Versión**: 1.0.0  
**Fecha**: Noviembre 2024

