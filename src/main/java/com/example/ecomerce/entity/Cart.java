package com.example.ecomerce.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Setter
@Getter
@Table (name ="cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private  User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name ="quantity")
    private BigDecimal  quantity;

    @Column(name = "total_price")
    private BigDecimal totalPrice;
}
