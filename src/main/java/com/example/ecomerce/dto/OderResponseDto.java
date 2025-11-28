package com.example.ecomerce.dto;
import com.example.ecomerce.entity.User;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
@Setter
@Getter
public class OderResponseDto {
    private int id;
    private String orderCode;
    private LocalDateTime orderDate;
    private String userName;
    private String phoneNumber;
    private String shippingAddress;
    private String paymentMethod;
    private double totalAmount;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
