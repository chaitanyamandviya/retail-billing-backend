package com.vandana.controller;

import com.vandana.dto.request.BillCreateRequest;
import com.vandana.dto.response.BillResponse;
import com.vandana.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/bills")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"})
public class BillController {

    @Autowired
    private BillService billService;

    @PostMapping("/create")
    public ResponseEntity<BillResponse> createBill(
            @Valid @RequestBody BillCreateRequest request,
            Authentication authentication) {
        Integer userId = (Integer) authentication.getPrincipal();
        BillResponse response = billService.createBill(request, userId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{billId}")
    public ResponseEntity<BillResponse> getBillDetails(@PathVariable Integer billId) {
        BillResponse response = billService.getBillDetails(billId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<BillResponse>> getAllBills(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            Authentication authentication) {
        Integer userId = (Integer) authentication.getPrincipal();
        Pageable pageable = PageRequest.of(page, size);
        Page<BillResponse> response = billService.getAllBills(pageable, userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{billId}/cancel")
    public ResponseEntity<BillResponse> cancelBill(
            @PathVariable Integer billId,
            Authentication authentication) {
        Integer userId = (Integer) authentication.getPrincipal();
        BillResponse response = billService.cancelBill(billId, userId);
        return ResponseEntity.ok(response);
    }
}