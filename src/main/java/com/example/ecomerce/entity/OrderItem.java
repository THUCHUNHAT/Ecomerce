package com.example.ecomerce.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Setter
@Getter
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column (name ="product_name")
    private String productName;

    @Column( name = "unit_price")
    private BigDecimal unitPrice;

    @Column (name ="quantity")
    private int quantity;

    @Column (name = "total_price")
    private BigDecimal totalPrice;


}
