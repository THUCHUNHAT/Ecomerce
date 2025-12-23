package com.example.ecomerce.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderFilter {
    private String status;
    private String paymentMethod;
}
