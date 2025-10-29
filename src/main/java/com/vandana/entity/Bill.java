package com.vandana.entity;

import lombok.*;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bills")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer billId;

    @Column(unique = true, nullable = false)
    private String billNumber;

    @Column(nullable = false)
    private LocalDate billDate;

    @Column(nullable = false)
    private LocalTime billTime;

    @Column
    private String customerName;

    @Column
    private String customerPhone;

    @Column(nullable = false)
    private BigDecimal subtotal;

    @Column(nullable = false)
    private BigDecimal discountAmount;

    @Column(nullable = false)
    private Long totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMode paymentMode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @Column(nullable = false)
    private String shopName;

    @Column
    private String shopAddress;

    @Column
    private String gstin;

    @Column
    private String contactNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BillStatus status;

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OrderBy("itemOrder ASC")
    @Builder.Default
    private List<BillItem> billItems = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) {
            status = BillStatus.COMPLETED;
        }
    }

    public enum PaymentMode {
        CASH, ONLINE
    }

    public enum BillStatus {
        DRAFT, COMPLETED, CANCELLED
    }
}