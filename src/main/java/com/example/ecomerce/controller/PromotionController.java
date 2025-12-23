package com.example.ecomerce.controller;
import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.dto.PromotionRequestDto;
import com.example.ecomerce.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/promotion")
public class PromotionController {

    @Autowired
    private PromotionService promotionService;

    @PostMapping
    public ResponseEntity<ApiResponse>addPromotion (@RequestBody PromotionRequestDto promotionRequestDto){
        return promotionService.addPromotion(promotionRequestDto);
    }

    @DeleteMapping ("/{id}")
    public ResponseEntity<ApiResponse>removePromotion (@PathVariable int id){
        return promotionService.removePromotion(id);
    }

    @PutMapping ("/{id}")
    public ResponseEntity<ApiResponse> updatePromotion (@PathVariable int id,PromotionRequestDto promotionRequestDto){
        return promotionService.updatePromotion(id, promotionRequestDto);
    }

    @GetMapping ("/{id}")
    public ResponseEntity<ApiResponse> getPromotionId(@PathVariable int id){
        return promotionService.getPromotionId(id);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllPromotion (
            @RequestParam (defaultValue = "0") int page,
            @RequestParam (defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page, size);
        return promotionService.getAllPromotion(pageable);
    }
}
