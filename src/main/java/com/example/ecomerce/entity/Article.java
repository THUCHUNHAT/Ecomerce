package com.example.ecomerce.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table ( name = "article")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column (name = "banner")
    private String banner;

    @Column (name = "title")
    private String title;

    @Column (name = "article_content")
    private String articleContent;

    @Column (name ="article_category")
    private String articleCategory;

    @Column( name = "create_at")
    private LocalDateTime createAt;
}
