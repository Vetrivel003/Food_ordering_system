package com.project.sapaadu.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CartItemResponse {

    private Long menuItemId;
    private String name;
    private Integer quantity;
    private BigDecimal price;
}
