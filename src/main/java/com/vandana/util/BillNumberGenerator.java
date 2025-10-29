package com.vandana.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BillNumberGenerator {

    public static String generateBillNumber(Integer billId, LocalDate billDate) {
        String dateFormat = billDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String sequentialNumber = String.format("%06d", billId);
        return String.format("BILL-%s-%s", dateFormat, sequentialNumber);
    }
}
