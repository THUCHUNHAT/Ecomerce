package com.example.ecomerce.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderItemRequestDto {
    private int productId;
    private int quantity;
}
