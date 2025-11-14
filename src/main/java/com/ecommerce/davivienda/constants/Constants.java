package com.ecommerce.davivienda.constants;

/**
 * Clase de constantes centralizada para todo el sistema.
 * Define mensajes de error y códigos de error estandarizados.
 * 
 * <p>Formato de códigos de error: ED-XXX-NN</p>
 * <ul>
 *   <li>ED = Ecommerce Davivienda</li>
 *   <li>XXX = Capa/Módulo (3 letras)</li>
 *   <li>NN = Número secuencial (2 dígitos)</li>
 * </ul>
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
public class Constants {

    // ==================== USER - ERROR MESSAGES ====================
    
    /**
     * Mensaje de error cuando el usuario no existe en el sistema.
     */
    public static final String ERROR_USER_NOT_FOUND = "Usuario no encontrado";
    
    /**
     * Mensaje de error cuando el usuario está inactivo.
     */
    public static final String ERROR_USER_INACTIVE = "Usuario inactivo";
    
    /**
     * Mensaje de error cuando el usuario no tiene rol activo asignado.
     */
    public static final String ERROR_USER_NO_ACTIVE_ROLE = "Usuario sin rol activo";

    // ==================== USER - SUCCESS MESSAGES ====================
    
    /**
     * Mensaje de éxito al crear un usuario.
     */
    public static final String SUCCESS_USER_CREATED = "Usuario creado exitosamente";
    
    /**
     * Mensaje de éxito al consultar un usuario.
     */
    public static final String SUCCESS_USER_FOUND = "Usuario encontrado exitosamente";
    
    /**
     * Mensaje de éxito al actualizar un usuario.
     */
    public static final String SUCCESS_USER_UPDATED = "Usuario actualizado exitosamente";
    
    /**
     * Mensaje de éxito al eliminar un usuario.
     */
    public static final String SUCCESS_USER_DELETED = "Usuario eliminado exitosamente";
    
    /**
     * Mensaje de éxito al consultar todos los usuarios.
     */
    public static final String SUCCESS_USERS_FOUND = "Usuarios encontrados exitosamente";
    
    /**
     * Mensaje de éxito al activar un usuario.
     */
    public static final String SUCCESS_USER_ACTIVATED = "Usuario activado exitosamente";
    
    /**
     * Mensaje de éxito al cambiar la contraseña.
     */
    public static final String SUCCESS_PASSWORD_CHANGED = "Contraseña actualizada exitosamente";
    
    /**
     * Mensaje de éxito al enviar correo de recuperación de contraseña.
     */
    public static final String SUCCESS_PASSWORD_RECOVERY_EMAIL_SENT = "Se ha enviado un correo con instrucciones para cambiar la contraseña";

    // ==================== USER - ERROR MESSAGES ====================
    
    /**
     * Mensaje de error cuando el correo ya está registrado.
     */
    public static final String ERROR_EMAIL_EXISTS = "El correo electrónico ya está registrado";
    
    /**
     * Mensaje de error cuando el tipo de documento no existe.
     */
    public static final String ERROR_DOCUMENT_TYPE_NOT_FOUND = "Tipo de documento no encontrado";
    
    /**
     * Mensaje de error cuando el rol no existe.
     */
    public static final String ERROR_ROLE_NOT_FOUND = "Rol no encontrado";
    
    /**
     * Mensaje de error cuando el estado de usuario no existe.
     */
    public static final String ERROR_STATUS_NOT_FOUND = "Estado de usuario no encontrado";
    
    /**
     * Mensaje de error cuando la contraseña está vacía.
     */
    public static final String ERROR_PASSWORD_EMPTY = "La contraseña no puede estar vacía";
    
    /**
     * Mensaje de error cuando el ID del usuario es nulo en actualización.
     */
    public static final String ERROR_USER_ID_NULL = "El ID del usuario es obligatorio para actualizar";
    
    /**
     * Mensaje de error cuando la combinación de documento ya existe.
     */
    public static final String ERROR_DOCUMENT_COMBINATION_EXISTS = "Ya existe un usuario con este tipo y número de documento";
    
    /**
     * Mensaje de error cuando no se proporcionan roles.
     */
    public static final String ERROR_ROLES_EMPTY = "Debe proporcionar al menos un rol";
    
    /**
     * Mensaje de error cuando hay roles duplicados en la solicitud.
     */
    public static final String ERROR_ROLES_DUPLICATED = "Los roles no pueden estar duplicados";
    
    /**
     * Mensaje de error cuando el usuario intenta actualizar otro usuario.
     */
    public static final String ERROR_USER_UNAUTHORIZED_UPDATE = "No tiene permiso para modificar este usuario";
    
    /**
     * Mensaje de error cuando el usuario intenta cambiar la contraseña de otro usuario.
     */
    public static final String ERROR_USER_UNAUTHORIZED_PASSWORD_CHANGE = "No tiene permiso para cambiar la contraseña de este usuario";

    // ==================== USER - ERROR CODES ====================
    
    /**
     * Código de error: Usuario no encontrado.
     * Formato: ED-USR-01 (Ecommerce Davivienda - User - 01)
     */
    public static final String CODE_USER_NOT_FOUND = "ED-USR-01";
    
    /**
     * Código de error: Usuario inactivo.
     * Formato: ED-USR-02 (Ecommerce Davivienda - User - 02)
     */
    public static final String CODE_USER_INACTIVE = "ED-USR-02";
    
    /**
     * Código de error: Usuario sin rol activo.
     * Formato: ED-USR-03 (Ecommerce Davivienda - User - 03)
     */
    public static final String CODE_USER_NO_ACTIVE_ROLE = "ED-USR-03";
    
    /**
     * Código de error: Correo electrónico ya registrado.
     * Formato: ED-USR-04 (Ecommerce Davivienda - User - 04)
     */
    public static final String CODE_EMAIL_EXISTS = "ED-USR-04";
    
    /**
     * Código de error: Tipo de documento no encontrado.
     * Formato: ED-USR-05 (Ecommerce Davivienda - User - 05)
     */
    public static final String CODE_DOCUMENT_TYPE_NOT_FOUND = "ED-USR-05";
    
    /**
     * Código de error: Rol no encontrado.
     * Formato: ED-USR-06 (Ecommerce Davivienda - User - 06)
     */
    public static final String CODE_ROLE_NOT_FOUND = "ED-USR-06";
    
    /**
     * Código de error: Estado de usuario no encontrado.
     * Formato: ED-USR-07 (Ecommerce Davivienda - User - 07)
     */
    public static final String CODE_STATUS_NOT_FOUND = "ED-USR-07";
    
    /**
     * Código de error: Contraseña vacía.
     * Formato: ED-USR-08 (Ecommerce Davivienda - User - 08)
     */
    public static final String CODE_PASSWORD_EMPTY = "ED-USR-08";
    
    /**
     * Código de error: ID de usuario nulo en actualización.
     * Formato: ED-USR-09 (Ecommerce Davivienda - User - 09)
     */
    public static final String CODE_USER_ID_NULL = "ED-USR-09";
    
    /**
     * Código de error: Combinación de documento ya existe.
     * Formato: ED-USR-10 (Ecommerce Davivienda - User - 10)
     */
    public static final String CODE_DOCUMENT_COMBINATION_EXISTS = "ED-USR-10";
    
    /**
     * Código de error: Lista de roles vacía.
     * Formato: ED-USR-11 (Ecommerce Davivienda - User - 11)
     */
    public static final String CODE_ROLES_EMPTY = "ED-USR-11";
    
    /**
     * Código de error: Roles duplicados en solicitud.
     * Formato: ED-USR-13 (Ecommerce Davivienda - User - 13)
     */
    public static final String CODE_ROLES_DUPLICATED = "ED-USR-13";
    
    /**
     * Código de error: Usuario no autorizado para actualizar otro usuario.
     * Formato: ED-USR-14 (Ecommerce Davivienda - User - 14)
     */
    public static final String CODE_USER_UNAUTHORIZED_UPDATE = "ED-USR-14";
    
    /**
     * Código de error: Usuario no autorizado para cambiar contraseña de otro usuario.
     * Formato: ED-USR-15 (Ecommerce Davivienda - User - 15)
     */
    public static final String CODE_USER_UNAUTHORIZED_PASSWORD_CHANGE = "ED-USR-15";

    // ==================== CREDENTIALS - ERROR MESSAGES ====================
    
    /**
     * Mensaje de error al leer credenciales del request.
     */
    public static final String ERROR_CREDENTIALS_READ = "Error al leer credenciales del request";
    
    /**
     * Mensaje de error al decodificar valor en Base64.
     */
    public static final String ERROR_BASE64_DECODE = "Error al decodificar contraseña desde Base64";

    // ==================== AUTHENTICATION - ERROR MESSAGES ====================
    
    /**
     * Mensaje de error de autenticación fallida.
     */
    public static final String ERROR_AUTHENTICATION_FAILED = "Error en la autenticación: credenciales incorrectas";

    // ==================== AUTHENTICATION - ERROR CODES ====================
    
    /**
     * Código de error: Autenticación fallida.
     * Formato: ED-AUT-01 (Ecommerce Davivienda - Autenticación - 01)
     */
    public static final String CODE_AUTHENTICATION_FAILED = "ED-AUT-01";
    
    /**
     * Código de error: Error al decodificar Base64.
     * Formato: ED-AUT-02 (Ecommerce Davivienda - Autenticación - 02)
     */
    public static final String CODE_BASE64_DECODE_ERROR = "ED-AUT-02";

    // ==================== JWT VALIDATION - ERROR MESSAGES ====================
    
    /**
     * Mensaje de error cuando el token JWT es inválido.
     */
    public static final String ERROR_JWT_TOKEN_INVALID = "Token JWT inválido o expirado";
    
    /**
     * Mensaje de error al parsear authorities del token JWT.
     */
    public static final String ERROR_JWT_AUTHORITIES_PARSE = "Error al procesar permisos del token JWT";

    /**
     * Mensaje de error al extraer información del token JWT.
     */
    public static final String ERROR_JWT_TOKEN_INFO_EXTRACTION = "Error al extraer información del token JWT";

    // ==================== JWT VALIDATION - ERROR CODES ====================
    
    /**
     * Código de error: Token JWT inválido.
     * Formato: ED-JWT-01 (Ecommerce Davivienda - JWT - 01)
     */
    public static final String CODE_JWT_TOKEN_INVALID = "ED-JWT-01";
    
    /**
     * Código de error: Error al parsear authorities del JWT.
     * Formato: ED-JWT-02 (Ecommerce Davivienda - JWT - 02)
     */
    public static final String CODE_JWT_AUTHORITIES_PARSE_ERROR = "ED-JWT-02";
    
    /**
     * Código de error: Error al extraer información del token JWT.
     * Formato: ED-JWT-03 (Ecommerce Davivienda - JWT - 03)
     */
    public static final String CODE_JWT_TOKEN_INFO_ERROR = "ED-JWT-03";

    // ==================== PRODUCT - SUCCESS MESSAGES ====================
    
    /**
     * Mensaje de éxito al crear un producto.
     */
    public static final String SUCCESS_PRODUCT_CREATED = "Producto creado exitosamente";
    
    /**
     * Mensaje de éxito al consultar un producto.
     */
    public static final String SUCCESS_PRODUCT_FOUND = "Producto encontrado";
    
    /**
     * Mensaje de éxito al actualizar un producto.
     */
    public static final String SUCCESS_PRODUCT_UPDATED = "Producto actualizado exitosamente";
    
    /**
     * Mensaje de éxito al eliminar un producto.
     */
    public static final String SUCCESS_PRODUCT_DELETED = "Producto eliminado exitosamente";
    
    /**
     * Mensaje de éxito al activar un producto.
     */
    public static final String SUCCESS_PRODUCT_ACTIVATED = "Producto activado exitosamente";
    
    /**
     * Mensaje de éxito al agregar inventario.
     */
    public static final String SUCCESS_INVENTORY_ADDED = "Inventario agregado exitosamente";
    
    /**
     * Mensaje de éxito al listar productos.
     */
    public static final String SUCCESS_PRODUCTS_LISTED = "Productos listados exitosamente";
    
    /**
     * Mensaje de éxito al buscar productos.
     */
    public static final String SUCCESS_PRODUCTS_SEARCH = "Búsqueda completada";

    // ==================== PRODUCT - ERROR MESSAGES ====================
    
    /**
     * Mensaje de error cuando el producto no existe.
     */
    public static final String ERROR_PRODUCT_NOT_FOUND = "Producto no encontrado";
    
    /**
     * Mensaje de error cuando la categoría no existe.
     */
    public static final String ERROR_CATEGORY_NOT_FOUND = "Categoría no encontrada";
    
    /**
     * Mensaje de error cuando la categoría está inactiva.
     */
    public static final String ERROR_CATEGORY_INACTIVE = "La categoría está inactiva";
    
    /**
     * Mensaje de error cuando el nombre del producto ya existe.
     */
    public static final String ERROR_PRODUCT_NAME_EXISTS = "Ya existe un producto con ese nombre";
    
    /**
     * Mensaje de error cuando el inventario es insuficiente.
     */
    public static final String ERROR_INSUFFICIENT_INVENTORY = "Inventario insuficiente";
    
    /**
     * Mensaje de error cuando el producto está inactivo.
     */
    public static final String ERROR_PRODUCT_INACTIVE = "El producto está inactivo";
    
    /**
     * Mensaje de error cuando el precio unitario es menor al precio final.
     */
    public static final String ERROR_PRICE_INVALID = "El precio final no puede ser mayor al precio unitario";
    
    /**
     * Mensaje de error cuando la cantidad de inventario es inválida.
     */
    public static final String ERROR_INVALID_INVENTORY_QUANTITY = "La cantidad de inventario debe ser mayor a 0";

    // ==================== PRODUCT - ERROR CODES ====================
    
    /**
     * Código de error: Producto no encontrado.
     * Formato: ED-PRO-01 (Ecommerce Davivienda - Product - 01)
     */
    public static final String CODE_PRODUCT_NOT_FOUND = "ED-PRO-01";
    
    /**
     * Código de error: Categoría no encontrada.
     * Formato: ED-PRO-02 (Ecommerce Davivienda - Product - 02)
     */
    public static final String CODE_CATEGORY_NOT_FOUND = "ED-PRO-02";
    
    /**
     * Código de error: Categoría inactiva.
     * Formato: ED-PRO-03 (Ecommerce Davivienda - Product - 03)
     */
    public static final String CODE_CATEGORY_INACTIVE = "ED-PRO-03";
    
    /**
     * Código de error: Nombre de producto duplicado.
     * Formato: ED-PRO-04 (Ecommerce Davivienda - Product - 04)
     */
    public static final String CODE_PRODUCT_NAME_EXISTS = "ED-PRO-04";
    
    /**
     * Código de error: Inventario insuficiente.
     * Formato: ED-PRO-05 (Ecommerce Davivienda - Product - 05)
     */
    public static final String CODE_INSUFFICIENT_INVENTORY = "ED-PRO-05";
    
    /**
     * Código de error: Producto inactivo.
     * Formato: ED-PRO-06 (Ecommerce Davivienda - Product - 06)
     */
    public static final String CODE_PRODUCT_INACTIVE = "ED-PRO-06";
    
    /**
     * Código de error: Precio inválido.
     * Formato: ED-PRO-07 (Ecommerce Davivienda - Product - 07)
     */
    public static final String CODE_PRICE_INVALID = "ED-PRO-07";
    
    /**
     * Código de error: Cantidad de inventario inválida.
     * Formato: ED-PRO-08 (Ecommerce Davivienda - Product - 08)
     */
    public static final String CODE_INVALID_INVENTORY_QUANTITY = "ED-PRO-08";

    // ==================== CART - SUCCESS MESSAGES ====================
    
    /**
     * Mensaje de éxito al agregar item al carrito.
     */
    public static final String SUCCESS_CART_ITEM_ADDED = "Producto agregado al carrito exitosamente";
    
    /**
     * Mensaje de éxito al actualizar item del carrito.
     */
    public static final String SUCCESS_CART_ITEM_UPDATED = "Cantidad del producto actualizada exitosamente";
    
    /**
     * Mensaje de éxito al eliminar item del carrito.
     */
    public static final String SUCCESS_CART_ITEM_DELETED = "Producto eliminado del carrito exitosamente";
    
    /**
     * Mensaje de éxito al limpiar el carrito.
     */
    public static final String SUCCESS_CART_CLEARED = "Carrito limpiado exitosamente";
    
    /**
     * Mensaje de éxito al obtener items del carrito.
     */
    public static final String SUCCESS_CART_ITEMS_FOUND = "Items del carrito obtenidos exitosamente";
    
    /**
     * Mensaje de éxito al agregar múltiples items al carrito.
     */
    public static final String SUCCESS_CART_ITEMS_BATCH_ADDED = "Productos agregados al carrito exitosamente";

    // ==================== CART - ERROR MESSAGES ====================
    
    /**
     * Mensaje de error cuando el carrito no existe.
     */
    public static final String ERROR_CART_NOT_FOUND = "Carrito no encontrado";
    
    /**
     * Mensaje de error cuando el item del carrito no existe.
     */
    public static final String ERROR_CART_ITEM_NOT_FOUND = "Item del carrito no encontrado";
    
    /**
     * Mensaje de error cuando el producto ya existe en el carrito.
     */
    public static final String ERROR_CART_ITEM_ALREADY_EXISTS = "El producto ya existe en el carrito";
    
    /**
     * Mensaje de error cuando la cantidad es inválida.
     */
    public static final String ERROR_CART_INVALID_QUANTITY = "La cantidad debe ser mayor a 0";
    
    /**
     * Mensaje de error cuando el carrito está vacío.
     */
    public static final String ERROR_CART_EMPTY = "El carrito está vacío";
    
    /**
     * Mensaje de error cuando se requiere userRoleId para crear el carrito.
     */
    public static final String ERROR_CART_USER_ROLE_REQUIRED = "Se requiere userRoleId para crear el carrito automáticamente";
    
    /**
     * Mensaje de error cuando el userRoleId no existe en el sistema.
     */
    public static final String ERROR_USER_ROLE_NOT_FOUND = "El userRoleId proporcionado no existe en el sistema";
    
    /**
     * Mensaje de error cuando el usuario no se encuentra por documento.
     */
    public static final String ERROR_USER_NOT_FOUND_BY_DOCUMENT = "Usuario no encontrado con el documento proporcionado";
    
    /**
     * Mensaje de error cuando el usuario no tiene roles asignados.
     */
    public static final String ERROR_USER_WITHOUT_ROLES = "El usuario no tiene roles asignados en el sistema";
    
    /**
     * Mensaje de error cuando el usuario no tiene el rol de Cliente.
     */
    public static final String ERROR_USER_NOT_CLIENT_ROLE = "Solo los usuarios con rol de Cliente pueden agregar productos al carrito";
    
    /**
     * Mensaje de error cuando el item no pertenece al carrito del usuario.
     */
    public static final String ERROR_CART_ITEM_UNAUTHORIZED = "Item del carrito no encontrado";
    
    /**
     * Mensaje de error cuando el carrito no pertenece al usuario autenticado.
     */
    public static final String ERROR_CART_UNAUTHORIZED = "Carrito no encontrado";

    // ==================== CART - ERROR CODES ====================
    
    /**
     * Código de error: Carrito no encontrado.
     * Formato: ED-CAR-01 (Ecommerce Davivienda - Cart - 01)
     */
    public static final String CODE_CART_NOT_FOUND = "ED-CAR-01";
    
    /**
     * Código de error: Item del carrito no encontrado.
     * Formato: ED-CAR-02 (Ecommerce Davivienda - Cart - 02)
     */
    public static final String CODE_CART_ITEM_NOT_FOUND = "ED-CAR-02";
    
    /**
     * Código de error: Producto ya existe en el carrito.
     * Formato: ED-CAR-03 (Ecommerce Davivienda - Cart - 03)
     */
    public static final String CODE_CART_ITEM_ALREADY_EXISTS = "ED-CAR-03";
    
    /**
     * Código de error: Cantidad inválida.
     * Formato: ED-CAR-04 (Ecommerce Davivienda - Cart - 04)
     */
    public static final String CODE_CART_INVALID_QUANTITY = "ED-CAR-04";
    
    /**
     * Código de error: Carrito vacío.
     * Formato: ED-CAR-05 (Ecommerce Davivienda - Cart - 05)
     */
    public static final String CODE_CART_EMPTY = "ED-CAR-05";
    
    /**
     * Código de error: UserRoleId requerido para crear carrito.
     * Formato: ED-CAR-06 (Ecommerce Davivienda - Cart - 06)
     */
    public static final String CODE_CART_USER_ROLE_REQUIRED = "ED-CAR-06";
    
    /**
     * Código de error: UserRoleId no existe en el sistema.
     * Formato: ED-CAR-07 (Ecommerce Davivienda - Cart - 07)
     */
    public static final String CODE_USER_ROLE_NOT_FOUND = "ED-CAR-07";
    
    /**
     * Código de error: Usuario no encontrado por documento.
     * Formato: ED-CAR-08 (Ecommerce Davivienda - Cart - 08)
     */
    public static final String CODE_USER_NOT_FOUND_BY_DOCUMENT = "ED-CAR-08";
    
    /**
     * Código de error: Usuario sin roles asignados.
     * Formato: ED-CAR-09 (Ecommerce Davivienda - Cart - 09)
     */
    public static final String CODE_USER_WITHOUT_ROLES = "ED-CAR-09";
    
    /**
     * Código de error: Usuario sin rol de Cliente.
     * Formato: ED-CAR-10 (Ecommerce Davivienda - Cart - 10)
     */
    public static final String CODE_USER_NOT_CLIENT_ROLE = "ED-CAR-10";
    
    /**
     * Código de error: Item no pertenece al carrito del usuario.
     * Formato: ED-CAR-11 (Ecommerce Davivienda - Cart - 11)
     */
    public static final String CODE_CART_ITEM_UNAUTHORIZED = "ED-CAR-11";
    
    /**
     * Código de error: Carrito no pertenece al usuario autenticado.
     * Formato: ED-CAR-12 (Ecommerce Davivienda - Cart - 12)
     */
    public static final String CODE_CART_UNAUTHORIZED = "ED-CAR-12";
    
    /**
     * Código de error: Estado de carrito no encontrado.
     * Formato: ED-CAR-13 (Ecommerce Davivienda - Cart - 13)
     */
    public static final String CODE_CART_STATUS_NOT_FOUND = "ED-CAR-13";

    // ==================== CART STATUS - IDS ====================
    
    /**
     * ID del estado "Activo" del carrito.
     * Estado inicial cuando se crea un carrito nuevo.
     */
    public static final Integer CART_STATUS_ACTIVE = 1;
    
    /**
     * ID del estado "Procesando" del carrito.
     * Estado cuando el carrito está siendo procesado para pago.
     */
    public static final Integer CART_STATUS_PROCESSING = 2;
    
    /**
     * ID del estado "Completado" del carrito.
     * Estado cuando el pago ha sido completado exitosamente.
     */
    public static final Integer CART_STATUS_COMPLETED = 3;
    
    /**
     * ID del estado "Abandonado" del carrito.
     * Estado cuando el usuario abandona el carrito sin completar.
     */
    public static final Integer CART_STATUS_ABANDONED = 4;
    
    /**
     * ID del estado "Expirado" del carrito.
     * Estado cuando el carrito ha expirado por tiempo de inactividad.
     */
    public static final Integer CART_STATUS_EXPIRED = 5;
    
    /**
     * ID del estado "Cancelado" del carrito.
     * Estado cuando el usuario cancela el carrito explícitamente.
     */
    public static final Integer CART_STATUS_CANCELLED = 6;

    // ==================== STOCK - SUCCESS MESSAGES ====================
    
    /**
     * Mensaje de éxito cuando todos los productos tienen stock suficiente.
     */
    public static final String SUCCESS_STOCK_AVAILABLE = "Todos los productos tienen stock suficiente";

    // ==================== STOCK - ERROR MESSAGES ====================
    
    /**
     * Mensaje de error cuando no hay stock suficiente para los productos del carrito.
     */
    public static final String ERROR_INSUFFICIENT_STOCK = "Stock insuficiente para uno o más productos";
    
    /**
     * Mensaje de error cuando el stock no existe para un producto.
     */
    public static final String ERROR_STOCK_NOT_FOUND = "No existe registro de stock para el producto";
    
    /**
     * Mensaje de error cuando el carrito no tiene productos.
     */
    public static final String ERROR_CART_NO_ITEMS = "El carrito no contiene productos para validar";
    
    /**
     * Mensaje de error cuando no se encuentra el carrito del usuario.
     */
    public static final String ERROR_USER_CART_NOT_FOUND = "No se encontró carrito para el usuario especificado";

    // ==================== STOCK - ERROR CODES ====================
    
    /**
     * Código de error: Stock insuficiente.
     * Formato: ED-STO-01 (Ecommerce Davivienda - Stock - 01)
     */
    public static final String CODE_INSUFFICIENT_STOCK = "ED-STO-01";
    
    /**
     * Código de error: Stock no encontrado.
     * Formato: ED-STO-02 (Ecommerce Davivienda - Stock - 02)
     */
    public static final String CODE_STOCK_NOT_FOUND = "ED-STO-02";
    
    /**
     * Código de error: Carrito sin items.
     * Formato: ED-STO-03 (Ecommerce Davivienda - Stock - 03)
     */
    public static final String CODE_CART_NO_ITEMS = "ED-STO-03";
    
    /**
     * Código de error: Carrito de usuario no encontrado.
     * Formato: ED-STO-04 (Ecommerce Davivienda - Stock - 04)
     */
    public static final String CODE_USER_CART_NOT_FOUND = "ED-STO-04";

    // ==================== DOCUMENT TYPE - SUCCESS MESSAGES ====================
    
    /**
     * Mensaje de éxito al consultar un tipo de documento.
     */
    public static final String SUCCESS_DOCUMENT_TYPE_FOUND = "Tipo de documento encontrado exitosamente";
    
    /**
     * Mensaje de éxito al listar tipos de documento.
     */
    public static final String SUCCESS_DOCUMENT_TYPES_LISTED = "Tipos de documento listados exitosamente";
    
    /**
     * Mensaje de éxito al crear un tipo de documento.
     */
    public static final String SUCCESS_DOCUMENT_TYPE_CREATED = "Tipo de documento creado exitosamente";
    
    /**
     * Mensaje de éxito al actualizar un tipo de documento.
     */
    public static final String SUCCESS_DOCUMENT_TYPE_UPDATED = "Tipo de documento actualizado exitosamente";
    
    /**
     * Mensaje de éxito al eliminar un tipo de documento.
     */
    public static final String SUCCESS_DOCUMENT_TYPE_DELETED = "Tipo de documento eliminado exitosamente";

    // ==================== DOCUMENT TYPE - ERROR MESSAGES ====================
    
    /**
     * Mensaje de error cuando el tipo de documento no existe por ID.
     */
    public static final String ERROR_DOCUMENT_TYPE_NOT_FOUND_BY_ID = "Tipo de documento no encontrado con el ID proporcionado";
    
    /**
     * Mensaje de error cuando el código de tipo de documento ya existe.
     */
    public static final String ERROR_DOCUMENT_TYPE_CODE_EXISTS = "Ya existe un tipo de documento con ese código";
    
    /**
     * Mensaje de error cuando el nombre de tipo de documento ya existe.
     */
    public static final String ERROR_DOCUMENT_TYPE_NAME_EXISTS = "Ya existe un tipo de documento con ese nombre";
    
    /**
     * Mensaje de error cuando el tipo de documento no existe por código.
     */
    public static final String ERROR_DOCUMENT_TYPE_NOT_FOUND_BY_CODE = "Tipo de documento no encontrado con el código proporcionado";

    // ==================== DOCUMENT TYPE - ERROR CODES ====================
    
    /**
     * Código de error: Tipo de documento no encontrado por ID.
     * Formato: ED-DOC-01 (Ecommerce Davivienda - Document - 01)
     */
    public static final String CODE_DOCUMENT_TYPE_NOT_FOUND_BY_ID = "ED-DOC-01";
    
    /**
     * Código de error: Código de tipo de documento duplicado.
     * Formato: ED-DOC-02 (Ecommerce Davivienda - Document - 02)
     */
    public static final String CODE_DOCUMENT_TYPE_CODE_EXISTS = "ED-DOC-02";
    
    /**
     * Código de error: Nombre de tipo de documento duplicado.
     * Formato: ED-DOC-03 (Ecommerce Davivienda - Document - 03)
     */
    public static final String CODE_DOCUMENT_TYPE_NAME_EXISTS = "ED-DOC-03";
    
    /**
     * Código de error: Tipo de documento no encontrado por código.
     * Formato: ED-DOC-04 (Ecommerce Davivienda - Document - 04)
     */
    public static final String CODE_DOCUMENT_TYPE_NOT_FOUND_BY_CODE = "ED-DOC-04";

    // ==================== ROLE - SUCCESS MESSAGES ====================
    
    /**
     * Mensaje de éxito al consultar un rol.
     */
    public static final String SUCCESS_ROLE_FOUND = "Rol encontrado exitosamente";
    
    /**
     * Mensaje de éxito al listar roles.
     */
    public static final String SUCCESS_ROLES_LISTED = "Roles listados exitosamente";
    
    /**
     * Mensaje de éxito al crear un rol.
     */
    public static final String SUCCESS_ROLE_CREATED = "Rol creado exitosamente";
    
    /**
     * Mensaje de éxito al actualizar un rol.
     */
    public static final String SUCCESS_ROLE_UPDATED = "Rol actualizado exitosamente";
    
    /**
     * Mensaje de éxito al eliminar un rol.
     */
    public static final String SUCCESS_ROLE_DELETED = "Rol eliminado exitosamente";

    // ==================== ROLE - ERROR MESSAGES ====================
    
    /**
     * Mensaje de error cuando el rol no existe por ID.
     */
    public static final String ERROR_ROLE_NOT_FOUND_BY_ID = "Rol no encontrado con el ID proporcionado";
    
    /**
     * Mensaje de error cuando el nombre de rol ya existe.
     */
    public static final String ERROR_ROLE_NAME_EXISTS = "Ya existe un rol con ese nombre";
    
    /**
     * Mensaje de error cuando el rol no existe por nombre.
     */
    public static final String ERROR_ROLE_NOT_FOUND_BY_NAME = "Rol no encontrado con el nombre proporcionado";

    // ==================== ROLE - ERROR CODES ====================
    
    /**
     * Código de error: Rol no encontrado por ID.
     * Formato: ED-ROL-01 (Ecommerce Davivienda - Role - 01)
     */
    public static final String CODE_ROLE_NOT_FOUND_BY_ID = "ED-ROL-01";
    
    /**
     * Código de error: Nombre de rol duplicado.
     * Formato: ED-ROL-02 (Ecommerce Davivienda - Role - 02)
     */
    public static final String CODE_ROLE_NAME_EXISTS = "ED-ROL-02";
    
    /**
     * Código de error: Rol no encontrado por nombre.
     * Formato: ED-ROL-03 (Ecommerce Davivienda - Role - 03)
     */
    public static final String CODE_ROLE_NOT_FOUND_BY_NAME = "ED-ROL-03";

    // ==================== GENERIC ERROR ====================
    
    /**
     * Mensaje de error genérico del sistema.
     */
    public static final String ERROR_GENERIC = "Ha ocurrido un error inesperado en el sistema";
    
    /**
     * Código de error genérico.
     * Formato: ED-GEN-01 (Ecommerce Davivienda - Generic - 01)
     */
    public static final String CODE_GENERIC_ERROR = "ED-GEN-01";
    
    /**
     * Código de error: Validación de datos fallida.
     * Formato: ED-VAL-01 (Ecommerce Davivienda - Validation - 01)
     */
    public static final String CODE_VALIDATION_EXCEPTION = "ED-VAL-01";
    
    /**
     * Código de error: Violación de integridad de datos.
     * Formato: ED-DAT-01 (Ecommerce Davivienda - Data - 01)
     */
    public static final String CODE_DATA_INTEGRITY_VIOLATION = "ED-DAT-01";
    
    /**
     * Código de error: Acceso denegado.
     * Formato: ED-SEC-01 (Ecommerce Davivienda - Security - 01)
     */
    public static final String CODE_ACCESS_DENIED = "ED-SEC-01";

    // ==================== PAYMENT - SUCCESS MESSAGES ====================
    
    /**
     * Mensaje de éxito al procesar un pago.
     */
    public static final String SUCCESS_PAYMENT_PROCESSED = "Pago procesado exitosamente";
    
    /**
     * Mensaje de éxito al consultar un pago.
     */
    public static final String SUCCESS_PAYMENT_FOUND = "Pago encontrado exitosamente";
    
    /**
     * Mensaje de éxito al cancelar un pago.
     */
    public static final String SUCCESS_PAYMENT_CANCELLED = "Pago cancelado exitosamente";

    // ==================== PAYMENT - ERROR MESSAGES ====================
    
    /**
     * Mensaje de error cuando el pago no existe.
     */
    public static final String ERROR_PAYMENT_NOT_FOUND = "Pago no encontrado";
    
    /**
     * Mensaje de error cuando los datos de tarjeta encriptados son inválidos.
     */
    public static final String ERROR_INVALID_ENCRYPTED_DATA = "Los datos encriptados de la tarjeta son inválidos";
    
    /**
     * Mensaje de error cuando el formato de los datos de tarjeta es inválido.
     */
    public static final String ERROR_INVALID_CARD_DATA_FORMAT = "El formato de los datos de la tarjeta es inválido";
    
    /**
     * Mensaje de error cuando el tipo de pago no existe o es inválido.
     */
    public static final String ERROR_INVALID_PAYMENT_TYPE = "Tipo de pago inválido. Debe ser 'debito' o 'credito'";
    
    /**
     * Mensaje de error cuando el estado de pago no existe.
     */
    public static final String ERROR_PAYMENT_STATUS_NOT_FOUND = "Estado de pago no encontrado";
    
    /**
     * Mensaje de error cuando el número de cuotas es inválido.
     */
    public static final String ERROR_INVALID_INSTALLMENTS = "El número de cuotas debe ser mayor a 0";
    
    /**
     * Mensaje de error cuando se requiere número de cuotas para crédito.
     */
    public static final String ERROR_INSTALLMENTS_REQUIRED_FOR_CREDIT = "El número de cuotas es obligatorio para pagos a crédito";
    
    /**
     * Mensaje de error cuando el carrito está vacío.
     */
    public static final String ERROR_CART_EMPTY_FOR_PAYMENT = "El carrito está vacío. No se puede procesar el pago";
    
    /**
     * Mensaje de error cuando falla la generación de referencia.
     */
    public static final String ERROR_PAYMENT_REFERENCE_GENERATION_FAILED = "Error al generar número de referencia del pago";
    
    /**
     * Mensaje de error cuando falla el procesamiento del pago.
     */
    public static final String ERROR_PAYMENT_PROCESSING_FAILED = "Error al procesar el pago";
    
    /**
     * Mensaje de error cuando la fecha de vencimiento es inválida.
     */
    public static final String ERROR_INVALID_EXPIRATION_DATE = "Fecha de vencimiento inválida. Formato esperado: MM/YY";
    
    /**
     * Mensaje de error cuando el número de tarjeta es inválido.
     */
    public static final String ERROR_INVALID_CARD_NUMBER = "Número de tarjeta inválido";

    // ==================== PAYMENT - ERROR CODES ====================
    
    /**
     * Código de error: Pago no encontrado.
     * Formato: ED-PAY-01 (Ecommerce Davivienda - Payment - 01)
     */
    public static final String CODE_PAYMENT_NOT_FOUND = "ED-PAY-01";
    
    /**
     * Código de error: Datos encriptados inválidos.
     * Formato: ED-PAY-02 (Ecommerce Davivienda - Payment - 02)
     */
    public static final String CODE_INVALID_ENCRYPTED_DATA = "ED-PAY-02";
    
    /**
     * Código de error: Formato de datos de tarjeta inválido.
     * Formato: ED-PAY-03 (Ecommerce Davivienda - Payment - 03)
     */
    public static final String CODE_INVALID_CARD_DATA_FORMAT = "ED-PAY-03";
    
    /**
     * Código de error: Tipo de pago inválido.
     * Formato: ED-PAY-04 (Ecommerce Davivienda - Payment - 04)
     */
    public static final String CODE_INVALID_PAYMENT_TYPE = "ED-PAY-04";
    
    /**
     * Código de error: Estado de pago no encontrado.
     * Formato: ED-PAY-05 (Ecommerce Davivienda - Payment - 05)
     */
    public static final String CODE_PAYMENT_STATUS_NOT_FOUND = "ED-PAY-05";
    
    /**
     * Código de error: Número de cuotas inválido.
     * Formato: ED-PAY-06 (Ecommerce Davivienda - Payment - 06)
     */
    public static final String CODE_INVALID_INSTALLMENTS = "ED-PAY-06";
    
    /**
     * Código de error: Cuotas requeridas para crédito.
     * Formato: ED-PAY-07 (Ecommerce Davivienda - Payment - 07)
     */
    public static final String CODE_INSTALLMENTS_REQUIRED_FOR_CREDIT = "ED-PAY-07";
    
    /**
     * Código de error: Carrito vacío para pago.
     * Formato: ED-PAY-08 (Ecommerce Davivienda - Payment - 08)
     */
    public static final String CODE_CART_EMPTY_FOR_PAYMENT = "ED-PAY-08";
    
    /**
     * Código de error: Fallo al generar referencia de pago.
     * Formato: ED-PAY-09 (Ecommerce Davivienda - Payment - 09)
     */
    public static final String CODE_PAYMENT_REFERENCE_GENERATION_FAILED = "ED-PAY-09";
    
    /**
     * Código de error: Fallo al procesar pago.
     * Formato: ED-PAY-10 (Ecommerce Davivienda - Payment - 10)
     */
    public static final String CODE_PAYMENT_PROCESSING_FAILED = "ED-PAY-10";
    
    /**
     * Código de error: Fecha de vencimiento inválida.
     * Formato: ED-PAY-11 (Ecommerce Davivienda - Payment - 11)
     */
    public static final String CODE_INVALID_EXPIRATION_DATE = "ED-PAY-11";
    
    /**
     * Código de error: Número de tarjeta inválido.
     * Formato: ED-PAY-12 (Ecommerce Davivienda - Payment - 12)
     */
    public static final String CODE_INVALID_CARD_NUMBER = "ED-PAY-12";

    // ==================== CART - ERROR MESSAGES ====================

    /**
     * Mensaje de error cuando no se encuentra un usuario para crear carrito.
     */
    public static final String ERROR_USER_NOT_FOUND_FOR_CART = "Usuario no encontrado. Verifique que el usuario esté registrado";

    /**
     * Mensaje de error cuando el usuario ya tiene un carrito existente.
     */
    public static final String ERROR_USER_ALREADY_HAS_CART = "El usuario ya tiene un carrito existente. No se puede crear uno nuevo";

    /**
     * Mensaje de error cuando el usuario no tiene roles asignados.
     */
    public static final String ERROR_USER_NO_ROLES = "El usuario no tiene roles asignados. No se puede crear carrito";

    /**
     * Mensaje de error cuando no hay usuario autenticado para crear carrito.
     */
    public static final String ERROR_CART_AUTHENTICATION_REQUIRED = "Autenticación requerida. Debe iniciar sesión para crear un carrito";

    /**
     * Mensaje de error cuando falla la creación del carrito.
     */
    public static final String ERROR_CART_CREATION_FAILED = "Error al crear el carrito. Intente nuevamente";

    // ==================== CART - ERROR CODES ====================

    /**
     * Código de error: Usuario no encontrado para crear carrito.
     * Formato: ED-CRT-01 (Ecommerce Davivienda - Cart - 01)
     */
    public static final String CODE_USER_NOT_FOUND_FOR_CART = "ED-CRT-01";

    /**
     * Código de error: Usuario ya tiene un carrito existente.
     * Formato: ED-CRT-02 (Ecommerce Davivienda - Cart - 02)
     */
    public static final String CODE_USER_ALREADY_HAS_CART = "ED-CRT-02";

    /**
     * Código de error: Usuario sin roles asignados.
     * Formato: ED-CRT-03 (Ecommerce Davivienda - Cart - 03)
     */
    public static final String CODE_USER_NO_ROLES = "ED-CRT-03";

    /**
     * Código de error: Autenticación requerida para crear carrito.
     * Formato: ED-CRT-04 (Ecommerce Davivienda - Cart - 04)
     */
    public static final String CODE_CART_AUTHENTICATION_REQUIRED = "ED-CRT-04";

    /**
     * Código de error: Fallo en la creación del carrito.
     * Formato: ED-CRT-05 (Ecommerce Davivienda - Cart - 05)
     */
    public static final String CODE_CART_CREATION_FAILED = "ED-CRT-05";

    // ==================== CART - SUCCESS MESSAGES ====================

    /**
     * Mensaje de éxito al crear un carrito.
     */
    public static final String SUCCESS_CART_CREATED = "Carrito creado exitosamente";

    /**
     * Constructor privado para evitar instanciación.
     */
    private Constants() {
        throw new IllegalStateException("Utility class - No se puede instanciar");
    }
}

