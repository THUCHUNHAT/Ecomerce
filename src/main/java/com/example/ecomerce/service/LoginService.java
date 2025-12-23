package com.example.ecomerce.service;
import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.dto.LoginDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.security.Principal;

public interface LoginService {
    ResponseEntity<ApiResponse> login (LoginDto loginDto);
    Object user(Principal principal);

}
