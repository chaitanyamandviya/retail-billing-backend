package com.vandana.controller;

import com.vandana.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"})
public class ReportingController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/total-sales")
    public ResponseEntity<BigDecimal> totalSales(
            @RequestParam("from") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
            @RequestParam("to") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toDate) {
        BigDecimal total = reportService.getTotalSales(fromDate, toDate);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/top-products")
    public ResponseEntity<Map<String, Integer>> topProducts(
            @RequestParam("from") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
            @RequestParam("to") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toDate,
            @RequestParam(value = "limit", defaultValue = "5") int limit) {
        Map<String, Integer> topProducts = reportService.getTopProducts(fromDate, toDate, limit);
        return ResponseEntity.ok(topProducts);
    }
}
