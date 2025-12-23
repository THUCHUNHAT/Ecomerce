package com.example.ecomerce.controller;
import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.dto.OderRequestDto;
import com.example.ecomerce.dto.OrderFilter;
import com.example.ecomerce.dto.UpdateStatusOrderDto;
import com.example.ecomerce.enums.OrderStatus;
import com.example.ecomerce.enums.PaymentMethod;
import com.example.ecomerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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


    @GetMapping("/{id}/details")
    public ResponseEntity<ApiResponse> getOrderDetails(@PathVariable int id) {
        return orderService.getOrderDetails(id);
    }

//    @GetMapping("/search")
//    public ResponseEntity<ApiResponse> searchOrders(@RequestParam String keyword, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        return orderService.searchOrder(keyword, pageable);
//    }

    @PostMapping ("/filter")
    public ResponseEntity<ApiResponse> filterOrders(@RequestBody OrderFilter orderFilter,Pageable pageable){
        return orderService.filter(orderFilter, pageable);
    }

    @GetMapping("/{id}" )
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable int id){
        return orderService.getOrderById(id);
    }

    @PostMapping ("/user")
    public ResponseEntity<ApiResponse> createOrder(@RequestBody OderRequestDto orderRequestDto, Authentication authentication) {
        return orderService.createOrder(orderRequestDto,authentication);
    }

    @PutMapping ("/{id}/status/{orderStatus}")
    public ResponseEntity <ApiResponse> UpdateOrderStatus (@PathVariable OrderStatus orderStatus, @PathVariable int id){
        return orderService.updateOrderStatus(orderStatus,id);
    }

    @PutMapping ("/{id}/{paymentMethod}")
    public ResponseEntity<ApiResponse> UpdateThePaymentMethodForTheOrder (@PathVariable PaymentMethod paymentMethod,@PathVariable int id){
        return orderService.UpdateThePaymentMethodForTheOrder(id,paymentMethod);
    }

}
