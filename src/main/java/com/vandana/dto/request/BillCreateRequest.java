package com.vandana.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class BillCreateRequest {

    private String customerName;

    private String customerPhone;

    @NotEmpty(message = "Bill items cannot be empty")
    private List<@Valid BillItemRequest> billItems;

    @NotBlank(message = "Payment mode is required")
    @Pattern(regexp = "CASH|ONLINE", message = "Payment mode must be CASH or ONLINE")
    private String paymentMode;
}
