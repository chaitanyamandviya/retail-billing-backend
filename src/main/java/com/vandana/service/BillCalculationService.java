package com.vandana.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class BillCalculationService {

    private static final BigDecimal DISCOUNT_RATE = new BigDecimal("0.10"); // 10% discount
    private static final int SCALE = 2; // for currency decimal places
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    /**
     * Calculate overall bill amounts with 10% discount and final rounded total.
     */
    public BillAmounts calculateBillAmounts(BigDecimal subtotal) {
        if (subtotal == null || subtotal.signum() <= 0) {
            throw new IllegalArgumentException("Subtotal must be greater than zero");
        }

        BigDecimal discountAmount = subtotal.multiply(DISCOUNT_RATE)
                .setScale(SCALE, ROUNDING_MODE);

        BigDecimal totalBeforeRounding = subtotal.subtract(discountAmount)
                .setScale(SCALE, ROUNDING_MODE);

        // Round to nearest whole number for final amount
        long finalAmount = Math.round(totalBeforeRounding.doubleValue());

        return BillAmounts.builder()
                .subtotal(subtotal.setScale(SCALE, ROUNDING_MODE))
                .discountAmount(discountAmount)
                .finalAmount(finalAmount)
                .build();
    }

    /**
     * Calculate amount per bill line = quantity * unit price.
     */
    public BigDecimal calculateLineAmount(Integer quantity, BigDecimal unitPrice) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        if (unitPrice == null || unitPrice.signum() <= 0) {
            throw new IllegalArgumentException("Unit price must be greater than zero");
        }

        return BigDecimal.valueOf(quantity)
                .multiply(unitPrice)
                .setScale(SCALE, ROUNDING_MODE);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BillAmounts {
        private BigDecimal subtotal;
        private BigDecimal discountAmount;
        private Long finalAmount;
    }
}