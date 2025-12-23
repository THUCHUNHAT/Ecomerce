package com.example.ecomerce.service;

import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.dto.ArticleRequestDto;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ArticleService {
    ResponseEntity<ApiResponse> getAllArticle (Pageable pageable);
    ResponseEntity<ApiResponse> addArticle (ArticleRequestDto articleRequestDto);
    ResponseEntity<ApiResponse> updateArticle (ArticleRequestDto articleRequestDto, int id);
    ResponseEntity<ApiResponse> deleteArticle (int id);
    ResponseEntity<ApiResponse> getArticleById (int id);
    ResponseEntity<ApiResponse> getNewArticle();
}
