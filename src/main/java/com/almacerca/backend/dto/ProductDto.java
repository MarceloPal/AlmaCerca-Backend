package com.almacerca.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private String id;   //String, no Long
    private String name;
    private String description;
    private Double price;
    private String storeId;
    private String categoryId;
    private Integer stock; // <- ¡REQUERIDO!
    private String imageUrl; // <- ¡REQUERIDO!
}
