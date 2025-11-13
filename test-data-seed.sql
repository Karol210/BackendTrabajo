-- ============================================
-- DATOS DE PRUEBA - E-commerce Davivienda
-- Sistema de E-commerce
-- PostgreSQL - Generación de datos realistas
-- ============================================

-- ============================================
-- 1. CREDENCIALES (20 usuarios)
-- ============================================
-- Contraseña hasheada con BCrypt: "password123"
-- Hash: $2a$10$N9qo8uLOickgx2ZMRZoMye1J7qjpjdPq8kFKA/LCqX8ZqQJU.0Z0K

INSERT INTO credenciales (correo, contrasena) VALUES
    ('admin@davivienda.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye1J7qjpjdPq8kFKA/LCqX8ZqQJU.0Z0K'),
    ('maria.garcia@gmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye1J7qjpjdPq8kFKA/LCqX8ZqQJU.0Z0K'),
    ('juan.rodriguez@hotmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye1J7qjpjdPq8kFKA/LCqX8ZqQJU.0Z0K'),
    ('ana.martinez@yahoo.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye1J7qjpjdPq8kFKA/LCqX8ZqQJU.0Z0K'),
    ('carlos.lopez@gmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye1J7qjpjdPq8kFKA/LCqX8ZqQJU.0Z0K'),
    ('laura.fernandez@hotmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye1J7qjpjdPq8kFKA/LCqX8ZqQJU.0Z0K'),
    ('pedro.sanchez@gmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye1J7qjpjdPq8kFKA/LCqX8ZqQJU.0Z0K'),
    ('sofia.ramirez@yahoo.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye1J7qjpjdPq8kFKA/LCqX8ZqQJU.0Z0K'),
    ('diego.torres@gmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye1J7qjpjdPq8kFKA/LCqX8ZqQJU.0Z0K'),
    ('valentina.castro@hotmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye1J7qjpjdPq8kFKA/LCqX8ZqQJU.0Z0K'),
    ('andres.morales@gmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye1J7qjpjdPq8kFKA/LCqX8ZqQJU.0Z0K'),
    ('camila.herrera@yahoo.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye1J7qjpjdPq8kFKA/LCqX8ZqQJU.0Z0K'),
    ('sebastian.vargas@gmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye1J7qjpjdPq8kFKA/LCqX8ZqQJU.0Z0K'),
    ('isabella.cruz@hotmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye1J7qjpjdPq8kFKA/LCqX8ZqQJU.0Z0K'),
    ('miguel.ortiz@gmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye1J7qjpjdPq8kFKA/LCqX8ZqQJU.0Z0K'),
    ('mariana.mendez@yahoo.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye1J7qjpjdPq8kFKA/LCqX8ZqQJU.0Z0K'),
    ('daniel.ruiz@gmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye1J7qjpjdPq8kFKA/LCqX8ZqQJU.0Z0K'),
    ('gabriela.jimenez@hotmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye1J7qjpjdPq8kFKA/LCqX8ZqQJU.0Z0K'),
    ('ricardo.flores@gmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye1J7qjpjdPq8kFKA/LCqX8ZqQJU.0Z0K'),
    ('natalia.romero@yahoo.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye1J7qjpjdPq8kFKA/LCqX8ZqQJU.0Z0K');

-- ============================================
-- 2. USUARIOS (20 usuarios)
-- ============================================

INSERT INTO usuarios (nombre, apellido, documento_id, numero_de_doc, credenciales_id, estado_usuario_id, creation_date) VALUES
    ('Admin', 'Sistema', 1, '1000000001', 1, 1, '2024-01-15 10:00:00'),
    ('María', 'García López', 1, '1000000002', 2, 1, '2024-01-20 14:30:00'),
    ('Juan', 'Rodríguez Silva', 1, '1000000003', 3, 1, '2024-02-01 09:15:00'),
    ('Ana', 'Martínez Ruiz', 1, '1000000004', 4, 1, '2024-02-10 16:45:00'),
    ('Carlos', 'López Fernández', 1, '1000000005', 5, 1, '2024-02-15 11:20:00'),
    ('Laura', 'Fernández Gómez', 1, '1000000006', 6, 1, '2024-03-01 13:00:00'),
    ('Pedro', 'Sánchez Díaz', 1, '1000000007', 7, 1, '2024-03-05 10:30:00'),
    ('Sofía', 'Ramírez Torres', 1, '1000000008', 8, 1, '2024-03-10 15:00:00'),
    ('Diego', 'Torres Castro', 1, '1000000009', 9, 1, '2024-03-15 12:00:00'),
    ('Valentina', 'Castro Morales', 1, '1000000010', 10, 1, '2024-04-01 09:30:00'),
    ('Andrés', 'Morales Herrera', 1, '1000000011', 11, 1, '2024-04-05 14:15:00'),
    ('Camila', 'Herrera Vargas', 1, '1000000012', 12, 1, '2024-04-10 11:45:00'),
    ('Sebastián', 'Vargas Cruz', 1, '1000000013', 13, 1, '2024-04-15 16:00:00'),
    ('Isabella', 'Cruz Ortiz', 1, '1000000014', 14, 1, '2024-05-01 10:00:00'),
    ('Miguel', 'Ortiz Méndez', 1, '1000000015', 15, 1, '2024-05-05 13:30:00'),
    ('Mariana', 'Méndez Ruiz', 1, '1000000016', 16, 1, '2024-05-10 09:00:00'),
    ('Daniel', 'Ruiz Jiménez', 1, '1000000017', 17, 2, '2024-05-15 15:30:00'),
    ('Gabriela', 'Jiménez Flores', 1, '1000000018', 18, 1, '2024-06-01 12:15:00'),
    ('Ricardo', 'Flores Romero', 2, '2000000001', 19, 5, '2024-06-05 14:00:00'),
    ('Natalia', 'Romero Vega', 3, 'CE123456', 20, 1, '2024-06-10 10:45:00');

-- ============================================
-- 3. USUARIO_ROL (Asignar roles a usuarios)
-- ============================================

INSERT INTO usuario_rol (usuario_id, rol_id) VALUES
    -- Admin (rol Administrador)
    (1, 1),
    -- Clientes (rol Cliente)
    (2, 2), (3, 2), (4, 2), (5, 2), (6, 2), (7, 2), (8, 2), (9, 2), (10, 2),
    (11, 2), (12, 2), (13, 2), (14, 2), (15, 2), (16, 2), (17, 2), (18, 2), (20, 2),
    -- Vendedor (rol Vendedor + Cliente)
    (19, 3), (19, 2);

-- ============================================
-- 4. PRODUCTOS (50 productos variados)
-- ============================================

-- Electrónica (15 productos)
INSERT INTO productos (nombre, estado_producto_id, categoria_id, valor_unitario, iva, imagen, descripcion, creation_date) VALUES
    ('Laptop Dell Inspiron 15', 1, 1, 2499000.00, 19.00, 'https://images.unsplash.com/photo-1588872657578-7efd1f1555ed?w=800&q=80', 'Laptop Dell Inspiron 15 con procesador Intel Core i5, 8GB RAM, 512GB SSD', '2024-01-10 10:00:00'),
    ('iPhone 15 Pro 128GB', 1, 1, 4999000.00, 19.00, 'https://images.unsplash.com/photo-1695048133142-1a20484d2569?w=800&q=80', 'iPhone 15 Pro con chip A17 Pro, cámara de 48MP, pantalla Super Retina XDR', '2024-01-15 11:00:00'),
    ('Samsung Galaxy S24 Ultra', 1, 1, 5499000.00, 19.00, 'https://images.unsplash.com/photo-1610945415295-d9bbf067e59c?w=800&q=80', 'Samsung Galaxy S24 Ultra con S Pen, cámara de 200MP, pantalla Dynamic AMOLED 2X', '2024-01-20 12:00:00'),
    ('iPad Air 256GB', 1, 1, 3299000.00, 19.00, 'https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?w=800&q=80', 'iPad Air con chip M1, pantalla Liquid Retina de 10.9 pulgadas', '2024-02-01 09:00:00'),
    ('AirPods Pro 2da Gen', 1, 1, 899000.00, 19.00, 'https://images.unsplash.com/photo-1606841837239-c5a1a4a07af7?w=800&q=80', 'AirPods Pro con cancelación activa de ruido, audio espacial', '2024-02-05 10:00:00'),
    ('Monitor LG UltraWide 34"', 1, 1, 1899000.00, 19.00, 'https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?w=800&q=80', 'Monitor LG UltraWide 34" QHD, IPS, HDR10', '2024-02-10 11:00:00'),
    ('Teclado Mecánico Logitech', 1, 1, 399000.00, 19.00, 'https://images.unsplash.com/photo-1595225476474-87563907a212?w=800&q=80', 'Teclado mecánico Logitech con switches táctiles, RGB', '2024-02-15 12:00:00'),
    ('Mouse Gamer Razer', 1, 1, 299000.00, 19.00, 'https://images.unsplash.com/photo-1527864550417-7fd91fc51a46?w=800&q=80', 'Mouse gamer Razer con sensor óptico 20K DPI, RGB Chroma', '2024-03-01 13:00:00'),
    ('Webcam Logitech C920', 1, 1, 349000.00, 19.00, 'https://images.unsplash.com/photo-1587826080692-f439cd0b70da?w=800&q=80', 'Webcam Logitech C920 Full HD 1080p con micrófono estéreo', '2024-03-05 14:00:00'),
    ('Audífonos Sony WH-1000XM5', 1, 1, 1299000.00, 19.00, 'https://images.unsplash.com/photo-1546435770-a3e426bf472b?w=800&q=80', 'Audífonos Sony con cancelación de ruido líder en la industria', '2024-03-10 15:00:00'),
    ('Smartwatch Apple Watch 9', 1, 1, 1999000.00, 19.00, 'https://images.unsplash.com/photo-1579586337278-3befd40fd17a?w=800&q=80', 'Apple Watch Series 9 con GPS, pantalla Always-On Retina', '2024-03-15 16:00:00'),
    ('Tablet Samsung Galaxy Tab S9', 1, 1, 2799000.00, 19.00, 'https://images.unsplash.com/photo-1561154464-82e9adf32764?w=800&q=80', 'Tablet Samsung con S Pen, pantalla AMOLED de 11 pulgadas', '2024-04-01 09:00:00'),
    ('Cámara Canon EOS R50', 1, 1, 3499000.00, 19.00, 'https://images.unsplash.com/photo-1606980702371-8412ff0a0bf2?w=800&q=80', 'Cámara mirrorless Canon EOS R50 con sensor CMOS de 24.2MP', '2024-04-05 10:00:00'),
    ('Disco SSD Samsung 1TB', 4, 1, 449000.00, 19.00, 'https://images.unsplash.com/photo-1597872200969-2b65d56bd16b?w=800&q=80', 'Disco SSD NVMe Samsung 980 PRO 1TB, velocidad hasta 7000 MB/s', '2024-04-10 11:00:00'),
    ('Router WiFi 6 TP-Link', 1, 1, 499000.00, 19.00, 'https://images.unsplash.com/photo-1606904825846-647eb07f5be2?w=800&q=80', 'Router WiFi 6 TP-Link AX3000 con tecnología MU-MIMO', '2024-04-15 12:00:00');

-- Ropa (15 productos)
INSERT INTO productos (nombre, estado_producto_id, categoria_id, valor_unitario, iva, imagen, descripcion, creation_date) VALUES
    ('Camiseta Nike Dri-FIT', 1, 2, 89000.00, 19.00, 'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=800&q=80', 'Camiseta deportiva Nike con tecnología Dri-FIT para control de humedad', '2024-01-12 10:00:00'),
    ('Jeans Levi''s 501 Original', 1, 2, 249000.00, 19.00, 'https://images.unsplash.com/photo-1542272604-787c3835535d?w=800&q=80', 'Jeans clásicos Levi''s 501 Original Fit, 100% algodón', '2024-01-18 11:00:00'),
    ('Chaqueta Adidas Windbreaker', 1, 2, 299000.00, 19.00, 'https://images.unsplash.com/photo-1551488831-00ddcb6c6bd3?w=800&q=80', 'Chaqueta cortavientos Adidas con capucha ajustable', '2024-01-25 12:00:00'),
    ('Zapatillas Nike Air Max', 1, 2, 459000.00, 19.00, 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=800&q=80', 'Zapatillas Nike Air Max con amortiguación visible', '2024-02-03 13:00:00'),
    ('Sudadera Puma Essential', 1, 2, 159000.00, 19.00, 'https://images.unsplash.com/photo-1556821840-3a63f95609a7?w=800&q=80', 'Sudadera Puma con capucha y bolsillo canguro', '2024-02-08 14:00:00'),
    ('Pantalón Deportivo Reebok', 1, 2, 129000.00, 19.00, 'https://images.unsplash.com/photo-1506629082955-511b1aa562c8?w=800&q=80', 'Pantalón deportivo Reebok con tecnología Speedwick', '2024-02-12 15:00:00'),
    ('Camisa Polo Lacoste', 1, 2, 279000.00, 19.00, 'https://images.unsplash.com/photo-1586363104862-3a5e2ab60d99?w=800&q=80', 'Camisa polo Lacoste clásica en piqué de algodón', '2024-02-18 16:00:00'),
    ('Vestido Zara Floral', 8, 2, 179000.00, 19.00, 'https://images.unsplash.com/photo-1595777457583-95e059d581b8?w=800&q=80', 'Vestido Zara con estampado floral, ideal para verano', '2024-03-02 09:00:00'),
    ('Chaqueta de Cuero H&M', 1, 2, 399000.00, 19.00, 'https://images.unsplash.com/photo-1551028719-00167b16eac5?w=800&q=80', 'Chaqueta de cuero sintético H&M con diseño biker', '2024-03-08 10:00:00'),
    ('Pantalón Cargo Levi''s', 1, 2, 219000.00, 19.00, 'https://images.unsplash.com/photo-1473966968600-fa801b869a1a?w=800&q=80', 'Pantalón cargo Levi''s con múltiples bolsillos', '2024-03-12 11:00:00'),
    ('Blusa Mango Seda', 1, 2, 189000.00, 19.00, 'https://images.unsplash.com/photo-1618932260643-eee4a2f652a6?w=800&q=80', 'Blusa Mango en tejido satinado, corte fluido', '2024-03-18 12:00:00'),
    ('Zapatos Clarks Desert Boot', 1, 2, 389000.00, 19.00, 'https://images.unsplash.com/photo-1608256246200-53e635b5b65f?w=800&q=80', 'Botines Clarks Desert Boot en cuero nobuck', '2024-04-02 13:00:00'),
    ('Bufanda Cashmere', 1, 2, 149000.00, 19.00, 'https://images.unsplash.com/photo-1601924994987-69e26d50dc26?w=800&q=80', 'Bufanda de cashmere suave y cálida', '2024-04-08 14:00:00'),
    ('Gorra New Era 9FIFTY', 1, 2, 99000.00, 19.00, 'https://images.unsplash.com/photo-1588850561407-ed78c282e89b?w=800&q=80', 'Gorra New Era 9FIFTY snapback con visera plana', '2024-04-12 15:00:00'),
    ('Cinturón Cuero Tommy', 1, 2, 129000.00, 19.00, 'https://images.unsplash.com/photo-1624222247344-550fb60583c2?w=800&q=80', 'Cinturón Tommy Hilfiger en cuero genuino', '2024-04-18 16:00:00');

-- Hogar (10 productos)
INSERT INTO productos (nombre, estado_producto_id, categoria_id, valor_unitario, iva, imagen, descripcion, creation_date) VALUES
    ('Licuadora Oster Pro 1200', 1, 3, 329000.00, 19.00, 'https://images.unsplash.com/photo-1585515320310-259814833e62?w=800&q=80', 'Licuadora Oster Pro 1200 con motor de 1200W y jarra de vidrio', '2024-01-14 10:00:00'),
    ('Cafetera Nespresso Vertuo', 1, 3, 599000.00, 19.00, 'https://images.unsplash.com/photo-1517668808822-9ebb02f2a0e6?w=800&q=80', 'Cafetera Nespresso Vertuo con sistema de cápsulas', '2024-01-22 11:00:00'),
    ('Aspiradora Robot iRobot', 1, 3, 1899000.00, 19.00, 'https://images.unsplash.com/photo-1558317374-067fb5f30001?w=800&q=80', 'Aspiradora robot iRobot Roomba con mapeo inteligente', '2024-02-02 12:00:00'),
    ('Microondas Samsung 28L', 1, 3, 549000.00, 19.00, 'https://images.unsplash.com/photo-1585659722983-3a675dabf23d?w=800&q=80', 'Microondas Samsung de 28 litros con grill', '2024-02-14 13:00:00'),
    ('Juego de Sábanas Algodón', 1, 3, 159000.00, 19.00, 'https://images.unsplash.com/photo-1631049035634-c04e3f7c821b?w=800&q=80', 'Juego de sábanas 100% algodón, king size', '2024-03-03 14:00:00'),
    ('Lámpara de Mesa LED', 1, 3, 89000.00, 19.00, 'https://images.unsplash.com/photo-1507473885765-e6ed057f782c?w=800&q=80', 'Lámpara de mesa LED con regulador de intensidad', '2024-03-14 15:00:00'),
    ('Silla Gamer Ergonómica', 1, 3, 899000.00, 19.00, 'https://images.unsplash.com/photo-1598550476439-6847785fcea6?w=800&q=80', 'Silla gamer ergonómica con soporte lumbar y reposabrazos 4D', '2024-04-03 16:00:00'),
    ('Set de Ollas Antiadherentes', 1, 3, 299000.00, 19.00, 'https://images.unsplash.com/photo-1584990347449-39b13d77b6e7?w=800&q=80', 'Set de 6 ollas antiadherentes con tapas de vidrio', '2024-04-14 09:00:00'),
    ('Toallas Premium Set 6pz', 1, 3, 189000.00, 19.00, 'https://images.unsplash.com/photo-1582735689369-4fe89db7114c?w=800&q=80', 'Set de 6 toallas premium de algodón egipcio', '2024-05-02 10:00:00'),
    ('Cortinas Blackout 2pz', 1, 3, 129000.00, 19.00, 'https://images.unsplash.com/photo-1577454715674-c7c6f7d69e7a?w=800&q=80', 'Par de cortinas blackout para bloquear luz', '2024-05-12 11:00:00');

-- Deportes (5 productos)
INSERT INTO productos (nombre, estado_producto_id, categoria_id, valor_unitario, iva, imagen, descripcion, creation_date) VALUES
    ('Bicicleta de Montaña Trek', 1, 4, 2899000.00, 19.00, 'https://images.unsplash.com/photo-1576435728678-68d0fbf94e91?w=800&q=80', 'Bicicleta de montaña Trek con suspensión completa', '2024-01-16 10:00:00'),
    ('Balón de Fútbol Nike', 1, 4, 129000.00, 19.00, 'https://images.unsplash.com/photo-1614632537423-1e6c2e7e0acd?w=800&q=80', 'Balón de fútbol Nike tamaño oficial', '2024-02-04 11:00:00'),
    ('Pesas Ajustables 20kg', 1, 4, 449000.00, 19.00, 'https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=800&q=80', 'Par de pesas ajustables hasta 20kg cada una', '2024-03-04 12:00:00'),
    ('Colchoneta Yoga Premium', 1, 4, 89000.00, 19.00, 'https://images.unsplash.com/photo-1601925260368-ae2f83cf8b7f?w=800&q=80', 'Colchoneta de yoga premium antideslizante', '2024-04-04 13:00:00'),
    ('Raqueta de Tenis Wilson', 1, 4, 599000.00, 19.00, 'https://images.unsplash.com/photo-1622163642998-1ea32b0bbc67?w=800&q=80', 'Raqueta de tenis Wilson Pro Staff', '2024-05-04 14:00:00');

-- Libros (5 productos)
INSERT INTO productos (nombre, estado_producto_id, categoria_id, valor_unitario, iva, imagen, descripcion, creation_date) VALUES
    ('Cien Años de Soledad', 1, 5, 49000.00, 0.00, 'https://images.unsplash.com/photo-1544947950-fa07a98d237f?w=800&q=80', 'Novela de Gabriel García Márquez, edición especial', '2024-01-19 10:00:00'),
    ('El Principito', 1, 5, 39000.00, 0.00, 'https://images.unsplash.com/photo-1512820790803-83ca734da794?w=800&q=80', 'Clásico de Antoine de Saint-Exupéry, edición ilustrada', '2024-02-06 11:00:00'),
    ('1984 George Orwell', 1, 5, 45000.00, 0.00, 'https://images.unsplash.com/photo-1495640388908-05fa85288e61?w=800&q=80', 'Novela distópica de George Orwell', '2024-03-06 12:00:00'),
    ('Harry Potter Colección', 1, 5, 299000.00, 0.00, 'https://images.unsplash.com/photo-1621351183012-e2f9972dd9bf?w=800&q=80', 'Colección completa de 7 libros de Harry Potter', '2024-04-06 13:00:00'),
    ('El Arte de la Guerra', 1, 5, 35000.00, 0.00, 'https://images.unsplash.com/photo-1589829085413-56de8ae18c73?w=800&q=80', 'Tratado militar clásico de Sun Tzu', '2024-05-06 14:00:00');

-- ============================================
-- 5. STOCK (Inventario para todos los productos)
-- ============================================

INSERT INTO stock (producto_id, cantidad) VALUES
    (1, 15), (2, 25), (3, 20), (4, 18), (5, 50),
    (6, 12), (7, 30), (8, 35), (9, 22), (10, 28),
    (11, 17), (12, 14), (13, 8), (14, 0), (15, 24),
    (16, 100), (17, 75), (18, 45), (19, 60), (20, 80),
    (21, 55), (22, 40), (23, 32), (24, 48), (25, 28),
    (26, 65), (27, 90), (28, 70), (29, 38), (30, 42),
    (31, 25), (32, 30), (33, 18), (34, 22), (35, 50),
    (36, 20), (37, 12), (38, 8), (39, 15), (40, 10),
    (41, 5), (42, 35), (43, 40), (44, 45), (45, 28),
    (46, 120), (47, 95), (48, 88), (49, 150), (50, 110);

-- ============================================
-- 6. CARRITOS (10 carritos activos)
-- ============================================

INSERT INTO carrito (usuario_rol_id) VALUES
    (2), (3), (4), (5), (6), (7), (8), (9), (10), (11);

-- ============================================
-- 7. PRODUCTOS_CARRITO (Productos en carritos)
-- ============================================

INSERT INTO productos_carrito (carrito_id, producto_id, cantidad) VALUES
    -- Carrito 1 (María García)
    (1, 2, 1),  -- iPhone 15 Pro
    (1, 5, 2),  -- AirPods Pro
    -- Carrito 2 (Juan Rodríguez)
    (2, 1, 1),  -- Laptop Dell
    (2, 7, 1),  -- Teclado Mecánico
    (2, 8, 1),  -- Mouse Gamer
    -- Carrito 3 (Ana Martínez)
    (3, 16, 3), -- Camisetas Nike
    (3, 19, 2), -- Zapatillas Nike
    -- Carrito 4 (Carlos López)
    (4, 31, 1), -- Licuadora Oster
    (4, 32, 1), -- Cafetera Nespresso
    -- Carrito 5 (Laura Fernández)
    (5, 24, 1), -- Vestido Zara
    (5, 27, 1), -- Blusa Mango
    -- Carrito 6 (Pedro Sánchez)
    (6, 41, 1), -- Bicicleta Trek
    (6, 44, 1), -- Colchoneta Yoga
    -- Carrito 7 (Sofía Ramírez)
    (7, 10, 1), -- Audífonos Sony
    (7, 11, 1), -- Apple Watch
    -- Carrito 8 (Diego Torres)
    (8, 46, 2), -- Cien Años de Soledad
    (8, 47, 1), -- El Principito
    -- Carrito 9 (Valentina Castro)
    (9, 3, 1),  -- Samsung S24 Ultra
    (9, 12, 1), -- Galaxy Tab S9
    -- Carrito 10 (Andrés Morales)
    (10, 37, 1), -- Silla Gamer
    (10, 6, 1);  -- Monitor LG

-- ============================================
-- 8. REFERENCIAS (10 referencias de pago)
-- ============================================

INSERT INTO referencias (numero) VALUES
    ('REF-2024-00001'),
    ('REF-2024-00002'),
    ('REF-2024-00003'),
    ('REF-2024-00004'),
    ('REF-2024-00005'),
    ('REF-2024-00006'),
    ('REF-2024-00007'),
    ('REF-2024-00008'),
    ('REF-2024-00009'),
    ('REF-2024-00010');

-- ============================================
-- 9. PAGOS (10 pagos realizados)
-- ============================================

INSERT INTO pago (carrito_id, tipo_pago_id, fecha_pago, referencia_id, estado_pago_id) VALUES
    (1, 'credito', '2024-06-01 15:30:00', 1, 3),  -- Aprobado
    (2, 'debito', '2024-06-02 10:15:00', 2, 3),   -- Aprobado
    (3, 'credito', '2024-06-03 14:20:00', 3, 3),  -- Aprobado
    (4, 'debito', '2024-06-04 11:45:00', 4, 3),   -- Aprobado
    (5, 'credito', '2024-06-05 16:10:00', 5, 2),  -- Procesando
    (6, 'debito', '2024-06-06 09:30:00', 6, 3),   -- Aprobado
    (7, 'credito', '2024-06-07 13:50:00', 7, 3),  -- Aprobado
    (8, 'debito', '2024-06-08 12:25:00', 8, 4),   -- Rechazado
    (9, 'credito', '2024-06-09 15:40:00', 9, 1),  -- Pendiente
    (10, 'debito', '2024-06-10 10:55:00', 10, 3); -- Aprobado

-- ============================================
-- 10. PAGOS DÉBITO (Detalles de pagos con débito)
-- ============================================

INSERT INTO pago_debito (pago_id, fecha_vencimiento, nombre_titular, numero_tarjeta) VALUES
    (2, '2025-12-31', 'Juan Rodríguez Silva', '4532-****-****-1234'),
    (4, '2026-03-31', 'Carlos López Fernández', '4532-****-****-5678'),
    (6, '2025-09-30', 'Pedro Sánchez Díaz', '4532-****-****-9012'),
    (8, '2026-01-31', 'Diego Torres Castro', '4532-****-****-3456'),
    (10, '2025-11-30', 'Andrés Morales Herrera', '4532-****-****-7890');

-- ============================================
-- 11. PAGOS CRÉDITO (Detalles de pagos con crédito)
-- ============================================

INSERT INTO pago_credito (pago_id, numero_de_cuotas, nombre_titular, numero_tarjeta, fecha_vencimiento) VALUES
    (1, 12, 'María García López', '5425-****-****-2345', '2026-08-31'),
    (3, 6, 'Ana Martínez Ruiz', '5425-****-****-6789', '2025-10-31'),
    (5, 18, 'Laura Fernández Gómez', '5425-****-****-0123', '2026-06-30'),
    (7, 12, 'Sofía Ramírez Torres', '5425-****-****-4567', '2025-12-31'),
    (9, 24, 'Valentina Castro Morales', '5425-****-****-8901', '2026-04-30');

-- ============================================
-- FIN DEL SCRIPT DE DATOS DE PRUEBA
-- ============================================

-- Verificación de datos insertados
SELECT 
    (SELECT COUNT(*) FROM credenciales) as credenciales,
    (SELECT COUNT(*) FROM usuarios) as usuarios,
    (SELECT COUNT(*) FROM usuario_rol) as usuario_roles,
    (SELECT COUNT(*) FROM productos) as productos,
    (SELECT COUNT(*) FROM stock) as stock,
    (SELECT COUNT(*) FROM carrito) as carritos,
    (SELECT COUNT(*) FROM productos_carrito) as items_carrito,
    (SELECT COUNT(*) FROM pago) as pagos;


