package com.project.sapaadu.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CartResponse {

    private Long userId;
    private List<RestaurantCartGroupResponse> restaurants;
}
