package com.example.ecomerce.dto;
import com.example.ecomerce.enums.PaymentMethod;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter

public class OderRequestDto {
    private String orderCode;
    private LocalDateTime orderDate;
    private String shippingAddress;
    private PaymentMethod paymentMethod;
    private BigDecimal totalAmount;
    private String status;
    private String couponCode;
    private List<OrderItemRequestDto> items;
}
