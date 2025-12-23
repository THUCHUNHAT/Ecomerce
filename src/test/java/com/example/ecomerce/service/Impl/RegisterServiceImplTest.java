package com.example.ecomerce.service.Impl;

import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.dto.UserRequestDto;
import com.example.ecomerce.entity.User;
import com.example.ecomerce.entity.Verification;
import com.example.ecomerce.enums.Error;
import com.example.ecomerce.enums.TokenType;
import com.example.ecomerce.enums.UserStatus;
import com.example.ecomerce.repository.UserRepository;
import com.example.ecomerce.repository.VerificationTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private VerificationTokenRepository verificationTokenRepository;

    @InjectMocks
    private RegisterServiceImpl registerService;

    private UserRequestDto userRequestDto;
    private User user;

    @BeforeEach
    void setUp() {
        userRequestDto = new UserRequestDto(
                "testuser",
                "password123",
                "test@example.com",
                "John Doe",
                "1234567890"
        );
        user = new User();
        user.setId(1);
        user.setUsername(userRequestDto.getUsername());
        user.setEmail(userRequestDto.getEmail());
        user.setStatus(UserStatus.INACTIVE);
    }

    @Test
    void registerUser_Success() {
        // GIVEN
        when(userRepository.existsByUsername(userRequestDto.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(userRequestDto.getEmail())).thenReturn(false);
        when(userRepository.existsByPhone(userRequestDto.getPhone())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // WHEN
        ResponseEntity<ApiResponse> response = registerService.registerUser(userRequestDto);

        // THEN
        assertEquals(HttpStatus.OK, response.getStatusCode());

        ApiResponse body = response.getBody();
        assertNotNull(body);

        assertEquals(Error.SUCCESS, body.getErrorCode());
        assertEquals("Success", body.getMessage());


        // Kiểm tra xem user đã được lưu với trạng thái INACTIVE chưa
        verify(userRepository, times(1)).save(any(User.class));
        // Kiểm tra xem Verification token đã được lưu chưa
        verify(verificationTokenRepository, times(1)).save(any(Verification.class));
        // Kiểm tra xem email đã được gửi chưa
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}