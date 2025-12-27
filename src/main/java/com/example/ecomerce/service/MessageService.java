package com.example.ecomerce.service;

import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.dto.MessageRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface MessageService {
    ResponseEntity<ApiResponse>sendMessage (MessageRequestDto messageRequestDto, Authentication authentication);

    ResponseEntity<ApiResponse> getMessage(Authentication authentication,int conversationId);
}
