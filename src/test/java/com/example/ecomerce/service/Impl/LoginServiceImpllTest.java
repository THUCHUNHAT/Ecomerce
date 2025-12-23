package com.example.ecomerce.service.Impl;

import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.dto.LoginDto;
import com.example.ecomerce.entity.User;
import com.example.ecomerce.enums.Error;
import com.example.ecomerce.enums.UserRole;
import com.example.ecomerce.enums.UserStatus;
import com.example.ecomerce.repository.UserRepository;
import com.example.ecomerce.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoginServiceImpllTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private LoginServiceImpll loginServiceImpll;

    private LoginDto loginDto;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        loginDto = new LoginDto("testuser", "password123");
        user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setStatus(UserStatus.ACTIVE);
        user.setRole(UserRole.USER);
    }

    @Test
    void testLogin_usernameNotExist() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        ResponseEntity<ApiResponse> response = loginServiceImpll.login(loginDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Username does not exist", response.getBody().getMessage());
        assertEquals(Error.USERNAME_EXISTS, response.getBody().getErrorCode());
    }

    @Test
    void testLogin_wrongPassword() {
        user.setPassword("wrongpass");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        ResponseEntity<ApiResponse> response = loginServiceImpll.login(loginDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Wrong password", response.getBody().getMessage());
        assertEquals(Error.WRONG_PASSWORD, response.getBody().getErrorCode());
    }

    @Test
    void testLogin_inactiveUser() {
        user.setStatus(UserStatus.INACTIVE);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        ResponseEntity<ApiResponse> response = loginServiceImpll.login(loginDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("unlicensed account", response.getBody().getMessage());
        assertEquals(Error.INACTIVE, response.getBody().getErrorCode());
    }

    @Test
    void testLogin_suspendedUser() {
        user.setStatus(UserStatus.SUSPENDED);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        ResponseEntity<ApiResponse> response = loginServiceImpll.login(loginDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("account is banned", response.getBody().getMessage());
        assertEquals(Error.SUSPENDED, response.getBody().getErrorCode());
    }

    @Test
    void testLogin_adminSuccess() {
        user.setRole(UserRole.ADMIN);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(jwtTokenProvider.generateToken(anyString(), anyMap())).thenReturn("mockToken");

        ResponseEntity<ApiResponse> response = loginServiceImpll.login(loginDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Admin login successful", response.getBody().getMessage());
        assertEquals(Error.SUCCESS, response.getBody().getErrorCode());
        assertNotNull(response.getBody().getData());
    }

    @Test
    void testLogin_userSuccess() {
        user.setRole(UserRole.USER);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(jwtTokenProvider.generateToken(anyString(), anyMap())).thenReturn("mockToken");

        ResponseEntity<ApiResponse> response = loginServiceImpll.login(loginDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User logged in successfully", response.getBody().getMessage());
        assertEquals(Error.SUCCESS, response.getBody().getErrorCode());
        assertNotNull(response.getBody().getData());
    }
}
