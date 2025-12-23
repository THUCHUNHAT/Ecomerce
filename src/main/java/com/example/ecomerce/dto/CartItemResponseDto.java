package com.example.ecomerce.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CartItemResponseDto {
    private ProductResponseDto product;
    private BigDecimal quantity;
    private BigDecimal price;

    public CartItemResponseDto(ProductResponseDto product, BigDecimal quantity, BigDecimal price) {
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }

}
