package com.example.ecomerce.payment.service;

import com.example.ecomerce.Response.ApiResponse;
import com.example.ecomerce.entity.Order;
import com.example.ecomerce.enums.Error;
import com.example.ecomerce.enums.OrderStatus;
import com.example.ecomerce.enums.PaymentMethod;
import com.example.ecomerce.enums.PaymentStatus;
import com.example.ecomerce.payment.config.VnpayConfig;
import com.example.ecomerce.payment.config.ZalopayConfig;
import com.example.ecomerce.payment.crypto.HMACUtil;
import com.example.ecomerce.payment.dto.ZalopayRequest;
import com.example.ecomerce.payment.entity.Payment;
import com.example.ecomerce.payment.repository.PaymentRepository;
import com.example.ecomerce.repository.OderRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.apache.http.impl.client.HttpClients;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ZalopayService {

    @Autowired
   private OderRepository orderRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    private static String getCurrentTimeString(String format) {
        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT+7"));
        SimpleDateFormat fmt = new SimpleDateFormat(format);
        fmt.setCalendar(cal);
        return fmt.format(cal.getTimeInMillis());
    }

    public String createOrder(ZalopayRequest zalopayRequest) {
        Order order = orderRepository.findById(zalopayRequest.getOrderId()).orElse(null);

        Random rand = new Random();
        int randomId = rand.nextInt(1000000);

        if (order == null) {
            throw new RuntimeException("Order not found");
        }
        long amount = order.getTotalAmount().longValue();
        String appTransId =
                getCurrentTimeString("yyMMdd") + "_" + String.format("%06d", randomId);


        Map<String, Object> zalo_param = new HashMap<>();
        zalo_param.put("app_id", ZalopayConfig.config.get("app_id"));
        zalo_param.put("app_trans_id", appTransId);
        zalo_param.put("app_time", System.currentTimeMillis());
        zalo_param.put("app_user", "user123");
        zalo_param.put("amount", amount);
        zalo_param.put("description", "SN Mobile - Payment for the order #" + randomId);
        zalo_param.put("bank_code", "");
        zalo_param.put("item", "[{}]");
        zalo_param.put("embed_data", "{}");
        zalo_param.put("callback_url",
                "https://9cbc-2405-4803-c860-45d0-a9ba-5d84-9e59-fb4b.ngrok-free.app/api/zalopay/callback");

        String data = zalo_param.get("app_id") + "|" + zalo_param.get("app_trans_id") + "|" + zalo_param.get("app_user") + "|"
                + zalo_param.get("amount") + "|" + zalo_param.get("app_time") + "|" + zalo_param.get("embed_data") + "|"
                + zalo_param.get("item");

        String mac = HMACUtil.HMacHexStringEncode(HMACUtil.HMACSHA256, ZalopayConfig.config.get("key1"), data);
        zalo_param.put("mac", mac);

        System.out.println("Generated MAC: " + mac);

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(ZalopayConfig.config.get("endpoint"));

            List<NameValuePair> params = new ArrayList<>();
            for (Map.Entry<String, Object> entry : zalo_param.entrySet()) {
                params.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
            }

            post.setEntity(new UrlEncodedFormEntity(params));

            try (CloseableHttpResponse response = client.execute(post)) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuilder resultJsonStr = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    resultJsonStr.append(line);
                }


                System.out.println("Zalopay Response: " + resultJsonStr.toString());
                Payment payment = new Payment();
                payment.setOrder(order);
                payment.setAmount(order.getTotalAmount());
                PaymentMethod method = zalopayRequest.getMethod() != null
                        ? zalopayRequest.getMethod()
                        : PaymentMethod.ZALOPAY;order.setPaymentMethod(method);

                payment.setStatus(PaymentStatus.SUCCESSFUL);
                payment.setPaidAt(LocalDateTime.now());
                payment.setTxnRef(appTransId);
                paymentRepository.save(payment);

                order.setStatus(OrderStatus.PAID);
                orderRepository.save(order);

                return resultJsonStr.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\": \"Failed to create order: " + e.getMessage() + "\"}";
        }
    }

    public String getOrderStatus(String appTransId) {
        String data = ZalopayConfig.config.get("app_id") + "|" + appTransId + "|" + ZalopayConfig.config.get("key1");
        String mac = HMACUtil.HMacHexStringEncode(HMACUtil.HMACSHA256, ZalopayConfig.config.get("key1"), data);

        Payment payment = paymentRepository.findByTxnRef(appTransId);
        Order order = orderRepository.findById(payment.getOrder().getId()).orElse(null);

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(ZalopayConfig.config.get("orderstatus"));

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("app_id", ZalopayConfig.config.get("app_id")));
            params.add(new BasicNameValuePair("app_trans_id", appTransId));
            params.add(new BasicNameValuePair("mac", mac));

            post.setEntity(new UrlEncodedFormEntity(params));

            try (CloseableHttpResponse response = client.execute(post)) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuilder resultJsonStr = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    resultJsonStr.append(line);
                }

//                ObjectMapper mapper = new ObjectMapper();
//                JsonNode json = mapper.readTree(resultJsonStr.toString());
//
//                int status = json.get("status").asInt();
//                if (status == 1){
//                    payment.setStatus(PaymentStatus.PROCESSING);
//                } else if (status == 2){
//                    order.setStatus(OrderStatus.PAID);
//                    payment.setStatus(PaymentStatus.SUCCESSFUL);
//                } else   {
//                    order.setStatus(OrderStatus.UNPAID);
//                    payment.setStatus(PaymentStatus.FAILED);
//                }
//
//                paymentRepository.save(payment);
//                orderRepository.save(order);

                return resultJsonStr.toString();
            }




        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\": \"Failed to get order status: " + e.getMessage() + "\"}";
        }
    }
}
