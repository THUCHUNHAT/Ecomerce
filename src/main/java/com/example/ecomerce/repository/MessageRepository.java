package com.example.ecomerce.repository;

import com.example.ecomerce.entity.Message;
import com.example.ecomerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message,Integer> {


    List<Message> findByConversationId(int conversationId);
}
