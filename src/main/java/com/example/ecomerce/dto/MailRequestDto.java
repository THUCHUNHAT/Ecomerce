package com.example.ecomerce.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailRequestDto {
    private String to;
    private String subject;
    private String content;
}
