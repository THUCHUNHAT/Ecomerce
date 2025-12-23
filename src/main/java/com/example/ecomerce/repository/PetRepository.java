package com.example.ecomerce.repository;

import com.example.ecomerce.entity.Pet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetRepository extends JpaRepository<Pet,Integer> {

    List<Pet> findTop8ByOrderByIdDesc();

    Page<Pet> findAllByOrderByIdDesc(Pageable pageable);

    Page<Pet> findAll(Specification<Pet> specification, Pageable pageable);
}
