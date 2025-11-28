package com.example.ecomerce.dto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter

public class OderRequestDto {
    private String orderCode;
    private LocalDateTime orderDate;
    private int userId;
    private String shippingAddress;
    private String paymentMethod;
    private double totalAmount;
    private String status;
    private List<OrderItemRequestDto> items;
}
