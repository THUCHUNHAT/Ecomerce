package com.example.ecomerce.service.Impl;

import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.dto.PagedResponse;
import com.example.ecomerce.dto.ProductResponseDto;
import com.example.ecomerce.entity.Favorite;
import com.example.ecomerce.entity.Product;
import com.example.ecomerce.entity.User;
import com.example.ecomerce.enums.Error;
import com.example.ecomerce.exception.ErrorProduct;
import com.example.ecomerce.exception.ErrorUser;
import com.example.ecomerce.repository.FavoriteRepository;
import com.example.ecomerce.repository.ProductRepository;
import com.example.ecomerce.repository.UserRepository;
import com.example.ecomerce.service.FavoriteService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteServiceImpl implements FavoriteService {
    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ResponseEntity<ApiResponse> addProductToFavorites(int productId, Authentication authentication){
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse("User not authenticated", null, ErrorUser.USER_NOT_AUTHENTICATED));
        }
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null){
            return ResponseEntity.badRequest().body(new ApiResponse("user not found",null,ErrorUser.USER_NOT_FOUND));
        }
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null){
            return ResponseEntity.badRequest().body(new ApiResponse("product not found",null, ErrorProduct.PRODUCT_NOT_FOUND));
        }

        Favorite favorite = new Favorite();
        favorite.setProduct(product);
        favorite.setUser(user);
        favoriteRepository.save(favorite);
        return  ResponseEntity.ok(new ApiResponse("list favorite ",null, "SUCCESS"));
    }

    @Override
    public ResponseEntity<ApiResponse> getAllFavoriteProducts(Authentication authentication, Pageable pageable){
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse("User not authenticated", null, ErrorUser.USER_NOT_AUTHENTICATED));
        }
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null){
            return ResponseEntity.badRequest().body(new ApiResponse("user not found",null,ErrorUser.USER_NOT_FOUND));
        }
        Page<Favorite> favorites = favoriteRepository.findByUser(user, pageable);
        List<ProductResponseDto> productDtos = favorites.stream().map(fav -> modelMapper.map(fav.getProduct(), ProductResponseDto.class)).toList();
        PagedResponse<ProductResponseDto> pagedResponse = new PagedResponse<>(productDtos, favorites);
        return ResponseEntity.ok(new ApiResponse("List favorite products", pagedResponse, "SUCCESS"));
    }

    @Override
    public ResponseEntity<ApiResponse> removeProductFromFavorites (int productId,Authentication authentication){
        if (authentication == null){
            return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse("User not authenticated",null,ErrorUser.USER_NOT_AUTHENTICATED));
        }

        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
        favoriteRepository.deleteByUserAndProduct(user, product);
        return ResponseEntity.ok(new ApiResponse("Product removed from favorites successfully.",null,"SUCCESS"));
    }




}
