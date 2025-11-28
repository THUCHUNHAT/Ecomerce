package com.example.ecomerce.controller;

import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.dto.OderRequestDto;
import com.example.ecomerce.dto.UpdateStatusOrderDto;
import com.example.ecomerce.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllOrder(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return orderService.getAllOrder(pageable);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getOrderDetails(@PathVariable int id) {
        return orderService.getOrderDetails(id);
    }

    @PostMapping
    public ResponseEntity<ApiResponse> addOrder(@RequestBody  OderRequestDto oderRequestDto) {
        return orderService.addOrder(oderRequestDto);
    }

    @PutMapping ("/{id}")
    public ResponseEntity<ApiResponse> updateOrder(@PathVariable  int id, @RequestBody UpdateStatusOrderDto updateStatusOrderDto){
        return orderService.updateOrder(id, updateStatusOrderDto);
    }

}
