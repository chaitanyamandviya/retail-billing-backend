package com.vandana.repository;

import com.vandana.entity.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill, Integer> {
    Optional<Bill> findByBillNumber(String billNumber);
    Page<Bill> findByCreatedBy_UserId(Integer userId, Pageable pageable);
    List<Bill> findByBillDateAndStatus(LocalDate billDate, Bill.BillStatus status);
    List<Bill> findByPaymentMode(Bill.PaymentMode paymentMode);
    List<Bill> findByBillNumberContaining(String billNumber);
    List<Bill> findByBillDateBetweenAndStatus(LocalDate start, LocalDate end, Bill.BillStatus status);

}
