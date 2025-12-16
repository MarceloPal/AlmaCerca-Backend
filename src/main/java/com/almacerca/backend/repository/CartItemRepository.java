package com.almacerca.backend.repository;

import com.almacerca.backend.model.CartItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends MongoRepository<CartItem, String> {

    //Ahora usa String (MongoDB usa String para IDs)
    List<CartItem> findByUserId(String userId);

    //Ahora usa String + String
    Optional<CartItem> findByUserIdAndProductId(String userId, String productId);

    List<CartItem> findByUserIdAndStatus(String userId, String status);
    Optional<CartItem> findByUserIdAndProductIdAndStatus(String userId, String productId, String status);
}
