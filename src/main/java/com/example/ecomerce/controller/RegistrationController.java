package com.example.ecomerce.controller;

import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.dto.UserRequestDto;
import com.example.ecomerce.service.RegisterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping ("/registration")
public class RegistrationController {

    @Autowired
    private RegisterService registerService;

    @PostMapping
    public ResponseEntity<ApiResponse> registerUser( @RequestBody @Valid UserRequestDto userRequestDto){
        return registerService.registerUser(userRequestDto);
    }

    @PostMapping("/tokenConfirmation")
    public ResponseEntity<ApiResponse> tokenConfirmation(@RequestParam String token) {
        return registerService.tokenConfirmation(token);
    }

    @PostMapping ("/resendcode")
    public ResponseEntity<ApiResponse> resendcode (@RequestBody String email){
        return registerService.resendCode(email);
    }


}
