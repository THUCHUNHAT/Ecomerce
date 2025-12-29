package com.example.ecomerce.controller;

import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.dto.SubscriberRequestDto;
import com.example.ecomerce.service.SubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/subscribers")
public class SubscriberController {
    @Autowired
    private SubscriberService subscriberService;

    @PostMapping
    public ResponseEntity<ApiResponse>Subscriber ( @RequestBody SubscriberRequestDto subscriberRequestDto){
        return subscriberService.Subscriber(subscriberRequestDto);
    }

    @DeleteMapping ("/{email}")
    public ResponseEntity<ApiResponse> unSubscriber (@PathVariable String email) {
        return subscriberService.unSubscriber(email);
    }
}
