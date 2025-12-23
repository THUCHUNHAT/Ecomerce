package com.example.ecomerce.entity;
import com.example.ecomerce.enums.PromotionStatus;
import com.example.ecomerce.enums.PromotionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Entity
@Setter
@Getter
@Table (name = "promotions")
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private PromotionType type;

    @Column (name ="coupon_code")
    private String couponCode;

    @Column (name ="discount_value")
    private BigDecimal discountValue;

    @Column (name = "start_date")
    private LocalDateTime startDate;

    @Column (name = "end_date")
    private LocalDateTime  endDate;

    @Enumerated(EnumType.STRING)
    @Column (name ="status")
    private PromotionStatus status;

    @Column (name = "min_order_value")
    private BigDecimal minOrderValue;

    @Column (name = "usage_limit")
    private int usageLimit;

    @Column (name = "banner_url")
    private String bannerUrl;

}
