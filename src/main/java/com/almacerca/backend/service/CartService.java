package com.almacerca.backend.service;

import com.almacerca.backend.dto.CartItemDto;
import com.almacerca.backend.exception.ResourceNotFoundException;
import com.almacerca.backend.model.CartItem;
import com.almacerca.backend.model.Product;
import com.almacerca.backend.model.User;
import com.almacerca.backend.repository.CartItemRepository;
import com.almacerca.backend.repository.ProductRepository;
import com.almacerca.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    private static final Logger logger = LoggerFactory.getLogger(CartService.class);

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    // userId es String
    public List<CartItemDto> getCartForUser(String userId) {
        logger.info("getCartForUser called: userId={}", userId);
        ensureUserExists(userId);
        
        // CAMBIO: Solo buscamos items ACTIVOS (ignoramos los comprados)
        List<CartItem> items = cartItemRepository.findByUserIdAndStatus(userId, "ACTIVE");
        
        logger.info("Found {} active cart items for userId={}", items.size(), userId);
        return items.stream().map(this::toDto).collect(Collectors.toList());
    }

    // userId y productId son String
    public CartItem addToCart(String userId, String productId, Integer quantity) {
        logger.info("addToCart called: userId={}, productId={}, quantity={}", userId, productId, quantity);
        
        ensureUserExists(userId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        logger.info("Product found: {}", product.getName());

        // CAMBIO: Buscamos si existe SOLO en el carrito ACTIVO
        CartItem existing = cartItemRepository
                .findByUserIdAndProductIdAndStatus(userId, productId, "ACTIVE")
                .orElse(null);

        if (existing != null) {
            logger.info("Updating existing active cart item. Old quantity: {}, adding: {}", existing.getQuantity(), quantity);
            existing.setQuantity(existing.getQuantity() + (quantity != null ? quantity : 1));
            CartItem saved = cartItemRepository.save(existing);
            logger.info("Cart item updated. New quantity: {}, saved ID: {}", saved.getQuantity(), saved.getId());
            return saved;
        }

        logger.info("Creating new cart item");
        CartItem ci = new CartItem();
        ci.setUserId(userId);
        ci.setProductId(productId);
        ci.setQuantity(quantity != null ? quantity : 1);
        
        // CAMBIO: Aseguramos que nazca como ACTIVO
        ci.setStatus("ACTIVE"); 

        CartItem saved = cartItemRepository.save(ci);
        logger.info("Cart item created with ID: {}, quantity: {}", saved.getId(), saved.getQuantity());
        return saved;
    }

    // MÉTODO NUEVO: Realizar la compra (Borrado lógico)
    public void buyCart(String userId) {
        logger.info("buyCart called for userId={}", userId);
        ensureUserExists(userId);

        List<CartItem> items = cartItemRepository.findByUserIdAndStatus(userId, "ACTIVE");
        
        for (CartItem item : items) {
            item.setStatus("PURCHASED");
            cartItemRepository.save(item);
        }
        logger.info("Cart purchased successfully. {} items moved to history.", items.size());
    }

    // userId y productId son String
    public void removeItem(String userId, String productId) {
        ensureUserExists(userId);

        // CAMBIO: Solo permitimos borrar items ACTIVOS
        CartItem existing = cartItemRepository
                .findByUserIdAndProductIdAndStatus(userId, productId, "ACTIVE")
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found or already purchased"));

        cartItemRepository.delete(existing);
    }

    // userId es String
    private User ensureUserExists(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    // DTO CORRECTO CON STRING
    private CartItemDto toDto(CartItem ci) {
        return new CartItemDto(
                ci.getId(),        // ✅ String
                ci.getProductId(), // ✅ String
                ci.getQuantity()
        );
    }
}