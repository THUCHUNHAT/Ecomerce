package com.example.ecomerce.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserResponseDto {
    private String username;
    private String fullName;
    private String email;
    private String password;
    private String phone;
}
