package com.example.ecomerce.dto;
import com.example.ecomerce.enums.PaymentMethod;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class OderResponseDto {
    private int id;
    private String orderCode;
    private LocalDateTime orderDate;
    private String userName;
    private String email;
    private String phoneNumber;
    private String shippingAddress;
    private PaymentMethod paymentMethod;
    private double totalAmount;
    private String status;
    private List<OrderItemResponseDto> items;
}
