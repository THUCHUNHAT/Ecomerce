package com.example.ecomerce.controller;

import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.service.ViewedRecentlyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/viewedrecently")

public class ViewedRecentlyController {

    @Autowired
    private ViewedRecentlyService viewedRecentlyService;

    @PostMapping("/{productId}")
    public ResponseEntity<ApiResponse> addToViewedProducts(@PathVariable int productId, Authentication authentication){
        return viewedRecentlyService.addToViewedProducts(productId,authentication);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> showProductsUsersHaveViewed(Authentication authentication){
        return viewedRecentlyService.showProductsUsersHaveViewed(authentication);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> DeleteRecentlyViewedProducts ( int id){
        return viewedRecentlyService.DeleteRecentlyViewedProducts(id);
    }
}
