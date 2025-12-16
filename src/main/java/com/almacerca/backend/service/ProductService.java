package com.almacerca.backend.service;

import com.almacerca.backend.exception.ResourceNotFoundException;
import com.almacerca.backend.model.Product;
import com.almacerca.backend.repository.ProductRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.almacerca.backend.config.StoreConstants;

import java.util.List;

@Service
public class ProductService {

	private static final String DEFAULT_STORE_ID = "TIENDA_ADMIN_001";

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Product> findAllFromDefaultStore() {
        return productRepository.findByStoreId(StoreConstants.DEFAULT_STORE_ID);
    }

    //id ahora es String
    public Product findById(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    public Product create(Product product) {
        //Fuerza la tienda por defecto
        product.setStoreId(DEFAULT_STORE_ID);
        return productRepository.save(product);
    }


    //id ahora es String
    public Product update(String id, Product updated) {
        Product p = findById(id);
        p.setName(updated.getName());
        p.setDescription(updated.getDescription());
        p.setPrice(updated.getPrice());
        p.setStock(updated.getStock());
        p.setImageUrl(updated.getImageUrl());
        p.setCategoryId(updated.getCategoryId());
        return productRepository.save(p);
    }

    // Buscar productos por categoría. Soporta categoryId almacenado como String o como número en MongoDB.
    public List<Product> findByCategoryId(String categoryId) {
        // Intentamos una consulta flexible que acepte tanto String como Number en el campo categoryId.
        Query q = new Query();
        try {
            // si categoryId es numérico, añadimos ambas condiciones
            Integer asInt = Integer.valueOf(categoryId);
            q.addCriteria(new Criteria().orOperator(
                    Criteria.where("categoryId").is(categoryId),
                    Criteria.where("categoryId").is(asInt)
            ));
        } catch (NumberFormatException ex) {
            // no es numérico, buscamos por string
            q.addCriteria(Criteria.where("categoryId").is(categoryId));
        }
        return mongoTemplate.find(q, Product.class);
    }

    //id ahora es String
    public void delete(String id) {
        Product p = findById(id);
        productRepository.delete(p);
    }
}
