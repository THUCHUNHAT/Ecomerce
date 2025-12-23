package com.example.ecomerce.payment.dto;
import com.example.ecomerce.enums.PaymentMethod;
import lombok.Data;
@Data
public class ZalopayRequest {
        private int orderId;
        private PaymentMethod method = PaymentMethod.ZALOPAY;
}
