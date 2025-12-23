package com.example.ecomerce.service;

import com.example.ecomerce.Response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface ViewedRecentlyService {
    ResponseEntity<ApiResponse> addToViewedProducts(int productId, Authentication authentication);
    ResponseEntity<ApiResponse> showProductsUsersHaveViewed(Authentication authentication);
    ResponseEntity<ApiResponse> DeleteRecentlyViewedProducts (int id);
}
