package com.example.ecomerce.controller;

import com.example.ecomerce.dto.MailRequestDto;
import com.example.ecomerce.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mail")
public class MailController {
    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public String sendMail(@RequestBody MailRequestDto request) {
        emailService.sendMail(request);
        return "Email sent successfully";
    }


}
