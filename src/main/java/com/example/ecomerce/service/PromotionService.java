package com.example.ecomerce.service;

import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.dto.PromotionRequestDto;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface PromotionService {
    ResponseEntity<ApiResponse>addPromotion (PromotionRequestDto promotionRequestDto);
    ResponseEntity<ApiResponse> removePromotion (int id);
    ResponseEntity<ApiResponse> updatePromotion(int id, PromotionRequestDto promotionRequestDto);
    ResponseEntity<ApiResponse> getPromotionId(int id);
    ResponseEntity<ApiResponse> getAllPromotion(Pageable pageable);
}
