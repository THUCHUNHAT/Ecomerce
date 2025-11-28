package com.example.ecomerce.service.Impl;

import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.dto.*;
import com.example.ecomerce.entity.*;
import com.example.ecomerce.repository.OderRepository;
import com.example.ecomerce.repository.OrderItemRepository;
import com.example.ecomerce.repository.UserRepository;
import com.example.ecomerce.service.OrderService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ResponseEntity<ApiResponse> getAllOrder(Pageable pageable){
        Page<Order> orders = orderRepository.findAll(pageable);
        List<OderResponseDto> orderDtos = orders.getContent().stream()
                .map(order -> modelMapper.map(order, OderResponseDto.class))
                .toList();
        PagedResponse<OderResponseDto> pagedResponse = new PagedResponse<>(orderDtos, orders);
        return ResponseEntity.ok(new ApiResponse("List Order", pagedResponse, "Success"));
    }

    @Override
    public ResponseEntity<ApiResponse> getOrderDetails(int id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        OrderDetailResponseDto orderDto = modelMapper.map(order, OrderDetailResponseDto.class);
        List<OrderItem> orderItems = orderItemRepository.findByOrder(order);
        List<OrderItemResponseDto> itemDtos = orderItems.stream().map(item -> modelMapper.map(item, OrderItemResponseDto.class)).collect(Collectors.toList());
        orderDto.setItems(itemDtos);
        return ResponseEntity.ok(new ApiResponse("Order detail", orderDto, "Success"));
    }




    @Override
    public ResponseEntity<ApiResponse> addOrder (OderRequestDto oderRequestDto){
        User user = userRepository.findById(oderRequestDto.getUserId()).orElseThrow(() -> new RuntimeException("user not found"));
        Order order = new Order();
        order.setOrderCode(oderRequestDto.getOrderCode());
        order.setStatus(oderRequestDto.getStatus());
        order.setTotalAmount(oderRequestDto.getTotalAmount());
        order.setShippingAddress(oderRequestDto.getShippingAddress());
        order.setPaymentMethod(oderRequestDto.getPaymentMethod());
        order.setOrderDate(oderRequestDto.getOrderDate());
        order.setUser(user);
        orderRepository.save(order);
        return ResponseEntity.ok(new ApiResponse("Order added successfully", null, "Success"));
    }




    @Override
    public ResponseEntity<ApiResponse> updateOrder(int id, UpdateStatusOrderDto updateStatusOrderDto){
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        order.setStatus(updateStatusOrderDto.getStatus());
        orderRepository.save(order);
        return ResponseEntity.ok(new ApiResponse("Order updated successfully", null, "Success"));
    }

    @Override
    public ResponseEntity<ApiResponse> deleteOrder(int id) {
        return null;
    }

}
