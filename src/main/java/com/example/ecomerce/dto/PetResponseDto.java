package com.example.ecomerce.dto;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class PetResponseDto {
    private int id;
    private String imageUrl;
    private String name;
    private String breed;
    private String color;
    private int age;
    private String gender;
    private String location;
    private BigDecimal price;
    private String description;
}
