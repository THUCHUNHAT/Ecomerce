package com.example.ecomerce.service;

import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.dto.ProductRequestDto;
import com.example.ecomerce.dto.QuantityRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface ProductService {
   ResponseEntity<ApiResponse> getAllProduct(Pageable pageable);
   ResponseEntity<ApiResponse> addProduct(ProductRequestDto productRequestDto);
   ResponseEntity<ApiResponse> updateProduct(int id, ProductRequestDto productRequestDto);
   ResponseEntity<ApiResponse> deleteProduct(int id);
   ResponseEntity<ApiResponse> searchProducts(String keyword, Pageable pageable);
   ResponseEntity<ApiResponse> getProductById(int id);
   ResponseEntity<ApiResponse> getproductDetails(int id);
   ResponseEntity<ApiResponse> getRelatedProducts(int productId);
   ResponseEntity<ApiResponse> filterByMaximumPrice(double maxPrice, Pageable pageable);

   ResponseEntity<ApiResponse> getProductsSortedByPriceAsc(int page, int size);
   ResponseEntity<ApiResponse> getProductsDescByPrice(int page, int size);

   ResponseEntity<ApiResponse> DisplayTheEightNewestProducts();

}
