package com.example.ecomerce.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table (name ="viewed_recently")
public class ViewedRecently {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn( name ="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name ="product_id")
    private Product product;

    @Column (name = "view_at")
    private LocalDateTime viewAt;
}
