package com.example.ecomerce.repository;

import com.example.ecomerce.entity.Favorite;
import com.example.ecomerce.entity.Product;
import com.example.ecomerce.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite,Integer> {

    Page<Favorite> findByUser(User user, Pageable pageable);

    void deleteByUserAndProduct(User user, Product product);
}
