package com.example.ecomerce.entity;
import com.example.ecomerce.enums.UserRole;
import com.example.ecomerce.enums.UserStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    @Enumerated(EnumType.STRING)
    @Column (name ="status")
    private UserStatus status;

    @Enumerated(EnumType.STRING)
    @Column (name = "role")
    private UserRole role;
}
