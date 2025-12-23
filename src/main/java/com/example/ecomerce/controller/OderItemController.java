package com.example.ecomerce.controller;
import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.dto.OderRequestDto;
import com.example.ecomerce.dto.OrderItemRequestDto;
import com.example.ecomerce.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/orderitem")
public class OderItemController {
    @Autowired
    private OrderItemService orderItemService;

    @GetMapping ("/{id}")
    public ResponseEntity<ApiResponse> getOrderItemById( int id){
        return orderItemService.getOderItemById(id);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllOrderItems(Pageable pageable){
        return orderItemService.getAllOrderItems(pageable);
    }

    @PostMapping
    public ResponseEntity<ApiResponse> addOrderItem(OrderItemRequestDto orderItemRequestDto){
        return orderItemService.addOrderItem(orderItemRequestDto);
    }

    @PutMapping ("/{id}")
    public ResponseEntity<ApiResponse> updateOrderItem(@PathVariable  int id, @RequestBody OrderItemRequestDto orderItemRequest){
        return orderItemService.updateOrderItem(id, orderItemRequest);
    }

    @DeleteMapping ("/{id}")
    public ResponseEntity<ApiResponse> deleteOrderItem(@PathVariable  int id) {
        return orderItemService.deleteOrderItem(id);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> searchOrderItem(@RequestParam String keyword, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return orderItemService.searchOrderItem(keyword, pageable);
    }
}
