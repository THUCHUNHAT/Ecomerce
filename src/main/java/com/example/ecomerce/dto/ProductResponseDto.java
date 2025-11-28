package com.example.ecomerce.dto;
import com.example.ecomerce.entity.Category;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ProductResponseDto {
    private String imageUrl;
    private String name;
    private double price;
    private String categoryName;
    private String brand;

}
