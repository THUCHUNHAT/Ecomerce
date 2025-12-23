package com.example.ecomerce.controller;
import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.dto.ArticleRequestDto;
import com.example.ecomerce.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping ("/Article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;


    @GetMapping
    public ResponseEntity<ApiResponse> getAllArticle (@RequestParam( defaultValue = "0") int page,
                                                      @RequestParam (defaultValue =  "10") int size
    ){
        Pageable pageable = PageRequest.of(page,size);
        return articleService.getAllArticle(pageable);
    }

    @PostMapping
    public ResponseEntity <ApiResponse> addArticle (@RequestBody ArticleRequestDto articleRequestDto){
        return articleService.addArticle(articleRequestDto);
    }

    @PutMapping ("/{id}")
    public ResponseEntity<ApiResponse> updateArticle (@RequestBody ArticleRequestDto articleRequestDto, @PathVariable int id){
        return articleService.updateArticle(articleRequestDto,id);
    }

    @DeleteMapping ( "/{id}")
    public ResponseEntity<ApiResponse> deleteArticle (@PathVariable int id){
        return articleService.deleteArticle(id);
    }

    @GetMapping ("/{id}")
    public ResponseEntity<ApiResponse>getArticleById (@PathVariable int id){
        return articleService.getArticleById(id);
    }

    @GetMapping ("/new")
    public ResponseEntity<ApiResponse>getNewArticle (){
        return articleService.getNewArticle();
    }
}
