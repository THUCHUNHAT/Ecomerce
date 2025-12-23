package com.example.ecomerce.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DetailProductDto {
    private String imageUrl;
    private String name;
    private double price;
    private String categoryName;
    private String brand;
    private String description;
}
