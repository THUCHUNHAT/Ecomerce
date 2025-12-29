package com.example.ecomerce.service.Impl;

import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.dto.SubscriberRequestDto;
import com.example.ecomerce.entity.Subscriber;
import com.example.ecomerce.repository.SubscriberRepository;
import com.example.ecomerce.service.SubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SubscriberServiceImpl implements SubscriberService {
    @Autowired
    private SubscriberRepository subscriberRepository;

    @Override
    public ResponseEntity<ApiResponse> Subscriber (SubscriberRequestDto subscriberRequestDto) {
        try {
            Subscriber subscriber = new Subscriber();
            subscriber.setEmail(subscriberRequestDto.getEmail());
            subscriber.setCreatedAt(LocalDateTime.now());
            subscriberRepository.save(subscriber);
            return ResponseEntity.ok(new ApiResponse("Subscriber added successfully", null, "SUCCESS"));
        } catch (Exception e){
            return ResponseEntity.badRequest().body(new ApiResponse("Something went wrong", null, "ERROR"));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> unSubscriber (String email){
        try {
            Subscriber subscriber = subscriberRepository.findByEmail(email);
            if (subscriber != null) {
                subscriberRepository.delete(subscriber);
                return ResponseEntity.ok(new ApiResponse("Unsubscribed successfully", null, "SUCCESS"));
            }

        } catch (Exception e){
            return ResponseEntity.badRequest().body(new ApiResponse("Something went wrong", null, "ERROR"));
        }
        return null;
    }


}
