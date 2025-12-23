package com.example.ecomerce.repository;

import com.example.ecomerce.entity.Product;
import com.example.ecomerce.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT COUNT(p.id) FROM Product p")
    Integer getTotalProducts();

    @Query("SELECT p FROM Product p " +
            "WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR LOWER(p.brand) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Product> searchProducts(@Param("keyword") String keyword, Pageable pageable);

    List<Product> findByCategoryIdAndIdNot(int categoryId, int productId);
    Page<Product> findByPriceLessThanEqual(double maxPrice, Pageable pageable);
    List<Product> findTop8ByOrderByIdDesc();

    Page<Product> findAllByOrderByIdDesc(Pageable pageable);
}
