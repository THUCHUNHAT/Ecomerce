package com.example.ecomerce.dto;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
@Setter
@Getter
public class ArticleResponseDto {
    private int id;
    private String banner;
    private String title;
    private String articleContent;
    private LocalDateTime createAt;
}
