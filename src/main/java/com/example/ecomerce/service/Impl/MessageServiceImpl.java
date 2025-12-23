package com.example.ecomerce.service.Impl;

import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.dto.MessageRequestDto;
import com.example.ecomerce.dto.MessageResponseDto;
import com.example.ecomerce.entity.Message;
import com.example.ecomerce.entity.User;
import com.example.ecomerce.exception.ErrorUser;
import com.example.ecomerce.repository.MessageRepository;
import com.example.ecomerce.repository.UserRepository;
import com.example.ecomerce.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

   @Autowired
   private MessageRepository messageRepository;

   @Autowired
   private UserRepository userRepository;

   @Override
    public ResponseEntity<ApiResponse> sendMessage (MessageRequestDto messageRequestDto, Authentication authentication){
       try {
           if (authentication == null) {
               return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse("User not authenticated", null, ErrorUser.USER_NOT_AUTHENTICATED));
           }
           String username = authentication.getName();
           User user = userRepository.findByUsername(username).orElse(null);
           if (user == null){
               return ResponseEntity.badRequest().body(new ApiResponse("user not found",null,"User_NOT_FOUND"));
           }

           Message message = new Message();
           message.setUser(user);
           message.setConversationId(messageRequestDto.getConversationId());
           message.setContent(messageRequestDto.getContent());
           message.setCreateAt(LocalDateTime.now());
           messageRepository.save(message);
           return ResponseEntity.ok(new ApiResponse("Message sent successfully",null,"Success"));
       }catch (Exception e) {
           return ResponseEntity.badRequest().body(new ApiResponse("Something went wrong",null,"Error"));
       }

    }

    @Override
    public ResponseEntity<ApiResponse> getMessage ( Authentication authentication) {

        try {
            if (authentication == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse("User not authenticated", null, ErrorUser.USER_NOT_AUTHENTICATED));
            }
            String username = authentication.getName();
            User user = userRepository.findByUsername(username).orElse(null);
            if (user == null) {
                return ResponseEntity.badRequest().body(new ApiResponse("user not found", null, "USER_NOT_FOUND"));
            }
            List<Message> message = messageRepository.findByUser(user);
            if (message.isEmpty()) {
                return ResponseEntity.badRequest().body(new ApiResponse("message not found", null, "MESSAGE_NOT_FOUND"));
            }

            List<MessageResponseDto> messageResponseDtos = message.stream() .map(msg -> { MessageResponseDto dto = new MessageResponseDto(); dto.setUser(user); dto.setContent(msg.getContent()); dto.setCreateAt(msg.getCreateAt()); return dto; }) .toList();
            return ResponseEntity.ok(new ApiResponse("show message success", messageResponseDtos, "Success"));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse("Something went wrong", null, "Error"));
        }

    }


}
