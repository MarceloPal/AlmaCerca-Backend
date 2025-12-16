package com.almacerca.backend.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Document(collection = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

	@Id
	private String id; // ID de MongoDB (String)
	private String description;
	private String name;
	private Double price;
    private String storeId;
	private String categoryId;
    private Integer stock;
    private String imageUrl;
}