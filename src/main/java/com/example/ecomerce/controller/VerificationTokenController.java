package com.example.ecomerce.controller;
import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.service.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/verification")
public class VerificationTokenController {

    @Autowired
    private VerificationTokenService verificationTokenService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllTokens (
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        return  verificationTokenService.getAllTokens(pageable);
    }

    @DeleteMapping ("/{id}")
    public ResponseEntity<ApiResponse> deleteToken (@PathVariable int id){
        return verificationTokenService.deleteToken(id);
    }

}
