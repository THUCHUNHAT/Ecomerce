package com.example.ecomerce.payment.controller;

import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.payment.dto.VnpayRequest;
import com.example.ecomerce.payment.service.VnpayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/api/vnpay")
public class VnpayController {

    @Autowired
    private VnpayService vnpayService;

    @PostMapping
    public ResponseEntity<ApiResponse> createPayment (@RequestBody VnpayRequest paymentRequest){
        return vnpayService.createPayment(paymentRequest);
    }

    @GetMapping("/return")
    public ResponseEntity<ApiResponse> vnpayReturn(
            @RequestParam("vnp_ResponseCode") String responseCode,
            @RequestParam("vnp_TxnRef") String txnRef
    ) {
        return vnpayService.handlePaymentReturn(responseCode, txnRef);
    }
}
