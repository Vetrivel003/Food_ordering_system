package com.project.sapaadu.dto.response;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CheckoutPreviewResponse {

    private List<ValidRestaurantPreviewResponse> validRestaurants;
    private List<InvalidRestaurantResponse> invalidRestaurants;
}
