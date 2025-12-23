package com.example.ecomerce.service;

import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.dto.QuantityRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface CartService {
    ResponseEntity<ApiResponse> addProductsToCart(int productId, Authentication authentication);
    ResponseEntity<ApiResponse> removeProductFromCart(int productId,Authentication authentication);
    ResponseEntity<ApiResponse> seePriceInformation(Authentication authentication);
    ResponseEntity<ApiResponse> setProductQuantity(Authentication authentication, int productId, QuantityRequest quantityRequest);
}
