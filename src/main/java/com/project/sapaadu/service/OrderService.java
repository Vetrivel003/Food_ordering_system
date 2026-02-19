package com.project.sapaadu.service;

import com.project.sapaadu.dto.response.OrderDetailResponse;
import com.project.sapaadu.dto.response.OrderSummaryResponse;

import java.util.List;

public interface OrderService {

    List<OrderSummaryResponse> getMyOrders();

    OrderDetailResponse getOrderById(Long orderId);
}

