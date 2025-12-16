package com.almacerca.backend.repository;

import com.almacerca.backend.model.Product;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findByStoreId(String storeId);
    List<Product> findByCategoryId(String categoryId);
}
