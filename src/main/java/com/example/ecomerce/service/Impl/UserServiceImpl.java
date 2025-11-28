package com.example.ecomerce.service.Impl;

import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.dto.PagedResponse;
import com.example.ecomerce.dto.ProductResponseDto;
import com.example.ecomerce.dto.UserRequestDto;
import com.example.ecomerce.dto.UserResponseDto;
import com.example.ecomerce.entity.User;
import com.example.ecomerce.repository.UserRepository;
import com.example.ecomerce.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public ResponseEntity<ApiResponse> getAllUsers(Pageable pageable){
        Page<User> users = userRepository.findAll(pageable);
        List<UserResponseDto> userDtos = users.getContent().stream()
                .map(user -> modelMapper.map(user, UserResponseDto.class))
                .toList();
        PagedResponse<UserResponseDto> pagedResponse = new PagedResponse<>(userDtos, users);
        return ResponseEntity.ok(new ApiResponse("List User", pagedResponse, "Success"));
    }

    @Override
    public ResponseEntity<ApiResponse> addUser(UserRequestDto userRequestDto){
        if (userRepository.existsByUsername(userRequestDto.getUsername())) {
            return ResponseEntity.badRequest().body(new ApiResponse("Username already exists", null, "USERNAME_EXISTS"));
        }

        if(userRepository.existsByEmail(userRequestDto.getEmail())){
            return  ResponseEntity.badRequest().body(new ApiResponse("Email already exists", null, "EMAIL_EXISTS"));
        }

        if (userRepository.existsByPhone(userRequestDto.getPhone())){
            return ResponseEntity.badRequest().body(new ApiResponse("Phone number already exists", null, "PHONE_EXISTS"));
        }
        User user = new User();
        user.setEmail(userRequestDto.getEmail());
        user.setPhone(userRequestDto.getPhone());
        user.setFullName(userRequestDto.getFullName());
        user.setUsername(userRequestDto.getUsername());
        user.setPassword(userRequestDto.getPassword());
        userRepository.save(user);
        return ResponseEntity.ok(new ApiResponse("User added successfully", null, "Success"));
    }

    @Override
    public ResponseEntity<ApiResponse> updateUser(int id, UserRequestDto dto) {
        if (dto.getUsername() != null && userRepository.existsByUsernameAndIdNot(dto.getUsername(), id)) {
            return ResponseEntity.badRequest().body(new ApiResponse("Username already exists", null, "USERNAME_EXISTS"));
        }
        if (dto.getEmail() != null && userRepository.existsByEmailAndIdNot(dto.getEmail(), id)) {
            return ResponseEntity.badRequest().body(new ApiResponse("Email already exists", null, "EMAIL_EXISTS"));
        }
        if (dto.getPhone() != null && userRepository.existsByPhoneAndIdNot(dto.getPhone(), id)) {
            return ResponseEntity.badRequest().body(new ApiResponse("Phone already exists", null, "PHONE_EXISTS"));
        }
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        if (dto.getUsername() != null) user.setUsername(dto.getUsername());
        if (dto.getPassword() != null) user.setPassword(dto.getPassword());
        if (dto.getPhone() != null) user.setPhone(dto.getPhone());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getFullName() != null) user.setFullName(dto.getFullName());
        userRepository.save(user);
        return ResponseEntity.ok(new ApiResponse("User updated successfully", null, "Success"));
    }


    @Override
    public ResponseEntity<ApiResponse> deleteUser(int id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.deleteById(id);
        return ResponseEntity.ok(new ApiResponse("User deleted successfully", null, "Success"));
    }


}
