package com.example.ecomerce.dto;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Setter
@Getter
public class ProductResponseDto {
    private String imageUrl;
    private String name;
    private BigDecimal price;
    private String categoryName;
    private String brand;

}
