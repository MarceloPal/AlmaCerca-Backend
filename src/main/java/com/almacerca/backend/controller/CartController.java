package com.almacerca.backend.controller;

import com.almacerca.backend.dto.CartItemDto;
import com.almacerca.backend.exception.AccessDeniedException;
import com.almacerca.backend.exception.ResourceNotFoundException;
import com.almacerca.backend.model.CartItem;
import com.almacerca.backend.model.User;
import com.almacerca.backend.repository.UserRepository;
import com.almacerca.backend.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {
	private static final Logger logger = LoggerFactory.getLogger(CartController.class);

	@Autowired
	private CartService cartService;

	@Autowired
	private UserRepository userRepository;

	//userId es String en todos los métodos de MongoDB
	private User requireBuyer(String userId) {
		// Nota: userRepository.findById(String) es correcto
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));
		if (!"BUYER".equals(user.getRole())) {
			throw new AccessDeniedException("Buyer role required");
		}
		return user;
	}

	@GetMapping
	//userId viene en el RequestHeader como String
	public List<CartItemDto> getCart(@RequestHeader("userId") String userId) {
		requireBuyer(userId);
		return cartService.getCartForUser(userId);
	}


	// Variante JSON para clientes que envían body raw JSON
	@PostMapping(path = "/add", consumes = "application/json")
	public ResponseEntity<CartItem> addToCartJson(
			@RequestHeader("userId") String userId,
			@RequestBody AddToCartRequest body) {

		logger.debug("addToCart json: userId={}, productId={}, quantity={}", userId, body.getProductId(), body.getQuantity());
		requireBuyer(userId);
		CartItem ci = cartService.addToCart(userId, body.getProductId(), body.getQuantity());
		return ResponseEntity.status(HttpStatus.CREATED).body(ci);
	}

	// DTO simple para el body JSON
	public static class AddToCartRequest {
		private String productId;
		private Integer quantity;

		public String getProductId() { return productId; }
		public void setProductId(String productId) { this.productId = productId; }
		public Integer getQuantity() { return quantity; }
		public void setQuantity(Integer quantity) { this.quantity = quantity; }
	}

	@DeleteMapping("/items/{productId}")
	public ResponseEntity<Void> removeItem(
			//userId es String
			@RequestHeader("userId") String userId, 
			//productId en la URL es String
			@PathVariable("productId") String productId) {
		
		requireBuyer(userId);
		//Llamada al servicio con String IDs
		cartService.removeItem(userId, productId);
		return ResponseEntity.noContent().build();
	}

	// Obtener un item específico del carrito por productId
	@GetMapping("/items/{productId}")
	public ResponseEntity<CartItemDto> getItem(
			@RequestHeader("userId") String userId,
			@PathVariable("productId") String productId) {

		requireBuyer(userId);
		List<CartItemDto> items = cartService.getCartForUser(userId);
		CartItemDto match = items.stream()
				.filter(i -> productId.equals(i.getProductId()))
				.findFirst()
				.orElseThrow(() -> new ResourceNotFoundException("Cart item not found for productId=" + productId));
		return ResponseEntity.ok(match);
	}

	// Actualizar cantidad de un item del carrito
	@PutMapping(path = "/items/{productId}", consumes = "application/json")
	public ResponseEntity<CartItem> updateItemQuantity(
			@RequestHeader("userId") String userId,
			@PathVariable("productId") String productId,
			@RequestBody UpdateQuantityRequest body) {

		requireBuyer(userId);
		Integer qty = body.getQuantity();
		logger.debug("updateItemQuantity: userId={}, productId={}, quantity={}", userId, productId, qty);
		CartItem ci = cartService.addToCart(userId, productId, qty);
		return ResponseEntity.ok(ci);
	}

	public static class UpdateQuantityRequest {
		private Integer quantity;
		public Integer getQuantity() { return quantity; }
		public void setQuantity(Integer quantity) { this.quantity = quantity; }
	}
	
	// ▼▼▼ CORRECCIÓN: Devolvemos Void (sin cuerpo) para evitar error de JSON ▼▼▼
    @PostMapping("/buy")
    public ResponseEntity<Void> buyCart(@RequestHeader("userId") String userId) {
        requireBuyer(userId);
        cartService.buyCart(userId);
        return ResponseEntity.ok().build(); // .build() envía un 200 OK sin texto
    }
}