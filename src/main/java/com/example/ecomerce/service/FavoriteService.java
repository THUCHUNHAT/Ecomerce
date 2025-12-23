package com.example.ecomerce.service;

import com.example.ecomerce.Response.ApiResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface FavoriteService {
    ResponseEntity<ApiResponse> addProductToFavorites(int productId, Authentication authentication);
    ResponseEntity<ApiResponse> getAllFavoriteProducts(Authentication authentication, Pageable pageable);
    ResponseEntity<ApiResponse> removeProductFromFavorites (int productId ,Authentication authentication);
}
