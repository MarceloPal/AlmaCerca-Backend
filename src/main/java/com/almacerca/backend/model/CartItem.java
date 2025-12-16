package com.almacerca.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Document(collection = "cart_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

	@Id
	private String id; // ID de MongoDB (String)

	private String userId; // Referencia al ID de User (String)

	private String productId; // Referencia al ID de Product (String)

	private Integer quantity;
	
	private String status = "ACTIVE"; 

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}