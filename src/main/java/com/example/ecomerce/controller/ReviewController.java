package com.example.ecomerce.controller;

import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.dto.ReviewDto;
import com.example.ecomerce.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/review")

public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @PostMapping("/{productId}")
    public ResponseEntity<ApiResponse> review(
            @RequestBody ReviewDto reviewDto,
            Authentication authentication,
            @PathVariable int productId) {

        return reviewService.review(reviewDto, authentication, productId);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse> getProductReview(
            @PathVariable int productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return reviewService.getProductReview(productId, pageable);
    }

}
