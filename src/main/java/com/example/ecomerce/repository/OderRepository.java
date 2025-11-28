package com.example.ecomerce.repository;

import com.example.ecomerce.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OderRepository extends JpaRepository<Order,Integer> {

    Optional<Order> findById(int id);
    @Query("SELECT SUM(o.totalAmount) " +
            "FROM Order o " +
            "WHERE o.status = 'COMPLETED' " +
            "AND MONTH(o.orderDate) = MONTH(CURRENT_DATE) " +
            "AND YEAR(o.orderDate) = YEAR(CURRENT_DATE)")
    Double getCurrentMonthRevenue();

}
