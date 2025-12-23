package com.example.ecomerce.service.Impl;
import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.dto.UserRequestDto;
import com.example.ecomerce.entity.User;
import com.example.ecomerce.entity.Verification;
import com.example.ecomerce.enums.Error;
import com.example.ecomerce.enums.TokenType;
import com.example.ecomerce.enums.UserRole;
import com.example.ecomerce.enums.UserStatus;
import com.example.ecomerce.exception.ErrorUser;
import com.example.ecomerce.exception.ErrorVerificationToken;
import com.example.ecomerce.repository.UserRepository;
import com.example.ecomerce.repository.VerificationTokenRepository;
import com.example.ecomerce.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;



    @Override
    public ResponseEntity<ApiResponse> registerUser(UserRequestDto userRequestDto){
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


        //lưu ở trạng thái inactive
        user.setEmail(userRequestDto.getEmail());
        user.setPhone(userRequestDto.getPhone());
        user.setFullName(userRequestDto.getFullName());
        user.setUsername(userRequestDto.getUsername());
        user.setPassword(userRequestDto.getPassword());
        UserRole role = UserRole.USER;
        user.setRole(role);
        UserStatus status = UserStatus.INACTIVE;
        user.setStatus(status);
        User saveUser= userRepository.save(user);


        //gửi mail
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(userRequestDto.getEmail());
        msg.setSubject("confirmation code");
        Random random = new Random();
        int code = 10000 + random.nextInt(90000);
        msg.setText("Your confirmation code is: " + code);


        Verification verification = new Verification();
        verification.setUser(saveUser);
        verification.setToken(String.valueOf(code));
        verification.setExpiryDate(LocalDateTime.now().plusMinutes(30));
        TokenType tokenType = TokenType.REGISTER;
        verification.setTokenType(tokenType);
        verificationTokenRepository.save(verification);
        mailSender.send(msg);
        return ResponseEntity.ok(new ApiResponse("You have received your account confirmation code.", null, "SUCCESS"));
    }


    @Override
    public ResponseEntity<ApiResponse> tokenConfirmation(String token) {
        Verification verification = verificationTokenRepository.findByToken(token).orElse(null);
        if (verification == null) {
            return ResponseEntity.badRequest().body(new ApiResponse("Token not found", null, ErrorVerificationToken.INVALID_TOKEN));
        }
        User user = verification.getUser();
        if (user == null) {
            return ResponseEntity.badRequest().body(new ApiResponse("Token not linked to any user", null, ErrorUser.USER_NOT_FOUND));
        }
//        if(verification.getExpiryDate().isBefore(LocalDateTime.now())){
//            verificationTokenRepository.delete(verification);
//            return ResponseEntity.badRequest().body(new ApiResponse("Confirmation code expired, please click resend to receive a new code.",null,ErrorVerificationToken.INVALID_TOKEN));
//        }
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);
        verificationTokenRepository.delete(verification);
        return ResponseEntity.ok(new ApiResponse("Success", null, "SUCCESS"));
    }


    @Override
    public ResponseEntity<ApiResponse> resendCode (String email){
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty() || userOptional.get().getStatus() == UserStatus.ACTIVE) {
            return ResponseEntity.badRequest().body(new ApiResponse("Email not found or already active", null, ErrorUser.EMAIL_EXISTS));
        }
        User user = userOptional.get();
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject("confirmation code");
        Random random = new Random();
        int code = 10000 + random.nextInt(90000);
        msg.setText("Your confirmation code is: " + code);

        Verification verification = new Verification();
        verification.setUser(user);
        verification.setToken(String.valueOf(code));
        verification.setExpiryDate(LocalDateTime.now().plusMinutes(30));
        TokenType tokenType = TokenType.REGISTER;
        verification.setTokenType(tokenType);
        verificationTokenRepository.save(verification);
        mailSender.send(msg);
        return  ResponseEntity.ok(new ApiResponse("Resend code successfully",null,"SUCCESS"));
    }

    @Scheduled(cron = "0 0 2 * * ?")
    public void deleteAllByStatus(){
        userRepository.deleteAllByStatus( UserStatus.INACTIVE);
        System.out.println("Delete user with inactive status successfully");
    }
}
