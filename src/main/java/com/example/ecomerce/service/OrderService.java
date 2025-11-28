package com.example.ecomerce.service;

import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.dto.OderRequestDto;
import com.example.ecomerce.dto.UpdateStatusOrderDto;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;


public interface OrderService {
    ResponseEntity<ApiResponse>getAllOrder(Pageable pageable);
    ResponseEntity<ApiResponse>getOrderDetails(int id);
    ResponseEntity<ApiResponse> addOrder(OderRequestDto oderRequestDto);
    ResponseEntity<ApiResponse> updateOrder(int id, UpdateStatusOrderDto updateStatusOrderDto);

    ResponseEntity<ApiResponse> deleteOrder(int id);
}
