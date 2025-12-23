package com.example.ecomerce.controller;

import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/favorite")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;


    @PostMapping("/{productId}")
    public ResponseEntity<ApiResponse> addProductToFavorites(@PathVariable int productId, Authentication authentication){
        return favoriteService.addProductToFavorites(productId,authentication);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllFavoriteProducts(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return favoriteService.getAllFavoriteProducts(authentication, pageable);
    }


    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse> removeProductFromFavorites (@PathVariable int productId,Authentication authentication){
        return favoriteService.removeProductFromFavorites(productId,authentication);
    }
}
