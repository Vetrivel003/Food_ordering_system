package com.project.sapaadu.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderDetailResponse {

    private Long orderId;
    private String restaurantName;
    private Double totalAmount;
    private String orderStatus;
    private LocalDateTime createdAt;
    private List<OrderItemResponse> items;
}

