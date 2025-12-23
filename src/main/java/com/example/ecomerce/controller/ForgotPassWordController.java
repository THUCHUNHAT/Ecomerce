package com.example.ecomerce.controller;

import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.dto.ConFirmPassWordChangeCodeDto;
import com.example.ecomerce.dto.ForgotPassWordDto;
import com.example.ecomerce.dto.NewPasswordDto;
import com.example.ecomerce.service.ForgotPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/forgotpass")

public class ForgotPassWordController  {
    @Autowired
    private ForgotPasswordService forgotPasswordService;

    @PostMapping
    public ResponseEntity<ApiResponse> forgotPassword(@RequestBody ForgotPassWordDto forgotPassWordDto){
        return forgotPasswordService.forgotPassword(forgotPassWordDto);
    }

    @PostMapping ("/conFirmPassWordChangeCode")
    public ResponseEntity<ApiResponse> conFirmPassWordChangeCode(@RequestBody ConFirmPassWordChangeCodeDto conFirmPassWordChangeCodeDto){
        return forgotPasswordService.conFirmPassWordChangeCode(conFirmPassWordChangeCodeDto);
    }

    @PostMapping ("/NewPassword")
    public ResponseEntity<ApiResponse> NewPassword(@RequestBody NewPasswordDto newPasswordDto){
        return  forgotPasswordService.NewPassword(newPasswordDto);
    }
}
