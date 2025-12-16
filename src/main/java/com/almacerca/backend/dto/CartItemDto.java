package com.almacerca.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto {

    private String id;        //String, no Long
    private String productId; //String, no Long
    private Integer quantity;
}
