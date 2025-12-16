package com.almacerca.backend.controller;

import com.almacerca.backend.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.almacerca.backend.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
public class ProductAdminController {

    @Autowired
    private ProductService productService; 

    @GetMapping
    public ResponseEntity<List<Product>> getAll(@RequestHeader("userId") String userId) {
    
        //return productService.findAllFromDefaultStore();
        System.out.println("Solicitud realizada por el admin: " + userId); // Log temporal
        return ResponseEntity.ok(productService.findAllFromDefaultStore());
    }

    @PostMapping
    public Product create(
        @RequestBody Product product) {
        return productService.create(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(
        @PathVariable String id,
        @RequestBody Product updated) {
        Product p = productService.update(id, updated);
        return ResponseEntity.ok(p);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
        @PathVariable String id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}