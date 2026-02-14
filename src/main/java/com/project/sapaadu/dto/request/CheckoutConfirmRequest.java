package com.project.sapaadu.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CheckoutConfirmRequest {

    @NotNull
    private Long userId;

    @NotEmpty
    private List<Long> restaurantIds;
}
