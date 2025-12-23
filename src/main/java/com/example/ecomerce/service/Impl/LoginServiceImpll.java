package com.example.ecomerce.service.Impl;
import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.dto.LoginDto;
import com.example.ecomerce.dto.LoginResponseDto;
import com.example.ecomerce.entity.User;
import com.example.ecomerce.enums.Error;
import com.example.ecomerce.enums.UserRole;
import com.example.ecomerce.enums.UserStatus;
import com.example.ecomerce.exception.ErrorUser;
import com.example.ecomerce.repository.UserRepository;
import com.example.ecomerce.security.JwtTokenProvider;
import com.example.ecomerce.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class LoginServiceImpll implements LoginService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public ResponseEntity<ApiResponse> login(LoginDto loginDto) {
        User user = userRepository.findByUsername(loginDto.getUsername()).orElse(null);

        if (user == null) {
            return ResponseEntity.badRequest().body(
                    new ApiResponse("Username does not exist", null, ErrorUser.USERNAME_EXISTS)
            );
        }
        if (!Objects.equals(loginDto.getPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body(
                    new ApiResponse("Wrong password", null, ErrorUser.WRONG_PASSWORD)
            );
        }

        UserStatus status = user.getStatus();
        if (status == UserStatus.INACTIVE) {
            return ResponseEntity.badRequest().body(new ApiResponse("unlicensed account", null, ErrorUser.INACTIVE));

        }
        if (status == UserStatus.SUSPENDED) {
            return ResponseEntity.badRequest().body(new ApiResponse("account is banned", null, ErrorUser.SUSPENDED));
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("username", user.getUsername());
        claims.put("password", user.getPassword());
        claims.put("role",user.getRole());

        String token = jwtTokenProvider.generateToken(user.getUsername(), claims);
        LoginResponseDto loginResponse = new LoginResponseDto(token, user.getUsername(),user.getRole());

        UserRole role = user.getRole();
        if (role == UserRole.ADMIN) {
            return ResponseEntity.ok(new ApiResponse("Admin login successful", loginResponse, "SUCCESS"));
        }

        return ResponseEntity.ok(new ApiResponse("User logged in successfully", loginResponse, "SUCCESS"));
    }


    @Override
    public Object user(Principal principal) {
        if (principal == null) {
            System.out.println("Principal is null");
            return null;
        }

        OAuth2AuthenticationToken auth = (OAuth2AuthenticationToken) principal;
        OAuth2User oauthUser = auth.getPrincipal();
        String email = oauthUser.getAttribute("email");
        String username = email.split("@")[0];

        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setStatus(UserStatus.ACTIVE);
            user.setRole(UserRole.USER);
            userRepository.save(user);
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        claims.put("email", email);

        String token = jwtTokenProvider.generateToken(username, claims);
        LoginResponseDto loginResponse = new LoginResponseDto(token, username, user.getRole());

        return ResponseEntity.ok(
                new ApiResponse("User logged in with google successfully", loginResponse, "SUCCESS")
        );
    }

    public LoginServiceImpll() {
    }


}
