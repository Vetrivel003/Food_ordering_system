package com.project.sapaadu.controller;

import com.project.sapaadu.dto.response.OrderDetailResponse;
import com.project.sapaadu.dto.response.OrderSummaryResponse;
import com.project.sapaadu.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderSummaryResponse>> getMyOrders() {
        return ResponseEntity.ok(orderService.getMyOrders());
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailResponse> getOrderById(
            @PathVariable Long orderId) {

        return ResponseEntity.ok(
                orderService.getOrderById(orderId)
        );
    }
}
