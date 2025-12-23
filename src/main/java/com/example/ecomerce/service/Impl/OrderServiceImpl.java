package com.example.ecomerce.service.Impl;
import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.dto.*;
import com.example.ecomerce.entity.*;
import com.example.ecomerce.enums.*;
import com.example.ecomerce.enums.Error;
import com.example.ecomerce.exception.ErrorOrder;
import com.example.ecomerce.repository.*;
import com.example.ecomerce.repository.specification.OrderSpecification;
import com.example.ecomerce.service.OrderService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private PromotionRepository promotionRepository;

    @Override
    public ResponseEntity<ApiResponse> getAllOrder(Pageable pageable){
        Page<Order> orders = orderRepository.findAll(pageable);
        List<OderResponseDto> orderDtos = orders.getContent().stream()
                .map(order -> modelMapper.map(order, OderResponseDto.class))
                .toList();
        PagedResponse<OderResponseDto> pagedResponse = new PagedResponse<>(orderDtos, orders);
        return ResponseEntity.ok(new ApiResponse("List Order", pagedResponse, "SUCCESS"));
    }

    @Override
    public ResponseEntity<ApiResponse> getOrderDetails(int id) {
        Order order = orderRepository.findById(id).orElse(null);
        if(order == null){
            return ResponseEntity.badRequest().body(new ApiResponse("UNDEFINED ORDER",null, ErrorOrder.UNDEFINED_ORDER));
        }
        OrderDetailResponseDto orderDto = modelMapper.map(order, OrderDetailResponseDto.class);
        List<OrderItem> orderItems = orderItemRepository.findByOrder(order);
        List<OrderItemResponseDto> itemDtos = orderItems.stream().map(item -> modelMapper.map(item, OrderItemResponseDto.class)).collect(Collectors.toList());
        orderDto.setItems(itemDtos);
        return ResponseEntity.ok(new ApiResponse("Order detail", orderDto, "SUCCESS"));
    }

//    @Override
//    public ResponseEntity<ApiResponse> searchOrder(String keyword,Pageable pageable){
//        Page<Order> orders = orderRepository.searchOrders(keyword, pageable);
//        List<OderResponseDto> orderDtos = orders.getContent().stream()
//                .map(order -> modelMapper.map(order, OderResponseDto.class))
//                .toList();
//        PagedResponse<OderResponseDto> pagedResponse = new PagedResponse<>(orderDtos, orders);
//        return ResponseEntity.ok(new ApiResponse("Search order", pagedResponse, Error.SUCCESS));
//    }

    @Override
    public ResponseEntity<ApiResponse> filter(OrderFilter filter, Pageable pageable) {
        Specification<Order> specification = Specification.where(OrderSpecification.statusEquals(filter.getStatus())).and(OrderSpecification.paymentEquals(filter.getPaymentMethod()));
        Page<Order> orders = orderRepository.findAll(specification, pageable);
        List<OderResponseDto> orderDtos = orders.getContent().stream().map(order -> modelMapper.map(order, OderResponseDto.class)).toList();
        ApiResponse response = new ApiResponse("Filtered orders retrieved", orderDtos, "SUCCESS");
        return ResponseEntity.ok(response);
    }


    @Override
    public ResponseEntity<ApiResponse> getOrderById(int id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        return  ResponseEntity.ok(new ApiResponse("Order found",modelMapper.map(order,OderResponseDto.class),"SUCCESS"));
    }


    @Override
    @Transactional
    public ResponseEntity<ApiResponse> createOrder(OderRequestDto request, Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse("User not authenticated", null, ErrorOrder.UNAUTHORIZED));
        }

        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        String orderCode = "ORD" + System.currentTimeMillis();

        Order order = new Order();
        order.setOrderCode(orderCode);
        order.setOrderDate(LocalDateTime.now());
        order.setUser(user);
        order.setShippingAddress(request.getShippingAddress());
        order.setPaymentMethod(request.getPaymentMethod());
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(BigDecimal.ZERO);
        orderRepository.save(order);
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderItemRequestDto itemReq : request.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId()).orElseThrow(() -> new RuntimeException("Product not found"));
            BigDecimal unitPrice = product.getPrice();
            BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(itemReq.getQuantity()));
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setProductName(product.getName());
            item.setUnitPrice(unitPrice);
            item.setQuantity(itemReq.getQuantity());
            item.setTotalPrice(totalPrice);
            orderItemRepository.save(item);
            order.getItems().add(item);
            totalAmount = totalAmount.add(totalPrice);
        }

        order.setTotalAmount(totalAmount);
        double phantram = 0.015;
        BigDecimal shippingFee = totalAmount.multiply(BigDecimal.valueOf(phantram));
        order.setShippingFee(shippingFee);



        Promotion promotion = promotionRepository.findByCouponCode(request.getCouponCode());


//        if (promotion != null) {
//            if (promotion.getStatus() == PromotionStatus.ACTIVE) {
//                if (promotion.getType() == PromotionType.FREESHIP) {
//                    shippingFee.multiply(promotion.getDiscountValue());
//                    int i = promotion.getUsageLimit() - 1;
//                    promotion.setUsageLimit(i);
//                    promotionRepository.save(promotion);
//
//                } else if (promotion.getType() == PromotionType.AMOUNT) {
//                    totalAmount.multiply(promotion.getDiscountValue());
//                    int i = promotion.getUsageLimit() - 1;
//                    promotion.setUsageLimit(i);
//                    promotionRepository.save(promotion);
//                    System.out.println("amout");
//                } else if (promotion.getType() == PromotionType.GIFT) {
//                    System.out.println("GIFT");
//                    int i = promotion.getUsageLimit() - 1;
//                    promotion.setUsageLimit(i);
//                    promotionRepository.save(promotion);
//                } else if (promotion.getType() == PromotionType.PERCENT) {
//                    totalAmount.subtract(promotion.getDiscountValue());
//                    int i = promotion.getUsageLimit() - 1;
//                    promotion.setUsageLimit(i);
//                    promotionRepository.save(promotion);
//                }
//            }
//        }

        if (promotion != null && promotion.getStatus() == PromotionStatus.ACTIVE) {
            switch (promotion.getType()) {
                case FREESHIP:
                    shippingFee.multiply(promotion.getDiscountValue());
                    break;

                case AMOUNT:
                    totalAmount.multiply(promotion.getDiscountValue());
                    break;

                case GIFT:
                    System.out.println("tặng quà cho người dùng");
                    break;

                case PERCENT:
                    totalAmount.subtract(promotion.getDiscountValue());
                    break;

                default:
                    break;
            }
            int i = promotion.getUsageLimit() - 1;
            promotion.setUsageLimit(i);
            promotionRepository.save(promotion);
        }
        orderRepository.save(order);

        // Map sang DTO trả về
        OderResponseDto responseDto = new OderResponseDto();
        responseDto.setId(order.getId());
        responseDto.setOrderCode(order.getOrderCode());
        responseDto.setOrderDate(order.getOrderDate());
        responseDto.setUserName(username);
        responseDto.setPhoneNumber(user.getPhone());
        responseDto.setEmail(user.getEmail());
        responseDto.setShippingAddress(order.getShippingAddress());
        responseDto.setPaymentMethod(order.getPaymentMethod());
        responseDto.setTotalAmount(order.getTotalAmount().doubleValue());
        responseDto.setStatus(order.getStatus().name());

        List<OrderItemResponseDto> itemDtos = order.getItems().stream().map(item -> {
            OrderItemResponseDto dto = new OrderItemResponseDto();
            dto.setProductId(item.getProduct().getId());
            dto.setName(item.getProductName());
            dto.setUnitPrice(item.getUnitPrice().doubleValue());
            dto.setQuantity(item.getQuantity());
            dto.setTotalPrice(item.getTotalPrice().doubleValue());
            return dto;
        }).collect(Collectors.toList());
        responseDto.setItems(itemDtos);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("order successfully",responseDto,"SUCCESS"));
    }

    private String buildEmailContent(Order order, String formattedDate, NumberFormat format) {
        List<OrderItemResponseDto> itemDtos = order.getItems().stream().map(item -> {
            OrderItemResponseDto dto = new OrderItemResponseDto();
            dto.setProductId(item.getProduct().getId());
            dto.setName(item.getProductName());
            dto.setQuantity(item.getQuantity());
            dto.setUnitPrice(item.getUnitPrice().doubleValue());
            dto.setTotalPrice(item.getTotalPrice().doubleValue());
            return dto;
        }).toList();

        String products = itemDtos.stream()
                .map(dto -> dto.getName() + " x" + dto.getQuantity() + " = " + format.format(dto.getTotalPrice()))
                .collect(Collectors.joining("\n"));

        return " Your order update\n\n" +
                " Order date: " + formattedDate + "\n" +
                " User: " + order.getUser().getUsername() + "\n" +
                " Delivery address: " + order.getShippingAddress() + "\n" +
                " Products:\n" + products + "\n" +
                " Total amount: " + format.format(order.getTotalAmount()) + "\n" +
                " Payment method: " + order.getPaymentMethod() + "\n" +
                " Status: " + order.getStatus() + "\n";
    }


    public ResponseEntity<ApiResponse> updateOrderStatus(OrderStatus orderStatus, int id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        Locale vnd = new Locale("vi", "VN");
        NumberFormat format = NumberFormat.getCurrencyInstance(vnd);
        LocalDateTime orderDate = order.getOrderDate();
        Date date = Timestamp.valueOf(orderDate);
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = dayFormat.format(date);
        order.setStatus(orderStatus);
        orderRepository.save(order);
        String content = buildEmailContent(order, formattedDate, format);
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(order.getUser().getEmail());
        msg.setSubject("Your order status - " + order.getOrderCode());
        msg.setText(content);
        mailSender.send(msg);
        return ResponseEntity.ok(new ApiResponse("Order status updated successfully", null, "SUCCESS"));
    }



    public ResponseEntity<ApiResponse> UpdateThePaymentMethodForTheOrder (int id,PaymentMethod paymentMethod){
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        order.setPaymentMethod(paymentMethod);
        orderRepository.save(order);
        return ResponseEntity.ok(new ApiResponse("Update payment method for successful order.",null,"SUCCESS"));
    }

    @Override
    public ResponseEntity<ApiResponse> deleteOrder(int id) {
        return null;
    }

}
