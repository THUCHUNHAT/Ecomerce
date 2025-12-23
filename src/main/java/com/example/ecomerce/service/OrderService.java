package com.example.ecomerce.service;

import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.dto.*;
import com.example.ecomerce.enums.OrderStatus;
import com.example.ecomerce.enums.PaymentMethod;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;


public interface OrderService {
    ResponseEntity<ApiResponse>getAllOrder(Pageable pageable);
    ResponseEntity<ApiResponse>getOrderDetails(int id);
//    ResponseEntity<ApiResponse> searchOrder(String keyword, Pageable pageable);
    ResponseEntity<ApiResponse> filter(OrderFilter orderFilter, Pageable pageable);
    ResponseEntity<ApiResponse> deleteOrder(int id);
    ResponseEntity<ApiResponse> getOrderById(int id);
    ResponseEntity<ApiResponse> createOrder(OderRequestDto request, Authentication authentication);
    ResponseEntity<ApiResponse> updateOrderStatus (OrderStatus orderStatus, int id);
    ResponseEntity<ApiResponse> UpdateThePaymentMethodForTheOrder (int id,PaymentMethod paymentMethod);
}
