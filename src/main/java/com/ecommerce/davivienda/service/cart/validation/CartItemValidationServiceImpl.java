package com.ecommerce.davivienda.service.cart.validation;

import com.ecommerce.davivienda.entity.cart.Cart;
import com.ecommerce.davivienda.entity.product.Product;
import com.ecommerce.davivienda.entity.user.DocumentType;
import com.ecommerce.davivienda.entity.user.User;
import com.ecommerce.davivienda.entity.user.UserRole;
import com.ecommerce.davivienda.exception.CartException;
import com.ecommerce.davivienda.exception.product.ProductException;
import com.ecommerce.davivienda.repository.cart.CartItemRepository;
import com.ecommerce.davivienda.repository.cart.CartRepository;
import com.ecommerce.davivienda.repository.product.ProductRepository;
import com.ecommerce.davivienda.repository.user.DocumentTypeRepository;
import com.ecommerce.davivienda.repository.user.UserRepository;
import com.ecommerce.davivienda.repository.user.UserRoleRepository;
import com.ecommerce.davivienda.service.cart.autocreate.CartAutoCreateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.ecommerce.davivienda.constants.Constants.*;

/**
 * Implementación del servicio de validación para items del carrito.
 * Aplica reglas de negocio y validaciones antes de operaciones CRUD.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CartItemValidationServiceImpl implements CartItemValidationService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final DocumentTypeRepository documentTypeRepository;
    private final CartAutoCreateService autoCreateService;

    @Override
    public Cart validateCartExists(Integer cartId) {
        log.debug("Validando existencia del carrito con ID: {}", cartId);
        
        if (cartId == null) {
            throw new CartException(ERROR_CART_NOT_FOUND, CODE_CART_NOT_FOUND);
        }
        
        return cartRepository.findById(cartId)
                .orElseThrow(() -> {
                    log.warn("Carrito no encontrado con ID: {}", cartId);
                    return new CartException(ERROR_CART_NOT_FOUND, CODE_CART_NOT_FOUND);
                });
    }
    
    @Override
    public Cart validateOrCreateCart(Integer cartId, Integer userRoleId) {
        log.debug("Validando o creando carrito con ID: {} y userRoleId: {}", cartId, userRoleId);
        
        return autoCreateService.getOrCreateCart(cartId, userRoleId);
    }

    @Override
    public Product validateProductExists(Integer productId) {
        log.debug("Validando existencia del producto con ID: {}", productId);
        
        if (productId == null) {
            throw new ProductException(ERROR_PRODUCT_NOT_FOUND, CODE_PRODUCT_NOT_FOUND);
        }
        
        return productRepository.findById(productId)
                .orElseThrow(() -> {
                    log.warn("Producto no encontrado con ID: {}", productId);
                    return new ProductException(ERROR_PRODUCT_NOT_FOUND, CODE_PRODUCT_NOT_FOUND);
                });
    }

    @Override
    public void validateProductActive(Product product) {
        if (product == null || !product.isActive()) {
            log.warn("Producto inactivo: {}", product != null ? product.getProductoId() : "null");
            throw new ProductException(ERROR_PRODUCT_INACTIVE, CODE_PRODUCT_INACTIVE);
        }
    }

    @Override
    public void validateQuantity(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            log.warn("Cantidad inválida: {}", quantity);
            throw new CartException(ERROR_CART_INVALID_QUANTITY, CODE_CART_INVALID_QUANTITY);
        }
    }

    @Override
    public void validateProductNotInCart(Integer cartId, Integer productId) {
        if (cartItemRepository.existsByCartAndProduct(cartId, productId)) {
            log.warn("El producto {} ya existe en el carrito {}", productId, cartId);
            throw new CartException(ERROR_CART_ITEM_ALREADY_EXISTS, CODE_CART_ITEM_ALREADY_EXISTS);
        }
    }
    
    @Override
    public void validateUserRoleExists(Integer userRoleId) {
        log.debug("Validando existencia del userRoleId: {}", userRoleId);
        
        if (userRoleId == null) {
            throw new CartException(ERROR_USER_ROLE_NOT_FOUND, CODE_USER_ROLE_NOT_FOUND);
        }
        
        if (!userRoleRepository.existsById(userRoleId)) {
            log.warn("UserRoleId no encontrado: {}", userRoleId);
            throw new CartException(ERROR_USER_ROLE_NOT_FOUND, CODE_USER_ROLE_NOT_FOUND);
        }
    }
    
    @Override
    public Integer getUserRoleIdFromDocument(String documentType, String documentNumber) {
        log.debug("Obteniendo userRoleId para documento: {} {}", documentType, documentNumber);
        
        DocumentType docType = documentTypeRepository.findByCodigo(documentType)
                .orElseThrow(() -> {
                    log.warn("Tipo de documento no encontrado: {}", documentType);
                    return new CartException(ERROR_DOCUMENT_TYPE_NOT_FOUND, CODE_DOCUMENT_TYPE_NOT_FOUND);
                });
        
        User user = userRepository.findByDocumentType_DocumentoIdAndNumeroDeDoc(
                docType.getDocumentoId(), documentNumber)
                .orElseThrow(() -> {
                    log.warn("Usuario no encontrado con documento: {} {}", documentType, documentNumber);
                    return new CartException(ERROR_USER_NOT_FOUND_BY_DOCUMENT, CODE_USER_NOT_FOUND_BY_DOCUMENT);
                });
        
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            log.warn("Usuario sin roles asignados: {} {}", documentType, documentNumber);
            throw new CartException(ERROR_USER_WITHOUT_ROLES, CODE_USER_WITHOUT_ROLES);
        }
        
        UserRole userRole = user.getRoles().get(0);
        Integer userRoleId = userRole.getUsuarioRolId();
        
        log.info("UserRoleId {} encontrado para usuario: {} {}", userRoleId, documentType, documentNumber);
        
        return userRoleId;
    }
    
    @Override
    public void validateUserHasClientRole(Integer userRoleId) {
        log.debug("Validando que userRoleId {} tenga rol de Cliente", userRoleId);
        
        if (userRoleId == null) {
            throw new CartException(ERROR_USER_NOT_CLIENT_ROLE, CODE_USER_NOT_CLIENT_ROLE);
        }
        
        UserRole userRole = userRoleRepository.findById(userRoleId)
                .orElseThrow(() -> {
                    log.warn("UserRoleId no encontrado: {}", userRoleId);
                    return new CartException(ERROR_USER_ROLE_NOT_FOUND, CODE_USER_ROLE_NOT_FOUND);
                });
        
        String roleName = userRole.getRole().getNombreRol();
        
        if (!"Cliente".equalsIgnoreCase(roleName)) {
            log.warn("Usuario con rol '{}' intentó agregar producto al carrito. Solo permitido para 'Cliente'", roleName);
            throw new CartException(ERROR_USER_NOT_CLIENT_ROLE, CODE_USER_NOT_CLIENT_ROLE);
        }
        
        log.info("Validación exitosa: UserRoleId {} tiene rol de Cliente", userRoleId);
    }
}

