package com.vandana.service;

import com.vandana.entity.Bill;
import com.vandana.entity.BillItem;
import com.vandana.exception.BadRequestException;
import com.vandana.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private BillRepository billRepository;

    /**
     * Total sales amount between dates (filter only for COMPLETED bills).
     */
    public BigDecimal getTotalSales(LocalDate fromDate, LocalDate toDate) {
        if (fromDate == null || toDate == null || fromDate.isAfter(toDate))
            throw new BadRequestException("Invalid date range");

        List<Bill> bills = billRepository.findByBillDateBetweenAndStatus(fromDate, toDate, Bill.BillStatus.COMPLETED);
        return bills.stream()
                .map(Bill::getTotalAmount)
                .filter(Objects::nonNull)
                .map(BigDecimal::valueOf)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Top N products sold by quantity in date range.
     */
    public Map<String, Integer> getTopProducts(LocalDate fromDate, LocalDate toDate, int topN) {
        if (fromDate == null || toDate == null || fromDate.isAfter(toDate))
            throw new BadRequestException("Invalid date range");

        List<Bill> bills = billRepository.findByBillDateBetweenAndStatus(fromDate, toDate, Bill.BillStatus.COMPLETED);
        Map<String, Integer> productQuantities = new HashMap<>();

        for (Bill bill : bills) {
            for (BillItem item : bill.getBillItems()) {
                String productName = item.getProduct().getProductName();
                productQuantities.merge(productName, item.getQuantity(), Integer::sum);
            }
        }

        return productQuantities.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(topN)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
