package com.example.ecomerce.service;

import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.dto.UserRequestDto;
import com.example.ecomerce.enums.UserRole;
import com.example.ecomerce.enums.UserStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<ApiResponse> getAllUsers(Pageable pageable);
    ResponseEntity<ApiResponse> addUser(UserRequestDto userRequestDto);
    ResponseEntity<ApiResponse> updateUser(int id, UserRequestDto userRequestDto);
    ResponseEntity<ApiResponse> deleteUser(int id);
    ResponseEntity<ApiResponse> searchUsers(String keyword, Pageable pageable);
    ResponseEntity<ApiResponse> getUserById(int id);
    ResponseEntity<ApiResponse> updateTheRoleForTheAccount (UserRole role, int id);
    ResponseEntity<ApiResponse> updateAccountStatus (int id,UserStatus status);
}
