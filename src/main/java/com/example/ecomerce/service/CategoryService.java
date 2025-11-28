package com.example.ecomerce.service;

import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.dto.CategoryRequestDto;
import org.springframework.http.ResponseEntity;

import org.springframework.data.domain.Pageable;

public interface CategoryService {
    ResponseEntity<ApiResponse> getAllCategories(Pageable pageable);
    ResponseEntity<ApiResponse> addCategory(CategoryRequestDto categoryRequestDto);
    ResponseEntity<ApiResponse> updateCategory(int id, CategoryRequestDto categoryRequestDto);
    ResponseEntity<ApiResponse> deleteCategory(int id);
}
