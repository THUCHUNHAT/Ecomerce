package com.example.ecomerce.dto;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Setter
@Getter

public class ProductRequestDto {
    @NotBlank(message = "Product name is required")
    @Size(max = 200, message = "Product name must be less than or equal to 200 characters")
    private String name;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", inclusive = true, message = "Price must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Price must be a valid decimal with up to 2 digits after decimal")
    private double price;

    @NotNull(message = "Category ID is required")
    @Min(value = 1, message = "Category ID must be greater than 0")
    private int categoryId;

    @Size(max = 100, message = "Brand must be less than or equal to 100 characters")
    private String brand;

    @Size(max = 255, message = "Image URL must be less than or equal to 255 characters")
    private String imageUrl;



}
