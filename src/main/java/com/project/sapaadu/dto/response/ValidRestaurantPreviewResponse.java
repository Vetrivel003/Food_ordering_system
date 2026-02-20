package com.project.sapaadu.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ValidRestaurantPreviewResponse {

    private Long restaurantId;
    private String restaurantName;
    private BigDecimal totalAmount;
}
