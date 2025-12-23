package com.example.ecomerce.repository;

import com.example.ecomerce.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article,Integer> {

    List<Article> findTop3ByOrderByIdDesc();
}
