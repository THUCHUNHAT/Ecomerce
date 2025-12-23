package com.example.ecomerce.dto;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderItemResponseDto {
    private int productId;
    private String name;
    private double unitPrice;
    private int quantity;
    private double totalPrice;
}
