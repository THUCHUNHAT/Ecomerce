package com.example.ecomerce.payment.entity;
import com.example.ecomerce.entity.Order;
import com.example.ecomerce.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Entity
@Getter
@Setter
@Table (name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "order_id")
    private Order order;

    @Column (name = "amount")
    private BigDecimal amount;

    @Column (name = "txn_ref")
    private String txnRef;

    @Column (name = "paid_at")
    private LocalDateTime paidAt;

    @Enumerated(EnumType.STRING)
    @Column (name = "status")
    private PaymentStatus status;
}
