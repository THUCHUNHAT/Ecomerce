package com.example.ecomerce.entity;
import com.example.ecomerce.enums.TokenType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
@Setter
@Getter
@Entity
@Table (name ="verification_token")

public class Verification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name ="token")
    private String token;

    @ManyToOne
    @JoinColumn(name = "user_id" )
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name ="token_type")
    private TokenType tokenType;

    @Column (name ="expiry_date")
    private LocalDateTime expiryDate;

}
