package com.example.ecomerce.controller;

import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.dto.UserRequestDto;
import com.example.ecomerce.enums.UserRole;
import com.example.ecomerce.enums.UserStatus;
import com.example.ecomerce.service.UserService;
import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return userService.getAllUsers(pageable);
    }

    @PostMapping
    public ResponseEntity<ApiResponse> addUser(@RequestBody @Valid UserRequestDto userRequestDto){
        return userService.addUser(userRequestDto);
    }

    @PatchMapping ("/{id}")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable @Valid int id, @RequestBody UserRequestDto userRequestDto) {
        return userService.updateUser(id, userRequestDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable int id) {
        return userService.deleteUser(id);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> searchUsers(@RequestParam String keyword, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userService.searchUsers(keyword, pageable);
    }

    @GetMapping ("/{id}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable int id){
        return userService.getUserById(id);
    }

    @PutMapping ("/{id}/role")
    public ResponseEntity<ApiResponse>updateTheRoleForTheAccount (@PathVariable int id, @PathVariable UserRole role){
        return userService.updateTheRoleForTheAccount(role,id);
    }

    @PutMapping ("/{id}/{status}")
    public ResponseEntity<ApiResponse> updateAccountStatus (@PathVariable int id, UserStatus status){
        return userService.updateAccountStatus(id,status);
    }

}
