package com.example.ecomerce.service;

import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.dto.UserRequestDto;
import org.springframework.http.ResponseEntity;

public interface RegisterService {
    ResponseEntity<ApiResponse> registerUser(UserRequestDto userRequestDto);
    ResponseEntity<ApiResponse> tokenConfirmation(String token);
    ResponseEntity<ApiResponse> resendCode (String email);
}
