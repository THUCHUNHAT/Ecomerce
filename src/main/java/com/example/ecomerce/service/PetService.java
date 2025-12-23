package com.example.ecomerce.service;

import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.dto.PetFilter;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface PetService {
    ResponseEntity<ApiResponse>getNewAnimal ();
    ResponseEntity<ApiResponse> getAllPet (Pageable pageable);
    ResponseEntity<ApiResponse>Filter (PetFilter petFilter, Pageable pageable);
}
