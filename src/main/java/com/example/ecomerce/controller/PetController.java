package com.example.ecomerce.controller;

import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.dto.PetFilter;
import com.example.ecomerce.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pet")

public class PetController {

    @Autowired
    private PetService petService;

    @GetMapping ("/new")
    public ResponseEntity<ApiResponse> getNewAnimal (){
        return petService.getNewAnimal();
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllPet (@RequestParam( defaultValue = "0") int page,
                                                  @RequestParam (defaultValue =  "10") int size){
        Pageable pageable = PageRequest.of(page,size);
        return petService.getAllPet(pageable);
    }

    @PostMapping ("/filter")
    public ResponseEntity<ApiResponse> Filter (@RequestBody PetFilter petFilter, Pageable pageable){
        return petService.Filter(petFilter,pageable);
    }
}
