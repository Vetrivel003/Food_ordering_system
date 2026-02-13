package com.project.sapaadu.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartItemResponse {

    private Long menuItemId;
    private String name;
    private Integer quantity;
    private Double price;
}
