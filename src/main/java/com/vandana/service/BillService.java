package com.vandana.service;

import com.vandana.dto.request.BillCreateRequest;
import com.vandana.dto.response.BillItemResponse;
import com.vandana.dto.response.BillResponse;
import com.vandana.entity.Bill;
import com.vandana.entity.BillItem;
import com.vandana.entity.Product;
import com.vandana.entity.User;
import com.vandana.exception.BadRequestException;
import com.vandana.exception.NotFoundException;
import com.vandana.repository.BillRepository;
import com.vandana.repository.ProductRepository;
import com.vandana.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BillService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BillCalculationService billCalculationService;

    public BillResponse createBill(BillCreateRequest request, Integer userId) {
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        BigDecimal subtotal = BigDecimal.ZERO;
        List<BillItem> billItems = new ArrayList<>();

        for (int i = 0; i < request.getBillItems().size(); i++) {
            var itemRequest = request.getBillItems().get(i);

            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new NotFoundException("Product not found"));

            BigDecimal lineAmount = billCalculationService.calculateLineAmount(
                    itemRequest.getQuantity(),
                    itemRequest.getUnitPrice()
            );

            BillItem billItem = BillItem.builder()
                    .product(product)
                    .quantity(itemRequest.getQuantity())
                    .unitPrice(itemRequest.getUnitPrice())
                    .lineAmount(lineAmount)
                    .itemOrder(i + 1)
                    .build();

            billItems.add(billItem);
            subtotal = subtotal.add(lineAmount);
        }

        var amounts = billCalculationService.calculateBillAmounts(subtotal);

        Bill bill = Bill.builder()
                .billDate(LocalDate.now())
                .billTime(LocalTime.now())
                .customerName(request.getCustomerName())
                .customerPhone(request.getCustomerPhone())
                .subtotal(amounts.getSubtotal())
                .discountAmount(amounts.getDiscountAmount())
                .totalAmount(amounts.getFinalAmount())
                .paymentMode(Bill.PaymentMode.valueOf(request.getPaymentMode()))
                .createdBy(currentUser)
                .shopName("Vandana")
                .shopAddress("Kapada line, Wardha, Maharashtra - 442001")
                .gstin("27ABMPL8177A1ZG")
                .contactNumber("+91 9226527720")
                .status(Bill.BillStatus.COMPLETED)
                .billItems(billItems)
                .build();

        Bill initialSavedBill = billRepository.save(bill);

        String billNumber = String.format("BILL-%04d%02d%02d-%06d",
                initialSavedBill.getBillDate().getYear(),
                initialSavedBill.getBillDate().getMonthValue(),
                initialSavedBill.getBillDate().getDayOfMonth(),
                initialSavedBill.getBillId());
        initialSavedBill.setBillNumber(billNumber);
        Bill billWithNumber = billRepository.save(initialSavedBill);

        billItems.forEach(item -> item.setBill(billWithNumber));

        return convertToResponse(billWithNumber);
    }

    public BillResponse getBillDetails(Integer billId) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new NotFoundException("Bill not found"));
        return convertToResponse(bill);
    }

    public Page<BillResponse> getAllBills(Pageable pageable, Integer userId) {
        Page<Bill> bills = billRepository.findByCreatedBy_UserId(userId, pageable);
        return bills.map(this::convertToResponse);
    }

    public BillResponse cancelBill(Integer billId, Integer userId) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new NotFoundException("Bill not found"));

        if (!bill.getCreatedBy().getUserId().equals(userId)) {
            throw new BadRequestException("You don't have permission to cancel this bill");
        }

        bill.setStatus(Bill.BillStatus.CANCELLED);
        Bill updatedBill = billRepository.save(bill);

        return convertToResponse(updatedBill);
    }

    private BillResponse convertToResponse(Bill bill) {
        return BillResponse.builder()
                .billId(bill.getBillId())
                .billNumber(bill.getBillNumber())
                .billDate(bill.getBillDate().toString())
                .billTime(bill.getBillTime().toString())
                .customerName(bill.getCustomerName())
                .customerPhone(bill.getCustomerPhone())
                .subtotal(bill.getSubtotal())
                .discountAmount(bill.getDiscountAmount())
                .totalAmount(bill.getTotalAmount())
                .paymentMode(bill.getPaymentMode().toString())
                .createdBy(bill.getCreatedBy().getFullName())
                .status(bill.getStatus().toString())
                .billItems(bill.getBillItems().stream()
                        .map(item -> BillItemResponse.builder()
                                .billItemId(item.getBillItemId())
                                .productName(item.getProduct().getProductName())
                                .quantity(item.getQuantity())
                                .unitPrice(item.getUnitPrice())
                                .lineAmount(item.getLineAmount())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}