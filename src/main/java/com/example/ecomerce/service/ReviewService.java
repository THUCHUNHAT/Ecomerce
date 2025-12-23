package com.example.ecomerce.service;

import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.dto.ReviewDto;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface ReviewService {
    ResponseEntity<ApiResponse>review(ReviewDto reviewDto, Authentication authentication,int productId);
    ResponseEntity<ApiResponse>getProductReview(int productId, Pageable pageable);
}
