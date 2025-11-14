# 📬 Postman Collections - Ecommerce Davivienda

Colecciones completas de Postman para probar todos los módulos del sistema de ecommerce.

## 🔐 Autenticación Global con JWT

**CRÍTICO**: Todos los endpoints del carrito, stock y pagos requieren autenticación JWT.

**Flujo de autenticación:**
1. **Login** → Obtener token JWT (username del JWT = email del usuario)
2. **AuthUserService** extrae automáticamente el `userRoleId` del token JWT
3. **Validación automática**: Verifica rol de Cliente
4. **Operaciones**: Usa el `userRoleId` automáticamente sin parámetros adicionales

**Beneficios:**
- ✅ **Más seguro**: No se puede manipular el usuario
- ✅ **Más simple**: Sin parámetros de documento en requests
- ✅ **Automático**: Sistema busca carrito del usuario autenticado

## 📦 Colecciones Disponibles

| Colección | Archivo | Autenticación | Descripción |
|-----------|---------|:-------------:|-------------|
| 🛒 **CartItems** | `CRUD_CartItems.postman_collection.json` | ✅ JWT | Operaciones CRUD de items del carrito |
| 📦 **Categorías** | `CRUD_Categorias.postman_collection.json` | ❌ | Gestión de categorías de productos |
| 📦 **Productos** | `CRUD_Productos.postman_collection.json` | ❌ | Operaciones CRUD de productos |
| 👥 **Roles** | `CRUD_Roles.postman_collection.json` | ❌ | Gestión de roles de usuario |
| 📄 **Tipos Documento** | `CRUD_Tipos_Documento.postman_collection.json` | ❌ | Tipos de documento de identidad |
| 👤 **Usuarios** | `CRUD_Usuarios.postman_collection.json` | ❌ | Operaciones CRUD de usuarios |
| 💳 **Payment Processing** | `Payment-Processing-API.postman_collection.json` | ✅ JWT | Procesamiento de pagos |
| 📊 **Stock** | `Stock_Validation_API.postman_collection.json` | ✅ JWT | Validación de inventario |

---

# 🛒 Cart Items API

Endpoints para gestión de items del carrito de compras.

## 🔐 Autenticación con JWT

**CRÍTICO**: Todos los endpoints requieren token JWT en header `Authorization: Bearer {token}`

**Flujo automático:**
1. Usuario hace login → Obtiene token JWT
2. Sistema extrae `userRoleId` automáticamente del token
3. Valida que el usuario tiene rol de "Cliente"
4. Busca el carrito del usuario autenticado automáticamente

**Beneficios:**
- ✅ No se requieren parámetros de documento (`documentType`, `documentNumber`)
- ✅ Mayor seguridad: No se puede manipular el usuario
- ✅ Más simple: Solo token JWT necesario

## 🔍 Endpoints Principales

### 1️⃣ **POST** `/api/v1/cart-items/add`

Agrega un producto al carrito del usuario autenticado.

**Request:**
```json
{
  "productId": 1,
  "quantity": 2
}
```

**Response Éxito (201 Created):**
```json
{
  "failure": false,
  "code": 201,
  "message": "Producto agregado al carrito exitosamente",
  "timestamp": "1699876543210"
}
```

### 2️⃣ **DELETE** `/api/v1/cart-items/{id}`

Elimina un item específico del carrito. Valida que el item pertenezca al usuario autenticado.

**Response Éxito (200 OK):**
```json
{
  "failure": false,
  "code": 200,
  "message": "Producto eliminado del carrito exitosamente",
  "timestamp": "1699876543210"
}
```

### 3️⃣ **GET** `/api/v1/cart-items/summary`

Obtiene resumen completo del carrito del usuario autenticado con totales agregados (subtotal, IVA, precio total).

**Response Éxito (200 OK):**
```json
{
  "failure": false,
  "code": 200,
  "message": "Items del carrito obtenidos exitosamente",
  "body": {
    "cartId": 1,
    "items": [
      {
        "id": 1,
        "productName": "Laptop Dell XPS 15",
        "calculation": {
          "unitValue": 2500000.00,
          "quantity": 2,
          "subtotal": 5000000.00,
          "ivaAmount": 950000.00,
          "totalPrice": 5950000.00
        }
      }
    ],
    "totalItems": 2,
    "totalSubtotal": 5000000.00,
    "totalIva": 950000.00,
    "totalPrice": 5950000.00
  },
  "timestamp": "1699876543210"
}
```

## 🚨 Códigos de Error

| Código | Mensaje | Descripción |
|--------|---------|-------------|
| `ED-AUT-01` | Token de autenticación requerido | Header Authorization no proporcionado |
| `ED-CAR-01` | Item del carrito no encontrado | Item inexistente o no pertenece al usuario |
| `ED-CAR-06` | Item no pertenece al usuario | Intento de acceso no autorizado |
| `ED-PRO-01` | Producto no encontrado | productId inválido |
| `ED-VAL-01` | Cantidad debe ser mayor a 0 | Validación de campo |

---

# 📊 Stock API

Endpoints para validación de disponibilidad de inventario.

## 🔍 Endpoint Principal

**GET** `/api/v1/stock/validate`

Valida que todos los productos del carrito del usuario autenticado tengan stock suficiente.

**🔑 Autenticación:** Requiere token JWT en header `Authorization: Bearer {token}`

**Arquitectura:**
- **Servicio:** StockService (unificado: CRUD + Validación)
- **Controller:** StockValidationController
- **Capacidades:** validation, transactional (cart, stock), mapper, auth
- **Flujo:** Extrae userRoleId del token JWT → Obtiene carrito → Valida stock

## 📋 Request

**Headers:**
```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

**No requiere body** - El usuario se identifica por el token JWT

## ✅ Response Éxito - Stock Suficiente (200 OK)

```json
{
  "failure": false,
  "code": 200,
  "message": "Todos los productos tienen stock suficiente",
  "body": {
    "available": true,
    "message": "Todos los productos tienen stock suficiente",
    "insufficientStockProducts": [],
    "totalProductsInCart": 3,
    "productsWithIssues": 0
  },
  "timestamp": "1699876543210"
}
```

## ⚠️ Response Stock Insuficiente (200 OK)

**Nota:** Retorna 200 OK con `available: false` para que el frontend maneje la lógica de mostrar productos faltantes.

```json
{
  "failure": false,
  "code": 200,
  "message": "Stock insuficiente para 2 producto(s)",
  "body": {
    "available": false,
    "message": "Stock insuficiente para 2 producto(s)",
    "insufficientStockProducts": [
      {
        "productId": 5,
        "productName": "Laptop Dell XPS 15",
        "requestedQuantity": 3,
        "availableQuantity": 1,
        "missingQuantity": 2
      },
      {
        "productId": 8,
        "productName": "Mouse Logitech MX Master",
        "requestedQuantity": 10,
        "availableQuantity": 0,
        "missingQuantity": 10
      }
    ],
    "totalProductsInCart": 3,
    "productsWithIssues": 2
  },
  "timestamp": "1699876543210"
}
```

## 🚨 Códigos de Error

| Código | Mensaje | Descripción |
|--------|---------|-------------|
| `ED-STO-01` | Stock insuficiente | Productos sin inventario suficiente |
| `ED-STO-03` | Carrito vacío | El carrito no tiene productos |
| `ED-STO-04` | Carrito no encontrado | Usuario sin carrito |
| `ED-CAR-08` | Usuario no encontrado | Documento no existe |
| `ED-VAL-01` | Errores de validación | Campos obligatorios faltantes |

## 📊 Casos de Prueba Incluidos

1. ✅ **Stock Suficiente**: Todos los productos disponibles
2. ❌ **Stock Insuficiente**: Productos sin inventario
3. ❌ **Usuario sin Carrito**: Carrito no existe
4. ❌ **Usuario No Encontrado**: Documento inválido
5. ❌ **Carrito Vacío**: Sin productos para validar
6. ❌ **Validación de Campos**: Campos obligatorios

---

# 💳 Payment Processing API

Procesamiento de pagos con tarjetas de crédito y débito.

## 🔐 Autenticación con JWT

**CRÍTICO**: Todos los endpoints requieren token JWT en header `Authorization: Bearer {token}`

**Flujo de Pago Seguro:**
1. Usuario autenticado → Token JWT extrae `userRoleId`
2. Sistema busca carrito activo automáticamente (cartId opcional)
3. Valida ownership del carrito
4. Procesa pago con datos encriptados

## 🔐 Mejora de Seguridad: CartId Automático

**✅ FUNCIONALIDAD**: El `cartId` es **OPCIONAL**. El sistema busca automáticamente el carrito activo del usuario autenticado por su email del JWT.

**Beneficios:**
- ✅ **Más seguro**: No se puede manipular el cartId para pagar el carrito de otro usuario
- ✅ **Más simple**: Menos parámetros en el request
- ✅ **Validación automática**: Sistema valida que el carrito pertenece al usuario

**Formas de uso:**
- ⭐ **RECOMENDADO**: Sin cartId → Sistema busca carrito automáticamente
- ⚠️ **Alternativo**: Con cartId → Solo si necesitas especificar un carrito específico

## 🔍 Endpoint Principal

### **POST** `/api/v1/payments/process`

Procesa un pago con tarjeta (débito o crédito). Los datos de la tarjeta deben enviarse encriptados en Base64.

**Request (Sin cartId - RECOMENDADO):**
```json
{
  "encryptedCardData": "eyJjYXJkTnVtYmVyIjoiMTIzNDU2NzgxMjM0NTY3OCIsImNhcmRIb2xkZXJOYW1lIjoiSnVhbiBQw6lyZXoiLCJleHBpcmF0aW9uRGF0ZSI6IjEyLzI1IiwiY3Z2IjoiMTIzIiwiaW5zdGFsbG1lbnRzIjozLCJwYXltZW50VHlwZSI6ImNyZWRpdG8ifQ=="
}
```

**Request (Con cartId - Alternativo):**
```json
{
  "cartId": 1,
  "encryptedCardData": "eyJjYXJkTnVtYmVyIjoiMTIzNDU2NzgxMjM0NTY3OCIsImNhcmRIb2xkZXJOYW1lIjoiSnVhbiBQw6lyZXoiLCJleHBpcmF0aW9uRGF0ZSI6IjEyLzI1IiwiY3Z2IjoiMTIzIiwiaW5zdGFsbG1lbnRzIjozLCJwYXltZW50VHlwZSI6ImNyZWRpdG8ifQ=="
}
```

**Datos Encriptados (Base64):**
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

**Response Éxito (200 OK):**
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
    "cardLast4Digits": "5678"
  },
  "timestamp": "1731506400000"
}
```

## 📋 Contenido Payment

### 1️⃣ Archivos

| Archivo | Descripción |
|---------|-------------|
| `Payment-Processing-API.postman_collection.json` | Colección principal con todos los endpoints |
| `Payment-Processing-Environment.postman_environment.json` | Variables de entorno para desarrollo |
| `README.md` | Esta guía de uso |

### 2️⃣ Endpoints Incluidos

#### **Payments** (Procesamiento exitoso)
- ⭐ **Process Payment - Auto Cart (Credit) RECOMMENDED**: Pago crédito sin cartId (automático)
- ⭐ **Process Payment - Auto Cart (Debit) RECOMMENDED**: Pago débito sin cartId (automático)
- ✅ **Process Payment - Credit Card (With cartId)**: Pago crédito con cartId explícito
- ✅ **Process Payment - Debit Card (With cartId)**: Pago débito con cartId explícito
- ✅ **Process Payment - Minimal Data**: Pago con solo campos obligatorios

#### **Error Cases** (Casos de error)
- ❌ **Error - Cart Not Found**: Carrito inexistente (ED-CAR-01)
- ❌ **Error - Invalid Encrypted Data**: Base64 inválido (ED-PAY-02)
- ❌ **Error - Invalid Payment Type**: Tipo de pago inválido (ED-PAY-04)
- ❌ **Error - Invalid Card Number**: Número de tarjeta inválido (ED-PAY-12)

#### **Utilities** (Utilidades)
- 🔧 **Base64 Encoder (Helper)**: Generador de datos Base64 para pruebas

## 🚀 Instalación

### Opción 1: Importar desde archivos

1. Abre Postman
2. Click en **Import** (esquina superior izquierda)
3. Arrastra los archivos JSON o selecciónalos:
   - `Payment-Processing-API.postman_collection.json`
   - `Payment-Processing-Environment.postman_environment.json`
4. Click en **Import**

### Opción 2: Importar desde URL (si está en GitHub)

1. Abre Postman
2. Click en **Import** > **Link**
3. Pega la URL del archivo raw en GitHub
4. Click en **Continue** > **Import**

## ⚙️ Configuración

### 1️⃣ Variables de Entorno

Después de importar, configura el entorno **Payment Processing - Development**:

| Variable | Valor por Defecto | Descripción |
|----------|-------------------|-------------|
| `base_url` | `http://localhost:8080` | URL base del API |
| `token` | *(vacío)* | Token JWT de autenticación |
| `cartId` | `1` | **OPCIONAL** - ID del carrito (ya no es requerido) |
| `paymentId` | *(vacío)* | Se llena automáticamente |
| `referenceNumber` | *(vacío)* | Se llena automáticamente |

**📌 IMPORTANTE**: 
- Solo necesitas actualizar `token` antes de ejecutar los requests marcados con ⭐ RECOMMENDED
- El `cartId` ya NO es obligatorio - el sistema lo busca automáticamente por el usuario autenticado

### 2️⃣ Obtener Token JWT (Login)

**Endpoint de autenticación:**

```bash
POST {{base_url}}/api/v1/auth/login
Content-Type: application/json

{
  "email": "usuario@example.com",
  "password": "password123"
}
```

**Response Éxito:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "usuario@example.com",
  "message": "Hola usuario@example.com, has iniciado sesión",
  "expiresIn": 3600000
}
```

**Uso del Token:**
1. Copia el valor del campo `token`
2. Agrégalo a la variable de entorno `jwt_token` o `token` (según la colección)
3. El sistema extraerá automáticamente:
   - Email del usuario (username del JWT)
   - UserRoleId correspondiente
   - Validación de rol Cliente

**⚠️ IMPORTANTE**: El `username` del JWT es el **email** del usuario. El sistema usa este email para obtener automáticamente el `userRoleId` mediante el servicio `AuthUserService`.

### 3️⃣ Agregar Items al Carrito

El sistema crea el carrito automáticamente al agregar el primer producto. El usuario se identifica por el token JWT.

```bash
POST {{base_url}}/api/v1/cart-items/add
Content-Type: application/json
Authorization: Bearer {{jwt_token}}

{
  "productId": 1,
  "quantity": 2
}
```

**✅ Características:**
- Crea carrito automáticamente si no existe
- Identifica usuario por token JWT (no requiere documentType/documentNumber)
- Si el producto ya existe, actualiza la cantidad

## 🔐 Encriptación Base64

### ¿Cómo generar datos encriptados?

#### Opción 1: Usar el Helper en Postman

1. Ve a **Utilities** > **Base64 Encoder (Helper)**
2. Abre el **Pre-request Script**
3. Modifica el objeto `cardData`:
   ```javascript
   const cardData = {
       "cardNumber": "1234567812345678",
       "cardHolderName": "Tu Nombre",
       "expirationDate": "12/25",
       "cvv": "123",
       "installments": 3,
       "paymentType": "credito"
   };
   ```
4. Click en **Send** (o solo ejecuta el script)
5. Copia el Base64 de la **Console**
6. Úsalo en el campo `encryptedCardData`

#### Opción 2: Usar JavaScript en Navegador

```javascript
const cardData = {
    "cardNumber": "1234567812345678",
    "cardHolderName": "Juan Pérez",
    "expirationDate": "12/25",
    "cvv": "123",
    "installments": 3,
    "paymentType": "credito"
};

const base64 = btoa(JSON.stringify(cardData));
console.log(base64);
```

#### Opción 3: Usar comando Linux/Mac

```bash
echo -n '{"cardNumber":"1234567812345678","cardHolderName":"Juan Pérez","expirationDate":"12/25","cvv":"123","installments":3,"paymentType":"credito"}' | base64
```

## 🧪 Tests Automáticos

Cada request incluye tests automáticos que se ejecutan después de la respuesta:

### Tests Globales (todas las requests)
- ✅ Verificar que la respuesta es JSON
- ✅ Verificar que el tiempo de respuesta < 3000ms

### Tests Específicos - Process Payment Credit Card
- ✅ Status code is 200
- ✅ Response has correct structure
- ✅ Payment processed successfully
- ✅ Payment body has required fields
- ✅ Payment status is "Pendiente"
- ✅ Payment type is "credito"
- ✅ Guardar paymentId y referenceNumber

### Tests Específicos - Error Cases
- ✅ Status code is 400
- ✅ Error code matches expected (ED-PAY-XX)
- ✅ Failure is true

## 📊 Ejemplos de Uso

### Ejemplo 1: Pago con Tarjeta de Crédito

**Request**:
```json
{
  "cartId": 1,
  "encryptedCardData": "eyJjYXJkTnVtYmVyIjoiMTIzNDU2NzgxMjM0NTY3OCIsImNhcmRIb2xkZXJOYW1lIjoiSnVhbiBQw6lyZXoiLCJleHBpcmF0aW9uRGF0ZSI6IjEyLzI1IiwiY3Z2IjoiMTIzIiwiaW5zdGFsbG1lbnRzIjozLCJwYXltZW50VHlwZSI6ImNyZWRpdG8ifQ=="
}
```

**Datos Desencriptados**:
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

**Response Exitoso (200)**:
```json
{
  "failure": false,
  "code": 200,
  "message": "Pago procesado exitosamente",
  "body": {
    "paymentId": 15,
    "referenceNumber": "F47AC10B-58CC-4372-A567-0E02B2C3D479",
    "status": "Pendiente",
    "paymentType": "credito"
  },
  "timestamp": "1731506400000"
}
```

### Ejemplo 2: Pago con Tarjeta de Débito

**Datos Desencriptados**:
```json
{
  "cardNumber": "9876543210123456",
  "cardHolderName": "María García",
  "expirationDate": "06/26",
  "cvv": "456",
  "paymentType": "debito"
}
```

**Response Exitoso (200)**:
```json
{
  "failure": false,
  "code": 200,
  "message": "Pago procesado exitosamente",
  "body": {
    "paymentId": 16,
    "referenceNumber": "A8B3D12E-91FF-4C82-B456-1A03C4D5E689",
    "status": "Pendiente",
    "paymentType": "debito"
  },
  "timestamp": "1731506500000"
}
```

## 🚨 Códigos de Error

### Errores de Autenticación (Comunes a todas las APIs)

| Código | Mensaje | HTTP | Descripción |
|--------|---------|------|-------------|
| `ED-AUT-01` | Token de autenticación no proporcionado o inválido | 401 | Header Authorization faltante o token JWT inválido |
| `ED-CAR-08` | Usuario no encontrado | 404 | Email del JWT no existe en BD |
| `ED-CAR-09` | Usuario sin roles asignados | 400 | Usuario sin UserRole |
| `ED-CAR-10` | UserRole no encontrado | 404 | UserRoleId inválido |
| `ED-CAR-11` | Usuario no tiene rol de Cliente | 403 | Rol diferente a "Cliente" |

### Errores de Pagos

| Código | Mensaje | HTTP |
|--------|---------|------|
| `ED-CAR-01` | Carrito no encontrado | 400 |
| `ED-PAY-02` | Datos encriptados inválidos | 400 |
| `ED-PAY-03` | Formato de datos de tarjeta inválido | 400 |
| `ED-PAY-04` | Tipo de pago inválido | 400 |
| `ED-PAY-05` | Estado de pago no encontrado | 400 |
| `ED-PAY-06` | Número de cuotas inválido | 400 |
| `ED-PAY-07` | Cuotas requeridas para crédito | 400 |
| `ED-PAY-08` | Carrito vacío | 400 |
| `ED-PAY-09` | Error al generar referencia | 400 |
| `ED-PAY-10` | Error al procesar pago | 400 |
| `ED-PAY-11` | Fecha de vencimiento inválida | 400 |
| `ED-PAY-12` | Número de tarjeta inválido | 400 |

## 📝 Flujo de Prueba Recomendado

1. ✅ **Autenticación**: Obtener token JWT
2. ✅ **Crear carrito**: Agregar productos al carrito
3. ✅ **Pago exitoso - Crédito**: Procesar pago con tarjeta de crédito
4. ✅ **Pago exitoso - Débito**: Procesar pago con tarjeta de débito
5. ✅ **Pago mínimo**: Procesar pago con solo campos obligatorios
6. ❌ **Error - Cart Not Found**: Validar error de carrito inexistente
7. ❌ **Error - Invalid Encrypted Data**: Validar error de Base64 inválido
8. ❌ **Error - Invalid Payment Type**: Validar error de tipo de pago inválido
9. ❌ **Error - Invalid Card Number**: Validar error de tarjeta inválida

## 🔧 Troubleshooting

### Problema 1: "Token inválido o expirado"
**Solución**: Obtener un nuevo token JWT y actualizar la variable de entorno.

### Problema 2: "Carrito no encontrado"
**Solución**: Crear un carrito agregando productos primero.

### Problema 3: "Error al desencriptar Base64"
**Solución**: Usar el Helper de Base64 en Postman para generar datos correctos.

### Problema 4: "Tests fallan"
**Solución**: Verificar que las variables de entorno estén configuradas correctamente.

## 📚 Referencias

- [Documentación del Módulo](../src/main/java/com/ecommerce/davivienda/service/payment/README.md)
- [Script SQL de Base de Datos](../src/main/resources/db/init-ecommerce.sql)

---

**Autor**: Team Ecommerce Davivienda  
**Versión**: 2.0.0  
**Fecha**: Noviembre 2024  
**Última Actualización**: Integración AuthUserService con JWT

## 📝 Changelog v2.0.0

### 🔐 Autenticación con JWT (BREAKING CHANGES)

**Cambios principales:**
1. ✅ **AuthUserService**: Servicio genérico que extrae `userRoleId` automáticamente del token JWT
2. ✅ **Sin parámetros de documento**: Ya NO se requiere `documentType` ni `documentNumber` en requests
3. ✅ **CartId opcional en pagos**: Sistema busca carrito automáticamente por usuario autenticado
4. ✅ **Validación automática**: Valida rol de Cliente en cada operación

**Endpoints actualizados:**
- `POST /api/v1/cart-items/add` → Solo requiere `productId` y `quantity` + JWT
- `GET /api/v1/cart-items/summary` → Solo requiere JWT
- `DELETE /api/v1/cart-items/{id}` → Solo requiere JWT
- `GET /api/v1/stock/validate` → Solo requiere JWT
- `POST /api/v1/payments/process` → `cartId` es opcional, solo requiere JWT

**Beneficios:**
- 🔒 **Mayor seguridad**: Imposible manipular identidad del usuario
- 🚀 **Más simple**: Menos parámetros en requests
- ✅ **Automático**: Sistema maneja todo desde el token JWT

