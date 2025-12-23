package com.example.ecomerce.controller;

import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.service.DashboardService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")

public class DashboardController {
    @Autowired
    private  DashboardService dashboardService;

    @GetMapping("/current-month")
    public ResponseEntity<Double> getCurrentMonthRevenue() {
        return ResponseEntity.ok(dashboardService.getCurrentMonthRevenue());
    }

    @GetMapping ("/toltal-order")
    public ResponseEntity <Integer> getTotalOrders(){
        return ResponseEntity.ok(dashboardService.getTotalOrders());
    }

    @GetMapping ("/total-product")
    public ResponseEntity<Integer> getTotalProducts(){
        return ResponseEntity.ok(dashboardService.getTotalProducts());
    }

    @GetMapping ("/total-user")
    public ResponseEntity<Integer> getTotalUsers() {
        return ResponseEntity.ok(dashboardService.getTotalUsers());
    }

//    @GetMapping ("/salesanalytics")
//    public ResponseEntity<ApiResponse> getSalesAnalytics( int year) {
//        return dashboardService.getSalesAnalytics(year);
//    }

    @GetMapping ("/totalordercanceled")
    public ResponseEntity<Integer> getTotalOrderCanceled(){
        return ResponseEntity.ok(dashboardService.getOrderCancelled());
    }


    @GetMapping("/totalordershipped")
    public ResponseEntity<Integer> getTotalOrderShipped(){
        return  ResponseEntity.ok(dashboardService.getOrderShipped());
    }
}
