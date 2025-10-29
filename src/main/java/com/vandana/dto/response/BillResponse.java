package com.vandana.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillResponse {

    private Integer billId;
    private String billNumber;
    private String billDate;
    private String billTime;

    private String customerName;
    private String customerPhone;

    private BigDecimal subtotal;
    private BigDecimal discountAmount;
    private Long totalAmount;

    private String paymentMode;

    private String createdBy;

    private String status;

    private List<BillItemResponse> billItems;
}
