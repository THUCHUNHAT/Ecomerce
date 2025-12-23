package com.example.ecomerce.payment.service;

import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.enums.Error;
import com.example.ecomerce.enums.OrderStatus;
import com.example.ecomerce.enums.PaymentMethod;
import com.example.ecomerce.enums.PaymentStatus;
import com.example.ecomerce.exception.ErrorOrder;
import com.example.ecomerce.exception.ErrorPayment;
import com.example.ecomerce.payment.config.VnpayConfig;
import com.example.ecomerce.payment.dto.VnpayRequest;
import com.example.ecomerce.payment.entity.Payment;
import com.example.ecomerce.payment.repository.PaymentRepository;
import com.example.ecomerce.repository.OderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class VnpayService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OderRepository orderRepository;

    public ResponseEntity<ApiResponse> createPayment(VnpayRequest paymentRequest) {
        try {
            String vnp_Version = "2.1.0";
            String vnp_Command = "pay";
            String orderType = "other";

            var order = orderRepository.findById(paymentRequest.getOrderId()).orElse(null);
            if (order == null){
                return ResponseEntity.badRequest().body(new ApiResponse("The order does not exist.",null, ErrorOrder.UNDEFINED_ORDER));
            }

            BigDecimal amount = order.getTotalAmount().multiply(BigDecimal.valueOf(100));            String vnp_TxnRef = VnpayConfig.getRandomNumber(8);
            String vnp_IpAddr = "127.0.0.1";
            String vnp_TmnCode = VnpayConfig.vnp_TmnCode;

            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", vnp_Version);
            vnp_Params.put("vnp_Command", vnp_Command);
            vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
            vnp_Params.put("vnp_Amount", amount.toBigInteger().toString());            vnp_Params.put("vnp_CurrCode", "VND");
            vnp_Params.put("vnp_BankCode", "NCB");
            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.put("vnp_OrderInfo", "order payment " + order.getId());
            vnp_Params.put("vnp_OrderType", orderType);
            vnp_Params.put("vnp_Locale", "vn");
            vnp_Params.put("vnp_ReturnUrl", VnpayConfig.vnp_ReturnUrl);
            vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String vnp_CreateDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

            cld.add(Calendar.MINUTE, 15);
            String vnp_ExpireDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

            List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();

            for (String fieldName : fieldNames) {
                String fieldValue = vnp_Params.get(fieldName);
                if (fieldValue != null && !fieldValue.isEmpty()) {
                    // Log để kiểm tra giá trị trước khi encode
                    System.out.println("Encoding field: " + fieldName + " = " + fieldValue);

                    // Dùng UTF-8 thay vì US_ASCII
                    String encodedFieldName = URLEncoder.encode(fieldName, StandardCharsets.UTF_8);
                    String encodedFieldValue = URLEncoder.encode(fieldValue, StandardCharsets.UTF_8);

                    hashData.append(fieldName).append('=').append(encodedFieldValue);
                    query.append(encodedFieldName).append('=').append(encodedFieldValue);

                    query.append('&');
                    hashData.append('&');
                }
            }

            if (query.length() > 0) {
                query.setLength(query.length() - 1);
            }
            if (hashData.length() > 0) {
                hashData.setLength(hashData.length() - 1);
            }


            String vnp_SecureHash = VnpayConfig.hmacSHA512(VnpayConfig.secretKey, hashData.toString());
            query.append("&vnp_SecureHash=").append(vnp_SecureHash);

            Payment payment = new Payment();
            payment.setOrder(order);
            payment.setAmount(order.getTotalAmount());

            // Nếu client không gửi method thì mặc định VNPAY
            PaymentMethod method = paymentRequest.getMethod() != null
                    ? paymentRequest.getMethod()
                    : PaymentMethod.VNPAY;order.setPaymentMethod(method);

            payment.setStatus(PaymentStatus.PENDING);
            payment.setTxnRef(vnp_TxnRef);
            paymentRepository.save(payment);

            String paymentUrl = VnpayConfig.vnp_PayUrl + "?" + query;

            return ResponseEntity.ok(new ApiResponse("Tạo thanh toán thành công", paymentUrl, "SUCCESS"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Lỗi khi tạo thanh toán: " + e.getMessage(), null, ErrorPayment.PAYMENT_ERROR));
        }
    }

    public ResponseEntity<ApiResponse> handlePaymentReturn(String responseCode, String txnRef) {
        System.out.println("====== VNPay Return ======");
        System.out.println("txnRef = " + txnRef);
        System.out.println("responseCode = " + responseCode);

        try {
            Payment payment = paymentRepository.findByTxnRef(txnRef);
            var order = orderRepository.findById(payment.getOrder().getId()).orElse(null);
            if (order == null){
                return ResponseEntity.badRequest().body(new ApiResponse("The order does not exist.",null,ErrorOrder.UNDEFINED_ORDER));
            }
            if ("00".equals(responseCode)) {
                payment.setStatus(PaymentStatus.SUCCESSFUL);
            } else {
                payment.setStatus(PaymentStatus.FAILED);
            }
            payment.setPaidAt(LocalDateTime.now());
            paymentRepository.save(payment);

            if ("00".equals(responseCode)) {
                order.setStatus(OrderStatus.PAID);
                orderRepository.save(order);
                return ResponseEntity.ok(new ApiResponse("successful payment", null, "SUCCESS"));
            } else {
                order.setStatus(OrderStatus.UNPAID);
                orderRepository.save(order);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Payment failed! Error code: " + responseCode, null, ErrorPayment.PAYMENT_FAIL));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error when processing payment: " + e.getMessage(), null, ErrorPayment.PAYMENT_ERROR));

        }
    }




}
