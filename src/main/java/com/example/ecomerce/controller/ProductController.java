package com.example.ecomerce.controller;

import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.dto.ProductRequestDto;
import com.example.ecomerce.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return productService.getAllProduct(pageable);
    }

    @PostMapping
    public ResponseEntity<ApiResponse> addProduct(@RequestBody @Valid ProductRequestDto productRequestDto) {
        return productService.addProduct(productRequestDto);
    }

    @PutMapping ("/{id}")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable @Valid int id, @RequestBody ProductRequestDto productRequestDto) {
        return productService.updateProduct(id, productRequestDto);
    }

    @DeleteMapping ("/{id}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable  int id) {
        return productService.deleteProduct(id);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> searchProducts(@RequestParam String keyword, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productService.searchProducts(keyword, pageable);
    }

    @GetMapping ("/{id}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable int id){
        return productService.getProductById(id);
    }

    @GetMapping("/{id}/detail")
    public ResponseEntity<ApiResponse> getproductDetails(@PathVariable int id){
        return productService.getproductDetails(id);
    }

    @GetMapping ("/RelatedProducts")
    public ResponseEntity<ApiResponse> getRelatedProducts (@PathVariable int productId){
        return productService.getRelatedProducts(productId);
    }

    @GetMapping("/maxprice/{maxPrice}")
    public ResponseEntity<ApiResponse> filterByMaximumPrice(@PathVariable double maxPrice,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productService.filterByMaximumPrice(maxPrice, pageable);
    }

    @GetMapping ("/sort")
    public ResponseEntity<ApiResponse> getProductsSortedByPriceAsc(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return productService.getProductsSortedByPriceAsc(page,size);

    }

    @GetMapping ("/desc")
    public ResponseEntity<ApiResponse>getProductsDescByPrice(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam (defaultValue = "10") int size
    ){
        return productService.getProductsDescByPrice(page,size);
    }

    @GetMapping("/products/newest")
    public ResponseEntity<ApiResponse> getEightNewestProducts()
    { return productService.DisplayTheEightNewestProducts();
    }


}
