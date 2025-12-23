package com.example.ecomerce.service;

import com.example.ecomerce.Response.ApiResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface VerificationTokenService {
    ResponseEntity<ApiResponse> getAllTokens(Pageable pageable);
    ResponseEntity<ApiResponse> deleteToken(int id);
}
