package com.example.ecomerce.dto;
import com.example.ecomerce.entity.Product;
import com.example.ecomerce.entity.User;
import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class ViewedRecentlyDto {
    private Product product;
    private User username;
}
