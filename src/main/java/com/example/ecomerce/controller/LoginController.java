package com.example.ecomerce.controller;
import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.dto.LoginDto;
import com.example.ecomerce.enums.Error;
import com.example.ecomerce.service.Impl.LoginServiceImpll;
import com.example.ecomerce.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private LoginService loginService;

    @Autowired
    private LoginServiceImpll loginServiceImpll;
    @PostMapping
    public ResponseEntity<ApiResponse> login ( @RequestBody LoginDto loginDto){
        return  loginService.login(loginDto);
    }

    @GetMapping ("/")
    public ResponseEntity<String> home() {
        return ResponseEntity.ok("API is running");
    }

    @GetMapping
    public ResponseEntity<String> success (){
        return ResponseEntity.ok("login with gooogle success");
    }

    @GetMapping("/google")
    public ResponseEntity<ApiResponse> loginWithGoogle(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse("Google login failed", null, "SUCCESS"));
        }

        String email = principal.getAttribute("email");

        // xử lý logic lưu user hoặc tạo token
        return ResponseEntity.ok(new ApiResponse("Login with Google success", email, "SUCCESS"));
    }



}
