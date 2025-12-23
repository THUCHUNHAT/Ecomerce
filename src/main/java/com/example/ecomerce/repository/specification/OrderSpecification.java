package com.example.ecomerce.repository.specification;

import com.example.ecomerce.entity.Order;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class OrderSpecification {
    public static Specification<Order> statusEquals(String status) {
        return (root, query, cb) -> {
            if (status == null || status.isEmpty()) {
                return cb.conjunction(); // không thêm điều kiện
            }
            return cb.equal(root.get("status"), status);
        };
    }

    public static Specification<Order> paymentEquals(String paymentMethod) {
        return (root, query, cb) -> {
            if (paymentMethod == null || paymentMethod.isEmpty()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("paymentMethod"), paymentMethod);
        };
    }
}
