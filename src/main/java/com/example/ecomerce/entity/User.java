package com.example.ecomerce.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Table (name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column (name = "username")
    private String username;

    @Column (name = "full_name")
    private String fullName;

    @Column (name ="email")
    private String email;

    @Column (name = "password")
    private String password;

    @Column (name = "phone")
    private String phone;

    @Column (name = "created_at")
    private LocalDateTime creactedAt;

    @Column (name = "updated_at")
    private LocalDateTime updatedAt;
}
