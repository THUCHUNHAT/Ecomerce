package com.example.ecomerce.service.Impl;
import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.dto.PagedResponse;
import com.example.ecomerce.dto.UserRequestDto;
import com.example.ecomerce.dto.UserResponseDto;
import com.example.ecomerce.entity.User;
import com.example.ecomerce.enums.Error;
import com.example.ecomerce.enums.UserRole;
import com.example.ecomerce.enums.UserStatus;
import com.example.ecomerce.exception.ErrorUser;
import com.example.ecomerce.repository.UserRepository;
import com.example.ecomerce.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;


    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JavaMailSender mailSender;


    @Override
    public ResponseEntity<ApiResponse> getAllUsers(Pageable pageable){
        Page<User> users = userRepository.findAll(pageable);
        List<UserResponseDto> userDtos = users.getContent().stream()
                .map(user -> modelMapper.map(user, UserResponseDto.class))
                .toList();
        PagedResponse<UserResponseDto> pagedResponse = new PagedResponse<>(userDtos, users);
        return ResponseEntity.ok(new ApiResponse("List User", pagedResponse, "SUCCESS"));
    }

    @Override
    public ResponseEntity<ApiResponse> addUser(UserRequestDto userRequestDto){
        if (userRepository.existsByUsername(userRequestDto.getUsername())) {
            return ResponseEntity.badRequest().body(new ApiResponse("Username already exists", null, ErrorUser.USERNAME_EXISTS));
        }

        if(userRepository.existsByEmail(userRequestDto.getEmail())){
            return  ResponseEntity.badRequest().body(new ApiResponse("Email already exists", null, ErrorUser.EMAIL_EXISTS));
        }

        if (userRepository.existsByPhone(userRequestDto.getPhone())){
            return ResponseEntity.badRequest().body(new ApiResponse("Phone number already exists", null, ErrorUser.PHONE_EXISTS));
        }

        User user = new User();

        try {
            UserStatus status = UserStatus.valueOf(userRequestDto.getStatus().toUpperCase());
            if (!(status == UserStatus.ACTIVE || status == UserStatus.INACTIVE)) {
                return ResponseEntity.badRequest().body(new ApiResponse("Invalid status", null, ErrorUser.INVALID_USER_STATUS));
            }
            user.setStatus(status);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse("Invalid status", null, ErrorUser.INVALID_USER_STATUS));
        }


        try {
            UserRole role = UserRole.valueOf(userRequestDto.getRole().toUpperCase());
            if (!(role == UserRole.USER || role == UserRole.ADMIN)) {
                return ResponseEntity.badRequest().body(new ApiResponse("Invalid role", null, ErrorUser.INVALID_USER_ROLE));
            }
            user.setRole(role);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse("Invalid ROLE", null, ErrorUser.INVALID_USER_ROLE));
        }


        user.setEmail(userRequestDto.getEmail());
        user.setPhone(userRequestDto.getPhone());
        user.setFullName(userRequestDto.getFullName());
        user.setUsername(userRequestDto.getUsername());
        user.setPassword(userRequestDto.getPassword());

        userRepository.save(user);
        return ResponseEntity.ok(new ApiResponse("User added successfully", null, "SUCCESS"));
    }

    @Override
    public ResponseEntity<ApiResponse> updateUser(int id, UserRequestDto dto) {
        if (dto.getUsername() != null && userRepository.existsByUsernameAndIdNot(dto.getUsername(), id)) {
            return ResponseEntity.badRequest().body(new ApiResponse("Username already exists", null,ErrorUser.USERNAME_EXISTS));
        }
        if (dto.getEmail() != null && userRepository.existsByEmailAndIdNot(dto.getEmail(), id)) {
            return ResponseEntity.badRequest().body(new ApiResponse("Email already exists", null, ErrorUser.EMAIL_EXISTS));
        }
        if (dto.getPhone() != null && userRepository.existsByPhoneAndIdNot(dto.getPhone(), id)) {
            return ResponseEntity.badRequest().body(new ApiResponse("Phone already exists", null, ErrorUser.PHONE_EXISTS));
        }

        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        try {
            UserStatus status = UserStatus.valueOf(dto.getStatus().toUpperCase());
            if (!(status == UserStatus.ACTIVE || status == UserStatus.INACTIVE)) {
                return ResponseEntity.badRequest().body(new ApiResponse("Invalid status", null, ErrorUser.INVALID_USER_STATUS));
            }
            if (dto.getStatus() != null) user.setStatus(status);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse("Invalid status", null, ErrorUser.INVALID_USER_STATUS));
        }


        try {
            UserRole role = UserRole.valueOf(dto.getRole().toUpperCase());
            if (!(role == UserRole.USER || role == UserRole.ADMIN)) {
                return ResponseEntity.badRequest().body(new ApiResponse("Invalid role", null, ErrorUser.INVALID_USER_ROLE));
            }
            if (dto.getRole() != null) user.setRole(role);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse("Invalid ROLE", null, ErrorUser.INVALID_USER_ROLE));
        }

        if (dto.getUsername() != null) user.setUsername(dto.getUsername());
        if (dto.getPassword() != null) user.setPassword(dto.getPassword());
        if (dto.getPhone() != null) user.setPhone(dto.getPhone());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getFullName() != null) user.setFullName(dto.getFullName());

        userRepository.save(user);
        return ResponseEntity.ok(new ApiResponse("User updated successfully", null, "SUCCESS"));
    }


    @Override
    public ResponseEntity<ApiResponse> deleteUser(int id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null){
            return  ResponseEntity.badRequest().body(new ApiResponse("The user does not exist.",null,ErrorUser.USER_NOT_FOUND));
        }
        userRepository.deleteById(user.getId());
        return ResponseEntity.ok(new ApiResponse("User deleted successfully", null, "SUCCESS"));
    }

    @Override
    public ResponseEntity<ApiResponse> searchUsers(String keyword, Pageable pageable) {
        Page<User> users = userRepository.searchUsers(keyword, pageable);
        List<UserResponseDto> userDtos = users.getContent().stream()
                .map(user -> modelMapper.map(user, UserResponseDto.class))
                .toList();
        PagedResponse<UserResponseDto> pagedResponse = new PagedResponse<>(userDtos, users);
        return ResponseEntity.ok(new ApiResponse("Search User", pagedResponse, "SUCCESS"));
    }

    @Override
    public ResponseEntity<ApiResponse> getUserById (int id){
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(new ApiResponse("User found",modelMapper.map(user,UserResponseDto.class),"SUCCESS"));
    }

    @Override
    public ResponseEntity<ApiResponse> updateTheRoleForTheAccount(UserRole role, int id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setRole(role);
        userRepository.save(user);
        String messageText;
        if (role == UserRole.STAFF) {
            messageText = "Your account has been updated to the employee role.";
        } else {
            messageText = "Your account has been updated to the user role.";
        }
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(user.getEmail());
        msg.setSubject("Update account roles");
        msg.setText(messageText);
        mailSender.send(msg);
        return ResponseEntity.ok(new ApiResponse("Account role update successful.", null, "SUCCESS"));
    }


    @Override
    public ResponseEntity<ApiResponse> updateAccountStatus(int id, UserStatus status) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        user.setStatus(status);
        userRepository.save(user);
        String messageText = null;
        if (status == UserStatus.SUSPENDED) {
            messageText = "Your account has been banned.";
        } else if (status == UserStatus.ACTIVE) {
            messageText = "Your account has been activated.";
        } else if (status == UserStatus.INACTIVE) {
            messageText = "Your account is pending approval.";
        }
        if (messageText != null) {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(user.getEmail());
            msg.setSubject("Update account status");
            msg.setText(messageText);
            mailSender.send(msg);
        }
        return ResponseEntity.ok(new ApiResponse("Account status update successful", null, "SUCCESS"));
    }



}
