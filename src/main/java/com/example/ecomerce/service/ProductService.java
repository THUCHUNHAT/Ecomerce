package com.example.ecomerce.service;

import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.dto.ProductRequestDto;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ProductService {
   ResponseEntity<ApiResponse>getAllProduct(Pageable pageable);
   ResponseEntity<ApiResponse> addProduct(ProductRequestDto productRequestDto);
   ResponseEntity<ApiResponse> updateProduct(int id, ProductRequestDto productRequestDto);
    ResponseEntity<ApiResponse> deleteProduct(int id);
}
