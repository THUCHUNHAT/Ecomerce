package com.example.ecomerce.service.Impl;
import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.dto.CategoryRequestDto;
import com.example.ecomerce.dto.CategoryResponseDto;
import com.example.ecomerce.dto.PagedResponse;
import com.example.ecomerce.entity.Category;
import com.example.ecomerce.repository.CategoryRepository;
import com.example.ecomerce.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ResponseEntity<ApiResponse> getAllCategories(Pageable pageable) {
        Page<Category> categories = categoryRepository.findAll(pageable);

        List<CategoryResponseDto> categoryDtos = categories.getContent().stream()
                .map(category -> modelMapper.map(category, CategoryResponseDto.class))
                .toList();

        PagedResponse<CategoryResponseDto> pagedResponse = new PagedResponse<>(categoryDtos, categories);

        return ResponseEntity.ok(new ApiResponse("List Category", pagedResponse, "Success"));
    }


    @Override
    public ResponseEntity<ApiResponse> addCategory (CategoryRequestDto categoryRequestDto){
        Category category = new Category();
        category.setName(categoryRequestDto.getName());
        category.setCode(categoryRequestDto.getCode());
        categoryRepository.save(category);
        return ResponseEntity.ok(new ApiResponse("Category added successfully", null, "Success"));
    }

    @Override
    public ResponseEntity<ApiResponse> updateCategory (int id, CategoryRequestDto categoryRequestDto){
        Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
        category.setName(categoryRequestDto.getName());
        category.setCode(categoryRequestDto.getCode());
        categoryRepository.save(category);
        return ResponseEntity.ok(new ApiResponse("Category updated successfully", null, "Success"));

    }

    @Override
    public ResponseEntity<ApiResponse> deleteCategory (int id) {
        return null;
    }

}
