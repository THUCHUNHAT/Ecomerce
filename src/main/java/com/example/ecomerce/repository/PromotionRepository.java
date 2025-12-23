package com.example.ecomerce.repository;

import com.example.ecomerce.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion,Integer> {

    Promotion findByCouponCode(String couponCode);
}
