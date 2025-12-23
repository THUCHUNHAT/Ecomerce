package com.example.ecomerce.controller;

import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.dto.CategoryRequestDto;
import com.example.ecomerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @GetMapping
    public ResponseEntity<ApiResponse> getAllCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return categoryService.getAllCategories(pageable);
    }

    @PostMapping
    public ResponseEntity<ApiResponse> addCategory(@RequestBody CategoryRequestDto categoryRequestDto) {
        return categoryService.addCategory(categoryRequestDto);
    }

    @PutMapping ("/{id}")
    public ResponseEntity<ApiResponse> updateCategory(@PathVariable  int id, @RequestBody CategoryRequestDto categoryRequestDto) {
        return categoryService.updateCategory(id, categoryRequestDto);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> searchCategories(@RequestParam String keyword, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return categoryService.searchCategories(keyword, pageable);
    }

    @GetMapping("/{id}" )
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable int id){
        return categoryService.getCategoryById(id);
    }
}
