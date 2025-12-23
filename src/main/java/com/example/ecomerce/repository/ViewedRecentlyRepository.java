package com.example.ecomerce.repository;

import com.example.ecomerce.entity.User;
import com.example.ecomerce.entity.ViewedRecently;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ViewedRecentlyRepository extends JpaRepository<ViewedRecently,Integer> {
    List<ViewedRecently> findByUser(User user);
}
