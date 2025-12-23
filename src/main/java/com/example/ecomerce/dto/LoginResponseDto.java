package com.example.ecomerce.dto;

import com.example.ecomerce.enums.UserRole;
import com.example.ecomerce.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {
    private String token;
    private String username;
    private UserRole role;
}
