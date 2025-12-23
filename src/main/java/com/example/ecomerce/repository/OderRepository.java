package com.example.ecomerce.repository;

import com.example.ecomerce.entity.Order;
import com.example.ecomerce.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OderRepository extends JpaRepository<Order,Integer>, JpaSpecificationExecutor<Order> {

    Optional<Order> findById(int id);


    @Query("SELECT SUM(o.totalAmount) " +
            "FROM Order o " +
            "WHERE o.status = 'COMPLETED' " +
            "AND MONTH(o.orderDate) = MONTH(CURRENT_DATE) " +
            "AND YEAR(o.orderDate) = YEAR(CURRENT_DATE)")
    Double getCurrentMonthRevenue();


    @Query("SELECT COUNT(o.id) " +
            "FROM Order o " +
            "WHERE MONTH(o.orderDate) = MONTH(CURRENT_DATE) " +
            "AND YEAR(o.orderDate) = YEAR(CURRENT_DATE)")
    Integer getTotalOrders();

//    @Query("SELECT MONTH(o.orderDate) AS month, COUNT(o.id) AS totalOrders " +
//            "FROM Order o " +
//            "WHERE YEAR(o.orderDate) = :year " +
//            "GROUP BY MONTH(o.orderDate) " +
//            "ORDER BY MONTH(o.orderDate)")
//    List<Object[]> getSalesAnalytics(@Param("year") int year);

//    @Query("SELECT SUM(o.totalAmount) " +
//            "FROM Order o " +
//            "WHERE o.status = 'COMPLETED' " +
//            "AND MONTH(o.orderDate) = MONTH(CURRENT_DATE) " +
//            "AND YEAR(o.orderDate) = YEAR(CURRENT_DATE)")
//    Integer get();
//}

    @Query("SELECT COUNT(o.id) " +
            "FROM Order o " +
            "WHERE o.status = 'CANCELED' " +
            "AND MONTH(o.orderDate) = MONTH(CURRENT_DATE) " +
            "AND YEAR(o.orderDate) = YEAR(CURRENT_DATE)")
    Integer getOrderCancelled();


    @Query("SELECT COUNT(o.id)  " +
            "FROM Order o " +
            "WHERE o.status = 'SHIPPED'  " +
            "AND MONTH(o.orderDate) = MONTH(CURRENT_DATE)" +
            "AND YEAR(o.orderDate) = YEAR(CURRENT_DATE)")
    Integer getOrderShipped();

//    @Query("SELECT o FROM Order o " +
//            "WHERE LOWER(o.orderCode) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
//            "   OR LOWER(o.shippingAddress) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
//            "   OR LOWER(o.paymentMethod) LIKE LOWER(CONCAT('%', :keyword, '%')) " )
//    Page<Order> searchOrders(@Param("keyword") String keyword, Pageable pageable);


}

