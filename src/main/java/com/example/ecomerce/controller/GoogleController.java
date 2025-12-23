package com.example.ecomerce.controller;
import com.example.ecomerce.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class GoogleController {

    @Autowired
    private LoginService loginService;

    @GetMapping
    public String welcome(){
        return "Sign in to PastelStore\n" +
                "with your Google account";
    }


    @GetMapping("/auth/google")
    public Object user(Principal principal) {
        return loginService.user(principal);
    }
}
