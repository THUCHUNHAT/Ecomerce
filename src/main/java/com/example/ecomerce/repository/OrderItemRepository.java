package com.example.ecomerce.repository;

import com.example.ecomerce.entity.Order;
import com.example.ecomerce.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem,Integer> {

    Arrays findByOrderId(int id);

    List<OrderItem> findByOrder(Order order);
}
