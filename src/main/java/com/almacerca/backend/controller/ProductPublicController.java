package com.almacerca.backend.controller;

import com.almacerca.backend.model.Product;
import com.almacerca.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductPublicController {

    private static final Logger logger = LoggerFactory.getLogger(ProductPublicController.class);

    @Autowired
    private ProductService productService;

    // ✅ ESTE ES EL QUE USA LA APP DEL CLIENTE
    @GetMapping
    public List<Product> getPublicProducts() {
        return productService.findAllFromDefaultStore();
    }

    // Endpoint público para listar productos por categoría (acepta categoryId como String)
    @GetMapping("/category/{categoryId}")
    public List<Product> getProductsByCategory(@PathVariable("categoryId") String categoryId) {
        logger.debug("getProductsByCategory called with categoryId={}", categoryId);
        List<Product> results = productService.findByCategoryId(categoryId);
        logger.debug("found {} products for categoryId={}", results.size(), categoryId);
        return results;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductDetail(@PathVariable String id) {
        // Reutilizamos el método findById de tu servicio (que ya maneja la excepción si no existe)
        Product product = productService.findById(id);
        return ResponseEntity.ok(product);
    }
}
