package com.project.sapaadu.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RestaurantCartGroupResponse {

    private Long restaurantId;
    private String restaurantName;
    private List<CartItemResponse> items;
}
