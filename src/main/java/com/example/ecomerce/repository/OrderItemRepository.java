package com.example.ecomerce.repository;

import com.example.ecomerce.entity.Order;
import com.example.ecomerce.entity.OrderItem;
import com.example.ecomerce.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem,Integer> {
    List<OrderItem> findByOrder(Order order);

    @Query("SELECT o FROM OrderItem o " +
            "WHERE LOWER(o.productName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<OrderItem> searchOrderItem(@Param("keyword") String keyword, Pageable pageable);

}
