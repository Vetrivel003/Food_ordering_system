package com.project.sapaadu.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class OrderSummaryResponse {

    private Long orderId;
    private String restaurantName;
    private Double totalAmount;
    private String orderStatus;
    private LocalDateTime createdAt;
}

