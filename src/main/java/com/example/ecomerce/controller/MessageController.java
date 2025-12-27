package com.example.ecomerce.controller;

import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.dto.MessageRequestDto;
import com.example.ecomerce.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping
    public ResponseEntity<ApiResponse> sendMessage (@RequestBody MessageRequestDto messageRequestDto, Authentication authentication){
        return messageService.sendMessage(messageRequestDto,authentication);
    }

    @GetMapping ("/{conversationId}")
    public ResponseEntity<ApiResponse> getMessage (Authentication authentication,@PathVariable int conversationId){
        return messageService.getMessage(authentication, conversationId);
    }
}
