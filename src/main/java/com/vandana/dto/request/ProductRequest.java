package com.vandana.dto.request;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class ProductRequest {

    @NotBlank(message = "Product name is required")
    private String productName;

    private String description;
}