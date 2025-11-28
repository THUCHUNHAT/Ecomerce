package com.example.ecomerce.service.Impl;
import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.dto.PagedResponse;
import com.example.ecomerce.dto.ProductRequestDto;
import com.example.ecomerce.dto.ProductResponseDto;
import com.example.ecomerce.entity.Category;
import com.example.ecomerce.entity.Product;
import com.example.ecomerce.repository.CategoryRepository;
import com.example.ecomerce.repository.ProductRepository;
import com.example.ecomerce.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public ResponseEntity<ApiResponse> getAllProduct(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);
        List<ProductResponseDto> productDtos = products.getContent().stream()
                .map(product -> modelMapper.map(product, ProductResponseDto.class))
                .toList();
        PagedResponse<ProductResponseDto> pagedResponse = new PagedResponse<>(productDtos, products);
        return ResponseEntity.ok(new ApiResponse("List Product", pagedResponse, "Success"));


    }

    @Override
    public ResponseEntity<ApiResponse> addProduct(ProductRequestDto dto) {
        Category category = categoryRepository.findById(dto.getCategoryId()).orElseThrow(() -> new RuntimeException("Category not found"));
        Product product = new Product();
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setBrand(dto.getBrand());
        product.setImageUrl(dto.getImageUrl());
        product.setCategory(category);
        Product savedProduct = productRepository.save(product);
        ProductResponseDto responseDto = modelMapper.map(savedProduct, ProductResponseDto.class);
        return ResponseEntity.ok(
                new ApiResponse("Product added successfully", responseDto, "Success")
        );
    }


    @Override
    public ResponseEntity<ApiResponse> updateProduct(int id, ProductRequestDto dto) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        Category category = categoryRepository.findById(dto.getCategoryId()).orElseThrow(() -> new RuntimeException("Category not found"));
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setBrand(dto.getBrand());
        product.setImageUrl(dto.getImageUrl());
        product.setCategory(category);
        Product savedProduct = productRepository.save(product);
        ProductResponseDto responseDto = modelMapper.map(savedProduct, ProductResponseDto.class);
        return ResponseEntity.ok(new ApiResponse("Product updated successfully", responseDto, "Success"));
    }


    @Override
    public ResponseEntity<ApiResponse> deleteProduct(int id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        productRepository.deleteById(id);
        return ResponseEntity.ok(new ApiResponse("Product deleted successfully", null, "Success"));
    }

}
