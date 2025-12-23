package com.example.ecomerce.service;

import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.dto.ConFirmPassWordChangeCodeDto;
import com.example.ecomerce.dto.ForgotPassWordDto;
import com.example.ecomerce.dto.NewPasswordDto;
import org.springframework.http.ResponseEntity;

public interface ForgotPasswordService {
    ResponseEntity<ApiResponse> forgotPassword(ForgotPassWordDto forgotPassWordDto);
    ResponseEntity<ApiResponse> conFirmPassWordChangeCode(ConFirmPassWordChangeCodeDto conFirmPassWordChangeCodeDto);
    ResponseEntity<ApiResponse>NewPassword(NewPasswordDto newPasswordDto);
}
