package com.example.ecomerce.repository;

import com.example.ecomerce.entity.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriberRepository extends JpaRepository <Subscriber,Integer> {
    Subscriber findByEmail(String email);
}
