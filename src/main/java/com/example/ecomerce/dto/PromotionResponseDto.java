package com.example.ecomerce.dto;
import com.example.ecomerce.enums.PromotionStatus;
import com.example.ecomerce.enums.PromotionType;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Setter
@Getter
public class PromotionResponseDto {
    private String name;
    private String description;
    private PromotionType type;
    private String couponCode;
    private BigDecimal discountValue;
    private LocalDateTime startDate;
    private LocalDateTime  endDate;
    private PromotionStatus status;
    private BigDecimal minOrderValue;
    private int usageLimit;
    private String bannerUrl;
}
