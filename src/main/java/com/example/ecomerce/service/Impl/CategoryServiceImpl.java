package com.example.ecomerce.service.Impl;
import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.dto.CategoryRequestDto;
import com.example.ecomerce.dto.CategoryResponseDto;
import com.example.ecomerce.dto.PagedResponse;
import com.example.ecomerce.entity.Category;
import com.example.ecomerce.enums.Error;
import com.example.ecomerce.exception.ErrorCategory;
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

        return ResponseEntity.ok(new ApiResponse("List Category", pagedResponse, "SUCESS"));
    }


    @Override
    public ResponseEntity<ApiResponse> addCategory (CategoryRequestDto categoryRequestDto){
        Category category = new Category();
        category.setName(categoryRequestDto.getName());
        categoryRepository.save(category);
        return ResponseEntity.ok(new ApiResponse("Category added successfully", null, "SUCCESS"));
    }

    @Override
    public ResponseEntity<ApiResponse> updateCategory (int id, CategoryRequestDto categoryRequestDto){
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null){
            return ResponseEntity.badRequest().body(new ApiResponse("PRODUCT CATEGORY UNDEFINED",null, ErrorCategory.PRODUCT_CATEGORY_UNDEFINED));
        }
        category.setName(categoryRequestDto.getName());
        categoryRepository.save(category);
        return ResponseEntity.ok(new ApiResponse("Category updated successfully", null,"SUCCESS"));

    }

    @Override
    public ResponseEntity<ApiResponse> searchCategories(String keyword, Pageable pageable) {
        Page<Category> categories = categoryRepository.searchCategories(keyword, pageable);
        List<CategoryResponseDto> categoryDtos = categories.getContent().stream()
                .map(category-> modelMapper.map(category, CategoryResponseDto.class))
                .toList();
        PagedResponse<CategoryResponseDto> pagedResponse = new PagedResponse<>(categoryDtos, categories);
        return ResponseEntity.ok(new ApiResponse("Search category", pagedResponse, "SUCCESS"));
    }

    @Override
    public ResponseEntity<ApiResponse> getCategoryById(int id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
        return ResponseEntity.ok(new ApiResponse("Category found", modelMapper.map(category, CategoryResponseDto.class),"SUCCESS"));
    }


    @Override
    public ResponseEntity<ApiResponse> deleteCategory (int id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null){
            return  ResponseEntity.badRequest().body(new ApiResponse("The shopping cart does not exist.",null,ErrorCategory.PRODUCT_CATEGORY_UNDEFINED));
        }
        categoryRepository.deleteById(category.getId());
        return ResponseEntity.ok(new ApiResponse("Category deletion successful",null, "SUCCESS"));
    }

}
