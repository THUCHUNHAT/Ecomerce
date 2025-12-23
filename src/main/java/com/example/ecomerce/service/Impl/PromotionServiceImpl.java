package com.example.ecomerce.service.Impl;
import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.dto.PagedResponse;
import com.example.ecomerce.dto.ProductResponseDto;
import com.example.ecomerce.dto.PromotionRequestDto;
import com.example.ecomerce.dto.PromotionResponseDto;
import com.example.ecomerce.entity.Product;
import com.example.ecomerce.entity.Promotion;
import com.example.ecomerce.enums.Error;
import com.example.ecomerce.exception.ErrorArticle;
import com.example.ecomerce.exception.ErrorPromotion;
import com.example.ecomerce.repository.PromotionRepository;
import com.example.ecomerce.service.PromotionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.ReactiveOffsetScrollPositionHandlerMethodArgumentResolver;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.CallableProcessingInterceptor;

import java.util.List;

@Service
public class PromotionServiceImpl implements PromotionService {

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ResponseEntity<ApiResponse> addPromotion (PromotionRequestDto promotionRequestDto){
        Promotion promotion = new Promotion();
        promotion.setName(promotionRequestDto.getName());
        promotion.setDescription(promotionRequestDto.getDescription());
        promotion.setType(promotionRequestDto.getType());
        promotion.setCouponCode(promotionRequestDto.getCouponCode());
        promotion.setDiscountValue(promotionRequestDto.getDiscountValue());
        promotion.setStartDate(promotionRequestDto.getStartDate());
        promotion.setEndDate(promotionRequestDto.getEndDate());
        promotion.setStatus(promotionRequestDto.getStatus());
        promotion.setMinOrderValue(promotionRequestDto.getMinOrderValue());
        promotion.setUsageLimit(promotionRequestDto.getUsageLimit());
        promotion.setBannerUrl(promotionRequestDto.getBannerUrl());
        promotionRepository.save(promotion);
        return  ResponseEntity.ok(new ApiResponse("Successfully added a promotion.",null, "SUCCESS"));
    }

    @Override
    public ResponseEntity<ApiResponse> removePromotion (int id){
        Promotion promotion = promotionRepository.findById(id).orElse(null);
        if (promotion == null){
            return ResponseEntity.badRequest().body(new ApiResponse("The promotional code does not exist.",null, ErrorPromotion.PROMOTION_NOT_FOUND));
        }
        promotionRepository.deleteById(id);
        return ResponseEntity.ok(new ApiResponse("Promotion successfully deleted.",null,"SUCCESS"));
    }

    @Override
    public ResponseEntity<ApiResponse> updatePromotion (int id, PromotionRequestDto promotionRequestDto){
        Promotion promotion = promotionRepository.findById(id).orElse(null);
        if (promotion == null){
            return ResponseEntity.badRequest().body(new ApiResponse("The promotional code does not exist.",null,ErrorPromotion.PROMOTION_NOT_FOUND));
        }
        promotion.setName(promotionRequestDto.getName());
        promotion.setDescription(promotionRequestDto.getDescription());
        promotion.setType(promotionRequestDto.getType());
        promotion.setCouponCode(promotionRequestDto.getCouponCode());
        promotion.setDiscountValue(promotionRequestDto.getDiscountValue());
        promotion.setStartDate(promotionRequestDto.getStartDate());
        promotion.setEndDate(promotionRequestDto.getEndDate());
        promotion.setStatus(promotionRequestDto.getStatus());
        promotion.setMinOrderValue(promotionRequestDto.getMinOrderValue());
        promotion.setUsageLimit(promotionRequestDto.getUsageLimit());
        promotion.setBannerUrl(promotionRequestDto.getBannerUrl());
        promotionRepository.save(promotion);
        return ResponseEntity.ok(new ApiResponse("Promotion updated successfully.",null,"SUCCESS"));
    }

    @Override
    public ResponseEntity<ApiResponse> getPromotionId(int id){
        Promotion promotion = promotionRepository.findById(id).orElse(null);
        if (promotion == null){
            return ResponseEntity.badRequest().body(new ApiResponse("The promotional code does not exist.",null,ErrorPromotion.PROMOTION_NOT_FOUND));
        }
        PromotionResponseDto promotionResponseDto = new PromotionResponseDto();
        promotionResponseDto.setName(promotion.getName());
        promotionResponseDto.setDescription(promotion.getDescription());
        promotionResponseDto.setType(promotion.getType());
        promotionResponseDto.setCouponCode(promotion.getCouponCode());
        promotionResponseDto.setDiscountValue(promotion.getDiscountValue());
        promotionResponseDto.setStartDate(promotion.getStartDate());
        promotionResponseDto.setEndDate(promotion.getEndDate());
        promotionResponseDto.setStatus(promotion.getStatus());
        promotionResponseDto.setMinOrderValue(promotion.getMinOrderValue());
        promotionResponseDto.setUsageLimit(promotion.getUsageLimit());
        promotionResponseDto.setBannerUrl(promotion.getBannerUrl());
        return  ResponseEntity.ok(new ApiResponse("promotion list",promotionResponseDto,"SUCCESS"));
    }


    @Override
    public ResponseEntity<ApiResponse> getAllPromotion(Pageable pageable) {
        Page<Promotion> promotions = promotionRepository.findAll(pageable);
        List<PromotionResponseDto> promotionResponseDtos = promotions.getContent().stream().map(promotion -> modelMapper.map(promotion, PromotionResponseDto.class)).toList();
        PagedResponse<PromotionResponseDto> pagedResponse = new PagedResponse<>(promotionResponseDtos, promotions);
        return ResponseEntity.ok(new ApiResponse("promotion list", pagedResponse, "SUCCESS"));

    }

}
