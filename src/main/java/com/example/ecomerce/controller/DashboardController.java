package com.example.ecomerce.controller;

import com.example.ecomerce.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/api/dashboard/revenue/current-month")
    public ResponseEntity<Double> getCurrentMonthRevenue() {
        return ResponseEntity.ok(dashboardService.getCurrentMonthRevenue());
    }
}
