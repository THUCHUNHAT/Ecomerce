package com.example.ecomerce.service.Impl;

import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.dto.PagedResponse;
import com.example.ecomerce.dto.ReviewDto;
import com.example.ecomerce.dto.ReviewResponseDto;
import com.example.ecomerce.entity.Product;
import com.example.ecomerce.entity.Review;
import com.example.ecomerce.entity.User;
import com.example.ecomerce.enums.Error;
import com.example.ecomerce.exception.ErrorProduct;
import com.example.ecomerce.exception.ErrorUser;
import com.example.ecomerce.repository.ProductRepository;
import com.example.ecomerce.repository.ReviewRepository;
import com.example.ecomerce.repository.UserRepository;
import com.example.ecomerce.service.ReviewService;
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
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;



    public ResponseEntity<ApiResponse> review(ReviewDto reviewDto, Authentication authentication,int productId){
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse("User not authenticated", null, ErrorUser.USER_NOT_AUTHENTICATED));
        }
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElse(null);
        if(user == null){
            return ResponseEntity.badRequest().body(new ApiResponse("user undefined",null, ErrorUser.USER_NOT_FOUND));
        }
        Product product = productRepository.findById(productId).orElse(null);
        if ( product == null){
            return ResponseEntity.badRequest().body(new ApiResponse("unidentified product",null, ErrorProduct.PRODUCT_NOT_FOUND));
        }

        Review review = new Review();
        review.setComments(reviewDto.getComment());
        review.setRatting(reviewDto.getRatting());
        review.setUsername(user);
        review.setProduct(product);
        reviewRepository.save(review);
        return  ResponseEntity.ok(new ApiResponse("Create a successful review",null, "SUCCESS"));
    }

    public ResponseEntity<ApiResponse> getProductReview(int productId, Pageable pageable) {
        Page<Review> reviews = reviewRepository.findByProductId(productId, pageable);
        List<ReviewResponseDto> reviewDtos = reviews.getContent().stream()
                .map(review -> modelMapper.map(review, ReviewResponseDto.class))
                .toList();
        PagedResponse<ReviewResponseDto> pagedResponse = new PagedResponse<>(reviewDtos, reviews);
        return ResponseEntity.ok(new ApiResponse("List review", pagedResponse, "SUCCESS"));
    }


}
