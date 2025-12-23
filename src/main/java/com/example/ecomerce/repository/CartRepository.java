package com.example.ecomerce.repository;
import com.example.ecomerce.entity.Cart;
import com.example.ecomerce.entity.Product;
import com.example.ecomerce.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart,Integer> {

    void deleteByProductIdAndUserId(User user, Product product);

    List<Cart> findByUser(User user);

    Optional<Cart> findByUserAndProduct(User user, Product product);

    Optional<Cart> findByProduct(Product product);


    boolean existsByUserAndProduct(User user, Product product);

    Optional<Cart> findByProductAndUser(Product product, User user);

    @Transactional
    void deleteByUserAndProduct(User user, Product product);}
