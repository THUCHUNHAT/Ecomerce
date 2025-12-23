package com.example.ecomerce.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table (name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column (name ="conversation_id")
    private int conversationId;

    @ManyToOne
    @JoinColumn (name ="user_id")
    private User user;

    @Column (name ="content")
    private String content;

    @Column(name ="create_at")
    private LocalDateTime createAt;

}
