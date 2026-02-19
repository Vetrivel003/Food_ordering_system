package com.project.sapaadu.service.impl;

import com.project.sapaadu.dto.response.OrderDetailResponse;
import com.project.sapaadu.dto.response.OrderItemResponse;
import com.project.sapaadu.dto.response.OrderSummaryResponse;
import com.project.sapaadu.entity.Order;
import com.project.sapaadu.entity.OrderItem;
import com.project.sapaadu.entity.User;
import com.project.sapaadu.exception.BadRequestException;
import com.project.sapaadu.repository.OrderRepository;
import com.project.sapaadu.repository.UserRepository;
import com.project.sapaadu.security.SecurityUtils;
import com.project.sapaadu.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Override
    public List<OrderSummaryResponse> getMyOrders() {

        String email = SecurityUtils.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("User not found"));

        List<Order> orders =
                orderRepository.findByUserIdOrderByCreatedAtDesc(user.getId());

        List<OrderSummaryResponse> responseList = new ArrayList<>();

        for (Order order : orders) {

            OrderSummaryResponse response =
                    OrderSummaryResponse.builder()
                            .orderId(order.getId())
                            .restaurantName(order.getRestaurant().getName())
                            .totalAmount(order.getTotalAmount())
                            .orderStatus(order.getOrderStatus())
                            .createdAt(order.getCreatedAt())
                            .build();

            responseList.add(response);
        }

        return responseList;
    }

    @Override
    public OrderDetailResponse getOrderById(Long orderId) {

        String email = SecurityUtils.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("User not found"));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BadRequestException("Order not found"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("You are not allowed to view this order");
        }

        List<OrderItemResponse> itemResponses = new ArrayList<>();

        for (OrderItem item : order.getOrderItems()) {

            OrderItemResponse itemResponse =
                    OrderItemResponse.builder()
                            .itemName(item.getItemName())
                            .price(item.getPrice())
                            .quantity(item.getQuantity())
                            .build();

            itemResponses.add(itemResponse);
        }

        return OrderDetailResponse.builder()
                .orderId(order.getId())
                .restaurantName(order.getRestaurant().getName())
                .totalAmount(order.getTotalAmount())
                .orderStatus(order.getOrderStatus())
                .createdAt(order.getCreatedAt())
                .items(itemResponses)
                .build();
    }
}
