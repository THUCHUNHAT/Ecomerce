package com.example.ecomerce.service;

import com.example.ecomerce.dto.MailRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendMail(MailRequestDto mailRequestDto) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(mailRequestDto.getTo());
        msg.setSubject(mailRequestDto.getSubject());
        msg.setText(mailRequestDto.getContent());
        mailSender.send(msg);
    }

}
