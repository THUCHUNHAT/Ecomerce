package com.example.ecomerce.dto;

import com.example.ecomerce.entity.Order;
import com.example.ecomerce.entity.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderItemRequestDto {
    private int productId ;
    private int quantity;
}
