package com.example.ecomerce.service.Impl;

import com.example.ecomerce.repository.OderRepository;
import com.example.ecomerce.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;

public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private OderRepository orderRepository;

    @Override
    public Double getCurrentMonthRevenue() {
        Double revenue = orderRepository.getCurrentMonthRevenue();
        return revenue != null ? revenue : 0.0;
    }
}
