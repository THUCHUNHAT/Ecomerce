package com.example.ecomerce.service.Impl;

import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.dto.CartItemResponseDto;
import com.example.ecomerce.dto.ProductResponseDto;
import com.example.ecomerce.dto.QuantityRequest;
import com.example.ecomerce.entity.Cart;
import com.example.ecomerce.entity.Product;
import com.example.ecomerce.entity.User;
import com.example.ecomerce.enums.Error;
import com.example.ecomerce.exception.ErrorCart;
import com.example.ecomerce.exception.ErrorProduct;
import com.example.ecomerce.exception.ErrorUser;
import com.example.ecomerce.repository.CartRepository;
import com.example.ecomerce.repository.CategoryRepository;
import com.example.ecomerce.repository.ProductRepository;
import com.example.ecomerce.repository.UserRepository;
import com.example.ecomerce.service.CartService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Override
    public ResponseEntity<ApiResponse> addProductsToCart(int productId, Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse("User not authenticated", null, ErrorUser.USER_NOT_AUTHENTICATED));
        }
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null){
            return ResponseEntity.badRequest().body(new ApiResponse("user not found",null,ErrorUser.USER_NOT_FOUND));
        }
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null){
            return ResponseEntity.badRequest().body(new ApiResponse("product not found", null, ErrorUser.USER_NOT_FOUND));
        }
        Cart cart = cartRepository.findByUserAndProduct(user, product)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    newCart.setProduct(product);
                    newCart.setQuantity(BigDecimal.valueOf(0));
                    return newCart;
                });
        cart.setQuantity(cart.getQuantity().add (BigDecimal.valueOf(1)));
        cart.setTotalPrice(cart.getQuantity().multiply((product.getPrice())));
        cartRepository.save(cart);
        return ResponseEntity.ok(new ApiResponse("Product added to cart", cart, "SUCCESS"));
    }


    @Transactional
    @Override
    public ResponseEntity<ApiResponse> removeProductFromCart(int productId, Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse("User not authenticated", null, ErrorUser.USER_NOT_AUTHENTICATED));
        }
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null){
            return ResponseEntity.badRequest().body(new ApiResponse("user not found",null,ErrorUser.USER_NOT_FOUND));
        }
        Product product = productRepository.findById(productId).orElse(null);
        if (product== null){
            return ResponseEntity.badRequest().body(new ApiResponse("product not found", null, ErrorProduct.PRODUCT_NOT_FOUND));
        }
        cartRepository.deleteByUserAndProduct(user, product);
        return ResponseEntity.ok(new ApiResponse("Successfully removed product from cart", null,"SUCCESS"));
    }



    @Override
    public ResponseEntity<ApiResponse> seePriceInformation(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse("User not authenticated", null, ErrorUser.USER_NOT_AUTHENTICATED));
        }
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null){
            return ResponseEntity.badRequest().body(new ApiResponse("user not found",null,ErrorUser.USER_NOT_FOUND));
        }
        List<Cart> carts = cartRepository.findByUser(user);
        List<CartItemResponseDto> cartItems = carts.stream().map(cart -> {
            ProductResponseDto productDto = modelMapper.map(cart.getProduct(), ProductResponseDto.class);
            return new CartItemResponseDto(productDto, cart.getQuantity(),cart.getTotalPrice());
        }).toList();
        return ResponseEntity.ok(new ApiResponse("Cart items of user", cartItems, "SUCCESS"));
    }

    @Override
    public ResponseEntity<ApiResponse> setProductQuantity(Authentication authentication, int productId, QuantityRequest quantityRequest) {
        BigDecimal bigDecimalQuantity = BigDecimal.valueOf(quantityRequest.getQuantity());
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse("User not authenticated", null, ErrorUser.USER_NOT_AUTHENTICATED));
        }
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null){
            return ResponseEntity.badRequest().body(new ApiResponse("user not found",null,ErrorUser.USER_NOT_FOUND));
        }
        Product product = productRepository.findById(productId).orElse(null);
        if (product== null){
            return ResponseEntity.badRequest().body(new ApiResponse("product not found",null,ErrorProduct.PRODUCT_NOT_FOUND));
        }
        Cart cart = cartRepository.findByProduct(product).orElse(null);
        if (cart == null){
            return ResponseEntity.badRequest().body(new ApiResponse("The product is not in the shopping cart.",null, ErrorCart.PRODUCT_NOT_IN_THE_SHOPPING_CART));
        }

        if (quantityRequest.getQuantity() <=0){
            return ResponseEntity.badRequest().body(new ApiResponse("The number of products must be greater than 0", null, ErrorCart.INVALID_QUANTITY));
        }
        cart.setQuantity(bigDecimalQuantity);
        cart.setTotalPrice(cart.getQuantity().multiply((product.getPrice())));
        cartRepository.save(cart);
        return ResponseEntity.ok(new ApiResponse("Product quantity setting successful", null, "SUCCESS"));
    }
}
