package com.example.ecomerce.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
@Entity
@Getter
@Setter
@Table ( name = "subscribers")
public class Subscriber {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column (name ="email")
    private String email;

    @Column (name ="status")
    private String status;

    @Column (name = "created_at")
    private LocalDateTime createdAt;
}
