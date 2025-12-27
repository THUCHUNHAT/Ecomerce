package com.example.ecomerce.dto;

import com.example.ecomerce.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class MessageResponseDto {
    private int id;
    private String username;
    private int setConversationId;
    private String content;
    private LocalDateTime createAt;
}
