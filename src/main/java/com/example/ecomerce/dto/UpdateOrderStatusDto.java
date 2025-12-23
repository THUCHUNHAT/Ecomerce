package com.example.ecomerce.dto;
import com.example.ecomerce.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class UpdateOrderStatusDto {
    private OrderStatus orderStatus;
}
