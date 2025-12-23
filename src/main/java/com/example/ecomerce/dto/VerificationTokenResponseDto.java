package com.example.ecomerce.dto;

import com.example.ecomerce.enums.TokenType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class VerificationTokenResponseDto {
    private String token;
    private String email;
    private TokenType tokenType;
    private LocalDateTime expiryDate;
}
