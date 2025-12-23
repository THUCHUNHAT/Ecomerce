package com.example.ecomerce.dto;
import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class MessageRequestDto {
    private int conversationId;
    private String content;
}
