-- ============================================
-- Script de Creación de Base de Datos
-- Sistema de E-commerce
-- PostgreSQL - Nomenclatura snake_case
-- ============================================

-- Eliminar tablas si existen (en orden inverso por dependencias)
DROP TABLE IF EXISTS pago_debito CASCADE;
DROP TABLE IF EXISTS pago_credito CASCADE;
DROP TABLE IF EXISTS tipo_pago CASCADE;
DROP TABLE IF EXISTS referencias CASCADE;
DROP TABLE IF EXISTS productos_carrito CASCADE;
DROP TABLE IF EXISTS carrito CASCADE;
DROP TABLE IF EXISTS pago CASCADE;
DROP TABLE IF EXISTS estado_pago CASCADE;
DROP TABLE IF EXISTS stock CASCADE;
DROP TABLE IF EXISTS productos CASCADE;
DROP TABLE IF EXISTS categorias CASCADE;
DROP TABLE IF EXISTS usuario_rol CASCADE;
DROP TABLE IF EXISTS roles CASCADE;
DROP TABLE IF EXISTS credenciales CASCADE;
DROP TABLE IF EXISTS usuarios CASCADE;
DROP TABLE IF EXISTS documentos CASCADE;
DROP TABLE IF EXISTS estado_usuario CASCADE;
DROP TABLE IF EXISTS estado_producto CASCADE;

-- ============================================
-- TABLAS INDEPENDIENTES (Sin claves foráneas)
-- ============================================

-- Tabla: documentos
CREATE TABLE documentos (
    documento_id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    codigo VARCHAR(50) UNIQUE NOT NULL
);

-- Tabla: roles
CREATE TABLE roles (
    rol_id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE
);

-- Tabla: estado_usuario
CREATE TABLE estado_usuario (
    estado_usuario_id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL
);

-- Tabla: estado_producto
CREATE TABLE estado_producto (
    estado_producto_id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL
);

-- Tabla: categorias
CREATE TABLE categorias (
    categoria_id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT
);

-- Tabla: tipo_pago
CREATE TABLE tipo_pago (
    tipo_pago VARCHAR(20) PRIMARY KEY CHECK (tipo_pago IN ('debito', 'credito')),
    nombre VARCHAR(100) NOT NULL
);

-- Tabla: estado_pago
CREATE TABLE estado_pago (
    estado_pago_id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL
);

-- ============================================
-- TABLAS CON DEPENDENCIAS DE PRIMER NIVEL
-- ============================================

-- Tabla: usuarios
CREATE TABLE usuarios (
    usuario_id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    documento_id INTEGER NOT NULL,
    numero_de_doc VARCHAR(50) NOT NULL,
    credenciales_id INTEGER UNIQUE,
    estado_usuario_id INTEGER,
    usuario_rol_id INTEGER,
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (documento_id) REFERENCES documentos(documento_id),
    FOREIGN KEY (estado_usuario_id) REFERENCES estado_usuario(estado_usuario_id),
    UNIQUE(documento_id, numero_de_doc)
);

-- Tabla: credenciales
CREATE TABLE credenciales (
    credenciales_id SERIAL PRIMARY KEY,
    correo VARCHAR(255) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL
);

-- Actualizar la foreign key de usuarios a credenciales
ALTER TABLE usuarios 
    ADD FOREIGN KEY (credenciales_id) REFERENCES credenciales(credenciales_id);

-- Tabla: productos
CREATE TABLE productos (
    producto_id SERIAL PRIMARY KEY,
    nombre VARCHAR(200) NOT NULL,
    estado_producto_id INTEGER,
    categoria_id INTEGER,
    valor_unitario DECIMAL(10, 2) NOT NULL CHECK (valor_unitario >= 0),
    iva DECIMAL(5, 2) DEFAULT 0.00 CHECK (iva >= 0 AND iva <= 100),
    imagen VARCHAR(500),
    descripcion TEXT,
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (estado_producto_id) REFERENCES estado_producto(estado_producto_id),
    FOREIGN KEY (categoria_id) REFERENCES categorias(categoria_id)
);

-- ============================================
-- TABLAS CON DEPENDENCIAS DE SEGUNDO NIVEL
-- ============================================

-- Tabla: usuario_rol (Relación muchos a muchos entre usuarios y roles)
CREATE TABLE usuario_rol (
    usuario_rol_id SERIAL PRIMARY KEY,
    usuario_id INTEGER NOT NULL,
    rol_id INTEGER NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(usuario_id) ON DELETE CASCADE,
    FOREIGN KEY (rol_id) REFERENCES roles(rol_id) ON DELETE CASCADE,
    UNIQUE(usuario_id, rol_id)
);

-- Actualizar la foreign key de usuarios a usuario_rol
ALTER TABLE usuarios 
    ADD FOREIGN KEY (usuario_rol_id) REFERENCES usuario_rol(usuario_rol_id);

-- Tabla: stock
CREATE TABLE stock (
    stock_id SERIAL PRIMARY KEY,
    producto_id INTEGER NOT NULL UNIQUE,
    cantidad INTEGER NOT NULL DEFAULT 0 CHECK (cantidad >= 0),
    FOREIGN KEY (producto_id) REFERENCES productos(producto_id) ON DELETE CASCADE
);

-- Tabla: carrito
CREATE TABLE carrito (
    carrito_id SERIAL PRIMARY KEY,
    usuario_rol_id INTEGER NOT NULL,
    FOREIGN KEY (usuario_rol_id) REFERENCES usuario_rol(usuario_rol_id) ON DELETE CASCADE
);

-- Tabla: pago
CREATE TABLE pago (
    pago_id SERIAL PRIMARY KEY,
    carrito_id INTEGER,
    tipo_pago_id VARCHAR(20),
    fecha_pago TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    referencia_id INTEGER,
    estado_pago_id INTEGER,
    FOREIGN KEY (carrito_id) REFERENCES carrito(carrito_id) ON DELETE SET NULL,
    FOREIGN KEY (tipo_pago_id) REFERENCES tipo_pago(tipo_pago),
    FOREIGN KEY (estado_pago_id) REFERENCES estado_pago(estado_pago_id)
);

-- ============================================
-- TABLAS CON DEPENDENCIAS DE TERCER NIVEL
-- ============================================

-- Tabla: productos_carrito (Relación muchos a muchos entre carrito y productos)
CREATE TABLE productos_carrito (
    productos_carrito_id SERIAL PRIMARY KEY,
    carrito_id INTEGER NOT NULL,
    producto_id INTEGER NOT NULL,
    cantidad INTEGER NOT NULL DEFAULT 1 CHECK (cantidad > 0),
    FOREIGN KEY (carrito_id) REFERENCES carrito(carrito_id) ON DELETE CASCADE,
    FOREIGN KEY (producto_id) REFERENCES productos(producto_id) ON DELETE CASCADE,
    UNIQUE(carrito_id, producto_id)
);

-- Tabla: referencias
CREATE TABLE referencias (
    referencia_id SERIAL PRIMARY KEY,
    numero VARCHAR(100) NOT NULL UNIQUE
);

-- Actualizar la foreign key de pago a referencias
ALTER TABLE pago 
    ADD FOREIGN KEY (referencia_id) REFERENCES referencias(referencia_id);

-- Tabla: pago_debito
CREATE TABLE pago_debito (
    pago_debito_id SERIAL PRIMARY KEY,
    pago_id INTEGER NOT NULL,
    fecha_vencimiento DATE NOT NULL,
    nombre_titular VARCHAR(200) NOT NULL,
    numero_tarjeta VARCHAR(20) NOT NULL,
    FOREIGN KEY (pago_id) REFERENCES pago(pago_id) ON DELETE CASCADE
);

-- Tabla: pago_credito
CREATE TABLE pago_credito (
    pago_credito_id SERIAL PRIMARY KEY,
    pago_id INTEGER NOT NULL,
    numero_de_cuotas INTEGER NOT NULL CHECK (numero_de_cuotas > 0),
    nombre_titular VARCHAR(200) NOT NULL,
    numero_tarjeta VARCHAR(20) NOT NULL,
    fecha_vencimiento DATE NOT NULL,
    FOREIGN KEY (pago_id) REFERENCES pago(pago_id) ON DELETE CASCADE
);

-- ============================================
-- ÍNDICES PARA MEJORAR EL RENDIMIENTO
-- ============================================

CREATE INDEX idx_usuarios_documento ON usuarios(documento_id, numero_de_doc);
CREATE INDEX idx_usuarios_credenciales ON usuarios(credenciales_id);
CREATE INDEX idx_usuarios_estado ON usuarios(estado_usuario_id);
CREATE INDEX idx_credenciales_correo ON credenciales(correo);
CREATE INDEX idx_productos_categoria ON productos(categoria_id);
CREATE INDEX idx_productos_estado ON productos(estado_producto_id);
CREATE INDEX idx_stock_producto ON stock(producto_id);
CREATE INDEX idx_carrito_usuario ON carrito(usuario_rol_id);
CREATE INDEX idx_productos_carrito_carrito ON productos_carrito(carrito_id);
CREATE INDEX idx_productos_carrito_producto ON productos_carrito(producto_id);
CREATE INDEX idx_pago_carrito ON pago(carrito_id);
CREATE INDEX idx_pago_referencia ON pago(referencia_id);
CREATE INDEX idx_pago_estado ON pago(estado_pago_id);
CREATE INDEX idx_usuario_rol_usuario ON usuario_rol(usuario_id);
CREATE INDEX idx_usuario_rol_rol ON usuario_rol(rol_id);

-- ============================================
-- DATOS INICIALES (SEED DATA)
-- ============================================

-- Insertar tipos de documento
INSERT INTO documentos (nombre, codigo) VALUES
    ('Cédula de Ciudadanía', 'CC'),
    ('Tarjeta de Identidad', 'TI'),
    ('Cédula de Extranjería', 'CE'),
    ('Pasaporte', 'PA'),
    ('NIT', 'NIT');

-- Insertar roles
INSERT INTO roles (nombre) VALUES
    ('Administrador'),
    ('Cliente'),
    ('Vendedor'),
    ('Invitado');

-- Insertar estados de usuario
INSERT INTO estado_usuario (nombre) VALUES
    ('Activo'),
    ('Inactivo'),
    ('Suspendido'),
    ('Bloqueado'),
    ('Pendiente Verificación'),
    ('En Proceso de Baja'),
    ('Eliminado');

-- Insertar estados de producto
INSERT INTO estado_producto (nombre) VALUES
    ('Activo'),
    ('Inactivo'),
    ('Descontinuado'),
    ('Agotado'),
    ('En Reorden'),
    ('Preventa'),
    ('Próximamente'),
    ('En Oferta'),
    ('Suspendido');

-- Insertar tipos de pago
INSERT INTO tipo_pago (tipo_pago, nombre) VALUES
    ('debito', 'Tarjeta Débito'),
    ('credito', 'Tarjeta Crédito');

-- Insertar estados de pago
INSERT INTO estado_pago (nombre) VALUES
    ('Pendiente'),
    ('Procesando'),
    ('Aprobado'),
    ('Rechazado'),
    ('Cancelado'),
    ('Reembolsado'),
    ('Fallido');

-- Insertar categorías de ejemplo
INSERT INTO categorias (nombre, descripcion) VALUES
    ('Electrónica', 'Productos electrónicos y tecnología'),
    ('Ropa', 'Prendas de vestir y accesorios'),
    ('Hogar', 'Artículos para el hogar'),
    ('Deportes', 'Artículos deportivos y fitness'),
    ('Libros', 'Libros físicos y digitales');


-- ============================================
-- COMENTARIOS EN TABLAS
-- ============================================

COMMENT ON TABLE usuarios IS 'Almacena información de los usuarios del sistema';
COMMENT ON TABLE productos IS 'Catálogo de productos disponibles';
COMMENT ON TABLE carrito IS 'Carritos de compra de los usuarios';
COMMENT ON TABLE pago IS 'Registro de pagos realizados';
COMMENT ON TABLE stock IS 'Inventario de productos';

-- ============================================
-- FIN DEL SCRIPT
-- ============================================

-- Para verificar las tablas creadas:
-- SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' ORDER BY table_name;

ALTER TABLE pago_debito 
    ALTER COLUMN numero_tarjeta TYPE VARCHAR(500);

ALTER TABLE pago_credito 
    ALTER COLUMN numero_tarjeta TYPE VARCHAR(500);

CREATE TABLE estado_carrito (
    estado_carrito_id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE
);

-- ============================================
-- PASO 2: Insertar estados iniciales
-- ============================================

INSERT INTO estado_carrito (nombre) VALUES
    ('Activo'),         -- ID = 1
    ('Procesando'),     -- ID = 2
    ('Completado'),     -- ID = 3
    ('Abandonado'),     -- ID = 4
    ('Expirado'),       -- ID = 5
    ('Cancelado');      -- ID = 6

-- ============================================
-- PASO 3: AHORA SÍ agregar la columna a carrito
-- ============================================

-- Agregar la columna
ALTER TABLE carrito 
    ADD COLUMN estado_carrito_id INTEGER;

-- Actualizar carritos existentes al estado "Activo" (ID = 1)
UPDATE carrito 
SET estado_carrito_id = 1 
WHERE estado_carrito_id IS NULL;

-- Establecer valor por defecto
ALTER TABLE carrito 
    ALTER COLUMN estado_carrito_id SET DEFAULT 1;

-- Hacer la columna NOT NULL
ALTER TABLE carrito 
    ALTER COLUMN estado_carrito_id SET NOT NULL;

-- ============================================
-- PASO 4: Agregar foreign key constraint
-- ============================================

ALTER TABLE carrito 
    ADD CONSTRAINT fk_estado_carrito 
    FOREIGN KEY (estado_carrito_id) 
    REFERENCES estado_carrito(estado_carrito_id) 
    ON DELETE RESTRICT;

-- ============================================
-- PASO 5: Crear índice
-- ============================================

CREATE INDEX idx_carrito_estado 
    ON carrito(estado_carrito_id);

    
-- Agregar columna numero_referencia para almacenar UUID de la referencia de pago
ALTER TABLE productos_carrito 
    ADD COLUMN numero_referencia VARCHAR(100);

-- Crear índice para búsquedas por número de referencia
CREATE INDEX idx_productos_carrito_referencia 
    ON productos_carrito(numero_referencia);

-- Comentario en la columna
COMMENT ON COLUMN productos_carrito.numero_referencia IS 'UUID de la referencia de pago asociada al item del carrito';
