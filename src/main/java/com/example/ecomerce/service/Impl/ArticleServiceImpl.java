package com.example.ecomerce.service.Impl;

import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.dto.*;
import com.example.ecomerce.entity.Article;
import com.example.ecomerce.entity.Pet;
import com.example.ecomerce.entity.Product;
import com.example.ecomerce.enums.Error;
import com.example.ecomerce.exception.ErrorArticle;
import com.example.ecomerce.repository.ArticleRepository;
import com.example.ecomerce.service.ArticleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ResponseEntity<ApiResponse> getAllArticle (Pageable pageable){
        Page <Article> articles = articleRepository.findAll(pageable);
        List<ArticleResponseDto> articleResponseDtos = articles.getContent().stream()
                .map(article -> modelMapper.map(article, ArticleResponseDto.class)).toList();
        PagedResponse<ArticleResponseDto> pagedResponse = new PagedResponse<>(articleResponseDtos, articles);
        return ResponseEntity.ok(new ApiResponse("list article",pagedResponse,"SUCCESS"));
    }


    @Override
    public ResponseEntity<ApiResponse> addArticle (ArticleRequestDto articleRequestDto){
        Article article = new Article();
        article.setBanner(articleRequestDto.getBanner());
        article.setTitle(articleRequestDto.getTitle());
        article.setArticleContent(articleRequestDto.getArticleContent());
        article.setCreateAt(LocalDateTime.now());
        articleRepository.save(article);
        return ResponseEntity.ok(new ApiResponse("Successfully added a post.", null,"SUCCESS"));
    }

    @Override
    public ResponseEntity<ApiResponse>updateArticle ( ArticleRequestDto articleRequestDto, int id){
        Article article = articleRepository.findById(id).orElse(null);
        if (article == null){
            return  ResponseEntity.badRequest().body(new ApiResponse("The article does not exist.", null,ErrorArticle.ARTICLE_NOT_FOUND));

        }

        article.setBanner(articleRequestDto.getBanner());
        article.setTitle(articleRequestDto.getTitle());
        article.setArticleContent(articleRequestDto.getArticleContent());
        articleRepository.save(article);
        return  ResponseEntity.ok(new ApiResponse("Article edited successfully.",null,"SUCCESS"));
    }

    @Override
    public ResponseEntity <ApiResponse> deleteArticle ( int id){
        Article article = articleRepository.findById(id).orElse(null);
        if ( article == null){
            return  ResponseEntity.badRequest().body(new ApiResponse("The article does not exist.", null, ErrorArticle.ARTICLE_NOT_FOUND));
        }

        articleRepository.deleteById(article.getId());
        return  ResponseEntity.ok(new ApiResponse("Post deleted successfully.", null,"SUCCESS"));
    }


    @Override
    public ResponseEntity<ApiResponse>getArticleById (int id){
        Article article = articleRepository.findById(id).orElse(null);
        if ( article == null){
            return ResponseEntity.badRequest().body(new ApiResponse("The article does not exist.", null,ErrorArticle.ARTICLE_NOT_FOUND));
        }
        return ResponseEntity.ok(new ApiResponse("article found",modelMapper.map(article,ArticleResponseDto.class),"SUCCESS"));
    }

    @Override
    public ResponseEntity<ApiResponse> getNewArticle (){
        List<Article> articles = articleRepository.findTop3ByOrderByIdDesc();
        List<ArticleResponseDto> articleResponseDtos = articles.stream().map(article->modelMapper.map(article, ArticleResponseDto.class) )
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse("show thanh cong",articleResponseDtos,"SUCCESS"));
    }
}
