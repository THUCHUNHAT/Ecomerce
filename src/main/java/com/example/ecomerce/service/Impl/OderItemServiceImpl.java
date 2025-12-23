package com.example.ecomerce.service.Impl;

import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.dto.*;
import com.example.ecomerce.entity.*;
import com.example.ecomerce.enums.Error;
import com.example.ecomerce.repository.OderRepository;
import com.example.ecomerce.repository.OrderItemRepository;
import com.example.ecomerce.repository.ProductRepository;
import com.example.ecomerce.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.util.List;


@Service
public class OderItemServiceImpl implements OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ResponseEntity<ApiResponse> getOderItemById(int id) {
        OrderItem orderitem = orderItemRepository.findById(id).orElseThrow(() -> new RuntimeException("OrderItem not found"));
        return ResponseEntity.ok(new ApiResponse("OrderItem found", modelMapper.map(orderitem, OrderItemResponseDto.class),"SUCCESS"));

    }

    @Override
    public ResponseEntity<ApiResponse> getAllOrderItems(Pageable pageable) {
        Page<OrderItem> orderItems = orderItemRepository.findAll(pageable);

        List<OrderItemResponseDto> oderItemDto = orderItems.getContent().stream().map(orderItem -> modelMapper.map(orderItem, OrderItemResponseDto.class)).toList();

        PagedResponse<OrderItemResponseDto> pagedResponse = new PagedResponse<>(oderItemDto, orderItems);

        return ResponseEntity.ok(new ApiResponse("List OrderItem", pagedResponse, "SUCCESS"));
    }

    @Override
    public ResponseEntity<ApiResponse> addOrderItem(OrderItemRequestDto orderItemRequestDto) {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse> updateOrderItem(int id, OrderItemRequestDto orderItemRequest) {
        return null;
    }


    public ResponseEntity<ApiResponse> deleteOrderItem(int id) {
        OrderItem orderItem = orderItemRepository.findById(id).orElseThrow(() -> new RuntimeException("OrderItem not found"));
        orderItemRepository.deleteById(id);
        return ResponseEntity.ok(new ApiResponse("OrderItem deleted successfully", null, "SUCCESS"));
    }

    @Override
    public ResponseEntity<ApiResponse> searchOrderItem(String keyword, Pageable pageable) {
        Page<OrderItem> orderItems = orderItemRepository.searchOrderItem(keyword, pageable);
        List<OrderItemResponseDto> oderItemDto = orderItems.getContent().stream().map(orderItem -> modelMapper.map(orderItem, OrderItemResponseDto.class)).toList();
        PagedResponse<OrderItemResponseDto> pagedResponse = new PagedResponse<>(oderItemDto, orderItems);
        return ResponseEntity.ok(new ApiResponse("List OrderItem", pagedResponse, "SUCCESS"));
    }


}
