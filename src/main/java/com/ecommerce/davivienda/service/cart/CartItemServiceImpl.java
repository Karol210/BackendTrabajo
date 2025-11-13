package com.ecommerce.davivienda.service.cart;

import com.ecommerce.davivienda.dto.cart.CartItemBatchRequestDto;
import com.ecommerce.davivienda.dto.cart.CartItemRequestDto;
import com.ecommerce.davivienda.dto.cart.CartItemResponseDto;
import com.ecommerce.davivienda.dto.cart.CartSummaryDto;
import com.ecommerce.davivienda.entity.cart.Cart;
import com.ecommerce.davivienda.entity.cart.CartItem;
import com.ecommerce.davivienda.entity.product.Product;
import com.ecommerce.davivienda.exception.CartException;
import com.ecommerce.davivienda.mapper.cart.CartItemMapper;
import com.ecommerce.davivienda.repository.cart.CartItemRepository;
import com.ecommerce.davivienda.repository.cart.CartRepository;
import com.ecommerce.davivienda.service.cart.builder.CartItemBuilderService;
import com.ecommerce.davivienda.service.cart.validation.CartItemValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ecommerce.davivienda.constants.Constants.*;

/**
 * Implementación del servicio para gestión de items del carrito.
 * Coordina validaciones, construcción y persistencia de items con cálculos.
 *
 * @author Team Ecommerce Davivienda
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final CartItemValidationService validationService;
    private final CartItemBuilderService builderService;
    private final CartItemMapper cartItemMapper;

    @Override
    @Transactional
    public CartItemResponseDto addItemToCart(CartItemRequestDto request) {
        log.info("Agregando producto {} (documento: {} {})", 
                request.getProductId(), 
                request.getDocumentType(), request.getDocumentNumber());
        
        try {
            Integer userRoleId = validationService.getUserRoleIdFromDocument(
                    request.getDocumentType(), request.getDocumentNumber());
            
            validationService.validateUserHasClientRole(userRoleId);
            
            // Buscar carrito existente del usuario o crear uno nuevo
            Cart cart = cartRepository.findByUsuarioRolId(userRoleId)
                    .orElseGet(() -> {
                        log.info("Usuario {} no tiene carrito, creando uno nuevo", userRoleId);
                        Cart newCart = Cart.builder()
                                .usuarioRolId(userRoleId)
                                .build();
                        return cartRepository.save(newCart);
                    });
            
            log.info("Usando carrito {} para usuario {}", cart.getCarritoId(), userRoleId);
            
            Product product = validationService.validateProductExists(request.getProductId());
            validationService.validateProductActive(product);
            validationService.validateQuantity(request.getQuantity());
            
            // Verificar si el producto ya existe en el carrito
            CartItem cartItem = cartItemRepository.findByCartAndProduct(
                    cart.getCarritoId(), request.getProductId())
                    .orElse(null);
            
            if (cartItem != null) {
                // Si existe, actualizar la cantidad (reemplazar con la nueva cantidad)
                Integer oldQuantity = cartItem.getCantidad();
                builderService.updateQuantity(cartItem, request.getQuantity());
                log.info("Producto {} ya existe en carrito {}, actualizando cantidad de {} a {}", 
                        request.getProductId(), cart.getCarritoId(), 
                        oldQuantity, request.getQuantity());
            } else {
                // Si no existe, crear nuevo item
                cartItem = builderService.buildCartItem(request, cart, product);
                log.info("Producto {} no existe en carrito {}, creando nuevo item", 
                        request.getProductId(), cart.getCarritoId());
            }
            
            CartItem savedItem = cartItemRepository.save(cartItem);
            
            log.info("Producto {} procesado exitosamente en carrito {} (usuario: {} {})", 
                    request.getProductId(), cart.getCarritoId(),
                    request.getDocumentType(), request.getDocumentNumber());
            
            return cartItemMapper.toResponseDto(savedItem);
            
        } catch (CartException e) {
            throw e;
        } catch (com.ecommerce.davivienda.exception.product.ProductException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al agregar producto al carrito: {}", e.getMessage(), e);
            throw new CartException(ERROR_GENERIC, CODE_GENERIC_ERROR, e);
        }
    }

    @Override
    @Transactional
    public List<CartItemResponseDto> addItemsToCartBatch(CartItemBatchRequestDto request) {
        log.info("Agregando {} productos al carrito {} en lote (documento: {} {})", 
                request.getItems().size(), request.getCartId(),
                request.getDocumentType(), request.getDocumentNumber());
        
        try {
            Integer userRoleId = validationService.getUserRoleIdFromDocument(
                    request.getDocumentType(), request.getDocumentNumber());
            
            validationService.validateUserHasClientRole(userRoleId);
            
            Cart cart = validationService.validateOrCreateCart(request.getCartId(), userRoleId);
            List<CartItemResponseDto> addedItems = new java.util.ArrayList<>();
            
            for (var item : request.getItems()) {
                try {
                    Product product = validationService.validateProductExists(item.getProductId());
                    validationService.validateProductActive(product);
                    validationService.validateQuantity(item.getQuantity());
                    
                    boolean productExists = cartItemRepository.existsByCartAndProduct(
                            request.getCartId(), item.getProductId());
                    
                    if (productExists) {
                        log.warn("Producto {} ya existe en carrito {}, omitiendo", 
                                item.getProductId(), request.getCartId());
                        continue;
                    }
                    
                    CartItemRequestDto singleRequest = CartItemRequestDto.builder()
                            .cartId(request.getCartId())
                            .productId(item.getProductId())
                            .quantity(item.getQuantity())
                            .documentType(request.getDocumentType())
                            .documentNumber(request.getDocumentNumber())
                            .build();
                    
                    CartItem cartItem = builderService.buildCartItem(singleRequest, cart, product);
                    CartItem savedItem = cartItemRepository.save(cartItem);
                    addedItems.add(cartItemMapper.toResponseDto(savedItem));
                    
                    log.debug("Producto {} agregado al carrito {}", item.getProductId(), request.getCartId());
                    
                } catch (Exception e) {
                    log.error("Error al agregar producto {} en lote: {}", 
                            item.getProductId(), e.getMessage());
                }
            }
            
            log.info("{} de {} productos agregados exitosamente al carrito {} (usuario: {} {})", 
                    addedItems.size(), request.getItems().size(), request.getCartId(),
                    request.getDocumentType(), request.getDocumentNumber());
            
            return addedItems;
            
        } catch (CartException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al agregar productos en lote al carrito: {}", e.getMessage(), e);
            throw new CartException(ERROR_GENERIC, CODE_GENERIC_ERROR, e);
        }
    }

    @Override
    @Transactional
    public CartItemResponseDto updateItemQuantity(Integer itemId, Integer quantity) {
        log.info("Actualizando cantidad del item {} a {}", itemId, quantity);
        
        try {
            validationService.validateQuantity(quantity);
            
            CartItem cartItem = findCartItemById(itemId);
            builderService.updateQuantity(cartItem, quantity);
            CartItem updatedItem = cartItemRepository.save(cartItem);
            
            log.info("Cantidad del item {} actualizada exitosamente", itemId);
            
            return cartItemMapper.toResponseDto(updatedItem);
            
        } catch (CartException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al actualizar cantidad del item {}: {}", itemId, e.getMessage(), e);
            throw new CartException(ERROR_GENERIC, CODE_GENERIC_ERROR, e);
        }
    }

    @Override
    @Transactional
    public void removeItemFromCart(Integer itemId) {
        log.info("Eliminando item {} del carrito", itemId);
        
        try {
            CartItem cartItem = findCartItemById(itemId);
            cartItemRepository.delete(cartItem);
            
            log.info("Item {} eliminado exitosamente", itemId);
            
        } catch (CartException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al eliminar item {}: {}", itemId, e.getMessage(), e);
            throw new CartException(ERROR_GENERIC, CODE_GENERIC_ERROR, e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CartItemResponseDto> getCartItems(Integer cartId) {
        log.info("Obteniendo items del carrito {}", cartId);
        
        try {
            validationService.validateCartExists(cartId);
            
            List<CartItem> items = cartItemRepository.findByCartCarritoId(cartId);
            
            log.info("Se encontraron {} items en el carrito {}", items.size(), cartId);
            
            return cartItemMapper.toResponseDtoList(items);
            
        } catch (CartException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al obtener items del carrito {}: {}", cartId, e.getMessage(), e);
            throw new CartException(ERROR_GENERIC, CODE_GENERIC_ERROR, e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CartSummaryDto getCartSummary(Integer cartId) {
        log.info("Obteniendo resumen del carrito {}", cartId);
        
        try {
            validationService.validateCartExists(cartId);
            
            List<CartItem> items = cartItemRepository.findByCartCarritoId(cartId);
            
            log.info("Generando resumen del carrito {} con {} items", cartId, items.size());
            
            return cartItemMapper.toCartSummaryDto(cartId, items);
            
        } catch (CartException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al obtener resumen del carrito {}: {}", cartId, e.getMessage(), e);
            throw new CartException(ERROR_GENERIC, CODE_GENERIC_ERROR, e);
        }
    }

    @Override
    @Transactional
    public void clearCart(Integer cartId) {
        log.info("Limpiando carrito {}", cartId);
        
        try {
            validationService.validateCartExists(cartId);
            
            cartItemRepository.deleteByCartCarritoId(cartId);
            
            log.info("Carrito {} limpiado exitosamente", cartId);
            
        } catch (CartException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al limpiar carrito {}: {}", cartId, e.getMessage(), e);
            throw new CartException(ERROR_GENERIC, CODE_GENERIC_ERROR, e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CartItemResponseDto getCartItemById(Integer itemId) {
        log.info("Obteniendo item del carrito con ID: {}", itemId);
        
        try {
            CartItem cartItem = findCartItemById(itemId);
            
            return cartItemMapper.toResponseDto(cartItem);
            
        } catch (CartException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al obtener item {}: {}", itemId, e.getMessage(), e);
            throw new CartException(ERROR_GENERIC, CODE_GENERIC_ERROR, e);
        }
    }

    /**
     * Busca un CartItem por ID o lanza excepción.
     *
     * @param itemId ID del item
     * @return CartItem encontrado
     */
    private CartItem findCartItemById(Integer itemId) {
        return cartItemRepository.findById(itemId)
                .orElseThrow(() -> {
                    log.warn("Item del carrito no encontrado con ID: {}", itemId);
                    return new CartException(ERROR_CART_ITEM_NOT_FOUND, CODE_CART_ITEM_NOT_FOUND);
                });
    }
}

