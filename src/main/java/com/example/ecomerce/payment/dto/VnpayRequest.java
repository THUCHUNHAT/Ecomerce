package com.example.ecomerce.payment.dto;

import com.example.ecomerce.enums.PaymentMethod;
import lombok.Data;

@Data
public class VnpayRequest {
    private int orderId;
    private PaymentMethod method = PaymentMethod.VNPAY;

}
