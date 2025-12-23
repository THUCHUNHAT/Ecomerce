package com.example.ecomerce.payment.controller;

import com.example.ecomerce.payment.dto.MomoRequest;
import com.example.ecomerce.payment.service.MomoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/api/momo")
public class MomoController {
    @Autowired
    private MomoService momoService;

    @PostMapping
    public String testPayment(@RequestBody MomoRequest momoRequest) {
      return momoService.createPaymentRequest(momoRequest);
    }

    @GetMapping("/order-status/{orderId}")
    public String checkPaymentStatus(@PathVariable String orderId) {
        String response = momoService.checkPaymentStatus(orderId);
        return response;
    }

}
