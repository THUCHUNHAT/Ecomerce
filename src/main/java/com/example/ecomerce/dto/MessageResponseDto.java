package com.example.ecomerce.dto;

import com.example.ecomerce.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class MessageResponseDto {
    private int id;
    private User user;
    private String content;
    private LocalDateTime createAt;
}
