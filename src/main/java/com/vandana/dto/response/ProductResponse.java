package com.vandana.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductResponse {
    private Integer productId;
    private String productName;
    private String description;
    private String status;
}
