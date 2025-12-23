package com.example.ecomerce.service;

import com.example.ecomerce.dto.MonthlySalesDto;
import org.springframework.stereotype.Service;

import java.util.List;


public interface DashboardService {
    Double getCurrentMonthRevenue();
    Integer getTotalOrders();
    Integer getTotalProducts();
    Integer getTotalUsers();
    List<MonthlySalesDto> getSalesAnalytics(int year);
    Integer getOrderCancelled();
    Integer getOrderShipped();
}
