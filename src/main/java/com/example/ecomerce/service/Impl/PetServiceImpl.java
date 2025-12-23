package com.example.ecomerce.service.Impl;

import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.dto.*;
import com.example.ecomerce.entity.Article;
import com.example.ecomerce.entity.Order;
import com.example.ecomerce.entity.Pet;
import com.example.ecomerce.repository.PetRepository;
import com.example.ecomerce.repository.specification.OrderSpecification;
import com.example.ecomerce.repository.specification.PetSpecification;
import com.example.ecomerce.service.PetService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PetServiceImpl implements PetService {
    @Autowired
    private PetRepository petRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ResponseEntity<ApiResponse> getNewAnimal (){
        List<Pet> pets = petRepository.findTop8ByOrderByIdDesc();
        List<PetResponseDto> petResponseDtos = pets.stream().map(pet->modelMapper.map(pet, PetResponseDto.class) )
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse("show thanh cong",petResponseDtos,"SUCCESS"));
    }

    @Override
    public ResponseEntity<ApiResponse> getAllPet(Pageable pageable){
        Page<Pet> pets = petRepository.findAllByOrderByIdDesc(pageable);
        List<PetResponseDto> petResponseDtos = pets.getContent().stream()
                .map(pet -> modelMapper.map(pet, PetResponseDto.class)).toList();
        PagedResponse<PetResponseDto> pagedResponse = new PagedResponse<>(petResponseDtos, pets);
        return ResponseEntity.ok(new ApiResponse("list pet",pagedResponse,"SUCCESS"));
    }

    @Override
    public ResponseEntity<ApiResponse> Filter (PetFilter petFilter, Pageable pageable){
        Specification<Pet> specification = Specification.where(PetSpecification.genderEquals(petFilter.getGender()))
                .and(PetSpecification.colorEquals(petFilter.getColor()));
        Page<Pet> pets = petRepository.findAll(specification, pageable);
        List<PetResponseDto> petResponseDtos = pets.getContent().stream().map(pet -> modelMapper.map(pet, PetResponseDto.class)).toList();
        ApiResponse response = new ApiResponse("Filter Pet", petResponseDtos, "SUCCESS");
        return ResponseEntity.ok(response);
    }


}
