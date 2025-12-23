package com.example.ecomerce.service.Impl;
import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.dto.*;
import com.example.ecomerce.entity.*;
import com.example.ecomerce.enums.Error;
import com.example.ecomerce.exception.ErrorCategory;
import com.example.ecomerce.exception.ErrorProduct;
import com.example.ecomerce.repository.CartRepository;
import com.example.ecomerce.repository.CategoryRepository;
import com.example.ecomerce.repository.ProductRepository;
import com.example.ecomerce.repository.UserRepository;
import com.example.ecomerce.service.ProductService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static java.awt.AWTEventMulticaster.add;


@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Override
    public ResponseEntity<ApiResponse> getAllProduct(Pageable pageable) {
        Page<Product> products = productRepository.findAllByOrderByIdDesc(pageable);
        List<ProductResponseDto> productDtos = products.getContent().stream()
                .map(product -> modelMapper.map(product, ProductResponseDto.class))
                .toList();
        PagedResponse<ProductResponseDto> pagedResponse = new PagedResponse<>(productDtos, products);
        return ResponseEntity.ok(new ApiResponse("List Product", pagedResponse, "SUCCESS"));


    }

    @Override
    public ResponseEntity<ApiResponse> addProduct(ProductRequestDto dto) {
        Category category = categoryRepository.findById(dto.getCategoryId()).orElse(null);
        if (category == null) {
            return ResponseEntity.badRequest().body(new ApiResponse("PRODUCT CATEGORY UNDEFINED", null, ErrorCategory.PRODUCT_CATEGORY_UNDEFINED));
        }
        Product product = new Product();
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setBrand(dto.getBrand());
        product.setImageUrl(dto.getImageUrl());
        product.setCategory(category);
        Product savedProduct = productRepository.save(product);
        ProductResponseDto responseDto = modelMapper.map(savedProduct, ProductResponseDto.class);
        return ResponseEntity.ok(
                new ApiResponse("Product added successfully", responseDto, "SUCCESS")
        );
    }


    @Override
    public ResponseEntity<ApiResponse> updateProduct(int id, ProductRequestDto dto) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return ResponseEntity.badRequest().body(new ApiResponse("PRODUCT NOT DEFINED", null, ErrorProduct.PRODUCT_NOT_FOUND));
        }
        Category category = categoryRepository.findById(dto.getCategoryId()).orElse(null);
        if (category == null) {
            return ResponseEntity.badRequest().body(new ApiResponse("PRODUCT CATEGORY UNDEFINED", null, ErrorCategory.PRODUCT_CATEGORY_UNDEFINED));
        }
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setBrand(dto.getBrand());
        product.setImageUrl(dto.getImageUrl());
        product.setCategory(category);
        Product savedProduct = productRepository.save(product);
        ProductResponseDto responseDto = modelMapper.map(savedProduct, ProductResponseDto.class);
        return ResponseEntity.ok(new ApiResponse("Product updated successfully", responseDto, "SUCCESS"));
    }


    @Override
    public ResponseEntity<ApiResponse> deleteProduct(int id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return ResponseEntity.badRequest().body(new ApiResponse("PRODUCT NOT DEFINED", null, ErrorProduct.PRODUCT_NOT_FOUND));
        }
        productRepository.deleteById(product.getId());
        return ResponseEntity.ok(new ApiResponse("Product deleted successfully", null, "SUCCESS"));
    }

    @Override
    public ResponseEntity<ApiResponse> searchProducts(String keyword, Pageable pageable) {
        Page<Product> products = productRepository.searchProducts(keyword, pageable);
        List<ProductResponseDto> productDtos = products.getContent().stream()
                .map(product -> modelMapper.map(product, ProductResponseDto.class))
                .toList();
        PagedResponse<ProductResponseDto> pagedResponse = new PagedResponse<>(productDtos, products);
        return ResponseEntity.ok(new ApiResponse("Search Product", pagedResponse, "SUCCESS"));
    }

    @Override
    public ResponseEntity<ApiResponse> getProductById(int id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return ResponseEntity.badRequest().body(new ApiResponse("PRODUCT NOT DEFINED", null, ErrorProduct.PRODUCT_NOT_FOUND));
        }
        return ResponseEntity.ok(new ApiResponse("Product found", modelMapper.map(product, ProductResponseDto.class), "SUCCESS"));

    }

    @Override
    public ResponseEntity<ApiResponse> getproductDetails(int id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return ResponseEntity.badRequest().body(new ApiResponse("PRODUCT NOT DEFINED", null, ErrorProduct.PRODUCT_NOT_FOUND));
        }
        return ResponseEntity.ok(new ApiResponse("Product found", modelMapper.map(product, DetailProductDto.class), "SUCCESS"));

    }

    @Override
    public ResponseEntity<ApiResponse> getRelatedProducts(int productId) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            return ResponseEntity.badRequest().body(new ApiResponse("PRODUCT NOT DEFINED", null, ErrorProduct.PRODUCT_NOT_FOUND));
        }
        int categoryId = product.getCategory().getId();
        List<Product> relatedProducts = productRepository.findByCategoryIdAndIdNot(categoryId, productId);
        List<ProductResponseDto> productDtos = relatedProducts.stream()
                .map(p -> modelMapper.map(p, ProductResponseDto.class))
                .toList();
        return ResponseEntity.ok(new ApiResponse("Related products", productDtos, "SUCCESS"));
    }


    @Override
    public ResponseEntity<ApiResponse> filterByMaximumPrice(double maxPrice, Pageable pageable) {
        Page<Product> products = productRepository.findByPriceLessThanEqual(maxPrice, pageable);
        List<ProductResponseDto> productDtos = products.getContent().stream()
                .map(product -> modelMapper.map(product, ProductResponseDto.class))
                .toList();
        PagedResponse<ProductResponseDto> pagedResponse = new PagedResponse<>(productDtos, products);
        return ResponseEntity.ok(new ApiResponse("List Product", pagedResponse, "SUCCESS"));

    }

    @Override
    public ResponseEntity<ApiResponse> getProductsSortedByPriceAsc(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("price").ascending());
        Page<Product> products = productRepository.findAll(pageable);
        List<ProductResponseDto> productDtos = products.getContent().stream()
                .map(product -> modelMapper.map(product, ProductResponseDto.class))
                .toList();
        PagedResponse<ProductResponseDto> pagedResponse = new PagedResponse<>(productDtos, products);
        return ResponseEntity.ok(new ApiResponse("List Product", pagedResponse, "SUCCESS"));
    }

    @Override
    public ResponseEntity<ApiResponse> getProductsDescByPrice(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("price").descending());
        Page<Product> products = productRepository.findAll(pageable);
        List<ProductResponseDto> productDtos = products.getContent().stream()
                .map(product -> modelMapper.map(product, ProductResponseDto.class))
                .toList();

        PagedResponse<ProductResponseDto> pagedResponse = new PagedResponse<>(productDtos, products);
        return ResponseEntity.ok(new ApiResponse("List Product", pagedResponse, "SUCCESS"));
    }


    @Override
    public ResponseEntity<ApiResponse> DisplayTheEightNewestProducts() {
        List<Product> products = productRepository.findTop8ByOrderByIdDesc();
        List<ProductResponseDto> productDtos = products.stream()
                .map(p -> modelMapper.map(p, ProductResponseDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse("Showing eight of the latest successful products.", productDtos, "SUCCESS"));
    }
}

