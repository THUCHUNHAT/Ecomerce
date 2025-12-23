package com.example.ecomerce.service.Impl;

import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.dto.PagedResponse;
import com.example.ecomerce.dto.ProductResponseDto;
import com.example.ecomerce.dto.ViewedRecentlyDto;
import com.example.ecomerce.entity.Favorite;
import com.example.ecomerce.entity.Product;
import com.example.ecomerce.entity.User;
import com.example.ecomerce.entity.ViewedRecently;
import com.example.ecomerce.enums.Error;
import com.example.ecomerce.exception.ErrorProduct;
import com.example.ecomerce.exception.ErrorUser;
import com.example.ecomerce.repository.ProductRepository;
import com.example.ecomerce.repository.UserRepository;
import com.example.ecomerce.repository.ViewedRecentlyRepository;
import com.example.ecomerce.service.ViewedRecentlyService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ViewedRecentlyServiceImpl implements ViewedRecentlyService {
    @Autowired
    private ViewedRecentlyRepository viewedRecentlyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ResponseEntity<ApiResponse>addToViewedProducts (int productId, Authentication authentication){
        if (authentication == null){
            return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse("User token not identified",null, ErrorUser.USER_NOT_AUTHENTICATED));
        }
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null){
            return ResponseEntity.badRequest().body(new ApiResponse("user not found",null,ErrorUser.USER_NOT_FOUND));
        }
        Product product = productRepository.findById(productId).orElse(null);
        if (product== null){
            return ResponseEntity.badRequest().body(new ApiResponse("product not found",null, ErrorProduct.PRODUCT_NOT_FOUND));
        }

        ViewedRecently viewedRecently = new ViewedRecently();
        viewedRecently.setProduct(product);
        viewedRecently.setUser(user);
        viewedRecently.setViewAt(LocalDateTime.now());
        viewedRecentlyRepository.save(viewedRecently);
        return  ResponseEntity.ok(new ApiResponse("Add product to viewed products successfully",null,"SUCCESS"));
    }

    @Override
    public ResponseEntity<ApiResponse> showProductsUsersHaveViewed (Authentication authentication){
        if (authentication == null){
            return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse("User token not identified",null,   ErrorUser.USER_NOT_AUTHENTICATED));
        }
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null){
            return ResponseEntity.badRequest().body(new ApiResponse("user not found",null,ErrorUser.USER_NOT_FOUND));
        }
        List<ViewedRecently> viewedRecentlies = viewedRecentlyRepository.findByUser(user);
        List<ViewedRecentlyDto> viewedRecentlyDtos = viewedRecentlies.stream().map(view -> modelMapper.map(view.getProduct(), ViewedRecentlyDto.class)).toList();
        System.out.println("username" + username);
        return ResponseEntity.ok(new ApiResponse("Show recently viewed products",viewedRecentlyDtos,"SUCCESS"));
    }

    @Override
    public ResponseEntity<ApiResponse> DeleteRecentlyViewedProducts (int id){
        ViewedRecently viewedRecently = viewedRecentlyRepository.findById(id).orElse(null);
        if ( viewedRecently == null){
            return ResponseEntity.badRequest().body(new ApiResponse("The product viewed does not exist.",null,"FAIL"));
        }
        viewedRecentlyRepository.deleteById(id);
        return  ResponseEntity.ok().body(new ApiResponse("deleted successfully", null,"SUCCESS"));
    }


}
