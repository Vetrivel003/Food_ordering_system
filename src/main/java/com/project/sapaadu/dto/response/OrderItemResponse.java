package com.project.sapaadu.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderItemResponse {

    private String itemName;
    private BigDecimal price;
    private Integer quantity;
}
