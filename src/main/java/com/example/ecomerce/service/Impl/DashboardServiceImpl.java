package com.example.ecomerce.service.Impl;

import com.example.ecomerce.dto.MonthlySalesDto;
import com.example.ecomerce.repository.OderRepository;
import com.example.ecomerce.repository.ProductRepository;
import com.example.ecomerce.repository.UserRepository;
import com.example.ecomerce.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private OderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Double getCurrentMonthRevenue() {
        Double revenue = orderRepository.getCurrentMonthRevenue();
        return revenue != null ? revenue : 0.0;
    }

    @Override
    public List<MonthlySalesDto> getSalesAnalytics(int year){
//        List<Object[]> rawData = orderRepository.getSalesAnalytics(year);
//        List<Long> monthlyOrders = new ArrayList<>(Collections.nCopies(12, 0L));
//        for (Object[] row : rawData) {
//            Integer month = (Integer) row[0];       // tháng (1–12)
//            Long totalOrders = (Long) row[1];       // số đơn hàng
//            monthlyOrders.set(month - 1, totalOrders);
//        }
        return null;
    }

    @Override
    public Integer getTotalOrders() {
        Integer totalOrders = orderRepository.getTotalOrders();
        return totalOrders != null ? totalOrders : 0;
    }

    @Override
    public Integer getTotalProducts() {
        Integer totalProducts = productRepository.getTotalProducts();
        return totalProducts != null ? totalProducts : 0;
    }

    @Override
    public Integer getTotalUsers() {
        Integer totalUsers = userRepository.getTotalUsers();
        return totalUsers != null ? totalUsers : 0;
    }

    @Override
    public Integer getOrderCancelled(){
        Integer orderCalceled = orderRepository.getOrderCancelled();
        return orderCalceled != null ? orderCalceled : 0;
    }

    @Override
    public Integer getOrderShipped(){
        Integer orderShipped = orderRepository.getOrderShipped();
        return orderShipped != null ? orderShipped : 0;
    }
}
