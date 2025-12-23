package com.example.ecomerce.payment.repository;

import com.example.ecomerce.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    Payment findByTxnRef(String txnRef);
}
