package com.project.sapaadu.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemResponse {

    private String itemName;
    private Double price;
    private Integer quantity;
}
