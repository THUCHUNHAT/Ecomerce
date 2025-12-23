package com.example.ecomerce.service.Impl;

import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.dto.ConFirmPassWordChangeCodeDto;
import com.example.ecomerce.dto.ForgotPassWordDto;
import com.example.ecomerce.dto.NewPasswordDto;
import com.example.ecomerce.entity.User;
import com.example.ecomerce.entity.Verification;
import com.example.ecomerce.enums.Error;
import com.example.ecomerce.enums.TokenType;
import com.example.ecomerce.enums.UserStatus;
import com.example.ecomerce.exception.ErrorUser;
import com.example.ecomerce.exception.ErrorVerificationToken;
import com.example.ecomerce.repository.UserRepository;
import com.example.ecomerce.repository.VerificationTokenRepository;
import com.example.ecomerce.service.ForgotPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class ForgotPasswordServiceImpl implements ForgotPasswordService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;


    @Override
    public ResponseEntity<ApiResponse> forgotPassword(ForgotPassWordDto forgotPassWordDto) {
        User user = userRepository.findByEmail(forgotPassWordDto.getEmail()).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body(new ApiResponse("Email does not exist", null, ErrorUser.EMAIL_EXISTS));
        }


        user.setStatus(UserStatus.SUSPENDED);
        userRepository.save(user);
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(forgotPassWordDto.getEmail());
        msg.setSubject("password change confirmation code");
        Random random = new Random();
        int code = 10000 + random.nextInt(90000);
        msg.setText("Your confirmation code is: " + code);

        Verification verification = new Verification();
        verification.setUser(user);
        verification.setToken(String.valueOf(code));
        verification.setExpiryDate(LocalDateTime.now());
        TokenType tokenType = TokenType.RESET_PASSWORD;
        verification.setTokenType(tokenType);
        verificationTokenRepository.save(verification);
        mailSender.send(msg);
        return ResponseEntity.ok(new ApiResponse("You have received the password change code.", null, "SUCCESS"));
    }

    @Override
    public ResponseEntity<ApiResponse> conFirmPassWordChangeCode(ConFirmPassWordChangeCodeDto conFirmPassWordChangeCodeDto) {
        Verification verification = verificationTokenRepository.findByToken(conFirmPassWordChangeCodeDto.getToken()).orElse(null);
        if (verification == null) {
            return ResponseEntity.badRequest().body(new ApiResponse("Token not found", null, ErrorVerificationToken.INVALID_TOKEN));
        }
        User user = verification.getUser();
        if (user == null) {
            return ResponseEntity.badRequest().body(new ApiResponse("Token not linked to any user", null, ErrorVerificationToken.INVALID_TOKEN));
        }
        user.setStatus(UserStatus.INACTIVE);
        userRepository.save(user);
        return ResponseEntity.ok(new ApiResponse("Confirmed successfully.", null, "SUCCESS"));
    }


    @Override
    public ResponseEntity<ApiResponse> NewPassword(NewPasswordDto newPasswordDto) {
        Verification verification = verificationTokenRepository.findByToken(newPasswordDto.getToken()).orElse(null);
        if (verification == null) {
            return ResponseEntity.badRequest().body(new ApiResponse("Token not found", null, ErrorVerificationToken.INVALID_TOKEN));
        }
        User user = verification.getUser();
        if (user == null) {
            return ResponseEntity.badRequest().body(new ApiResponse("Token not linked to any user", null, ErrorVerificationToken.INVALID_TOKEN));
        }

        user.setPassword(newPasswordDto.getPassword());
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);
        verificationTokenRepository.delete(verification);
        return ResponseEntity.ok(new ApiResponse("Password changed successfully", null, "SUCCESS"));

    }

}
