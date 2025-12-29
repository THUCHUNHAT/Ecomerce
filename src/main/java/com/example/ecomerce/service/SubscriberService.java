package com.example.ecomerce.service;

import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.dto.SubscriberRequestDto;
import org.springframework.http.ResponseEntity;

public interface SubscriberService {
  ResponseEntity<ApiResponse> Subscriber(SubscriberRequestDto subscriberRequestDto);
  ResponseEntity <ApiResponse> unSubscriber(String email);
}
