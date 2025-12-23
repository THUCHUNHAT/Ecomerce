package com.example.ecomerce.service;

import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.dto.OrderItemRequestDto;
import com.example.ecomerce.dto.OrderItemResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface OrderItemService {
    ResponseEntity<ApiResponse> getOderItemById(int id);
    ResponseEntity<ApiResponse> getAllOrderItems(Pageable pageable);
    ResponseEntity<ApiResponse> addOrderItem(OrderItemRequestDto orderItemRequestDto);
    ResponseEntity<ApiResponse> updateOrderItem(int id, OrderItemRequestDto orderItemRequest);
    ResponseEntity<ApiResponse> deleteOrderItem(int id);
    ResponseEntity<ApiResponse> searchOrderItem(String keyword, Pageable pageable);
}
