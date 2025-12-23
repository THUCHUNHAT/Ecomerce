package com.example.ecomerce.payment.controller;

import com.example.ecomerce.payment.dto.ZalopayRequest;
import com.example.ecomerce.payment.service.ZalopayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/zalopay")
public class ZalopayController {
    @Autowired
    private ZalopayService zalopayService;

    @PostMapping
    public ResponseEntity<String> createPayment(@RequestBody ZalopayRequest zalopayRequest) {
        try {
            String response = zalopayService.createOrder(zalopayRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating payment: " + e.getMessage());
        }
    }

    @GetMapping("/order-status/{appTransId}")
    public ResponseEntity<String> getOrderStatus(@PathVariable String appTransId) {
        String response = zalopayService.getOrderStatus(appTransId);
        return ResponseEntity.ok(response);
    }
}
