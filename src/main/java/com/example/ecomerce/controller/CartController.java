package com.example.ecomerce.controller;

import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.dto.QuantityRequest;
import com.example.ecomerce.service.CartService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/{productId}")
    public ResponseEntity<ApiResponse> addProductsToCart (@PathVariable int productId, Authentication authentication){
        return cartService.addProductsToCart(productId,authentication);

    }

    @Transactional
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse> removeProductFromCart (@PathVariable int productId,Authentication authentication){
        return  cartService.removeProductFromCart(productId,authentication);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> seePriceInformation (Authentication authentication){
        return cartService.seePriceInformation(authentication);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse> setProductQuantity (@PathVariable int productId, @RequestBody QuantityRequest quantityRequest, Authentication authentication){
        return cartService.setProductQuantity(authentication,productId,quantityRequest);
    }


}
