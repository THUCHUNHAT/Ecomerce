package com.example.ecomerce.repository;
import com.example.ecomerce.entity.Review;
import com.example.ecomerce.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    Page<Review> findByProductId(int productId, Pageable pageable);
}
