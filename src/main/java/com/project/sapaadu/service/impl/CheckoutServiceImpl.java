package com.project.sapaadu.service.impl;

import com.project.sapaadu.dto.request.CheckoutConfirmRequest;
import com.project.sapaadu.dto.response.CheckoutPreviewResponse;
import com.project.sapaadu.dto.response.InvalidRestaurantResponse;
import com.project.sapaadu.dto.response.ValidRestaurantPreviewResponse;
import com.project.sapaadu.entity.*;
import com.project.sapaadu.exception.BadRequestException;
import com.project.sapaadu.repository.CartItemRepository;
import com.project.sapaadu.repository.CartRepository;
import com.project.sapaadu.repository.OrderItemRepository;
import com.project.sapaadu.repository.OrderRepository;
import com.project.sapaadu.service.CheckoutService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CheckoutServiceImpl implements CheckoutService {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public CheckoutPreviewResponse previewCheckout(Long userId) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new BadRequestException("Cart not found"));

        if (cart.getCartItems().isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }

        Map<Restaurant, List<CartItem>> grouped =
                cart.getCartItems()
                        .stream()
                        .collect(Collectors.groupingBy(
                                item -> item.getMenuItem().getRestaurant()
                        ));

        List<ValidRestaurantPreviewResponse> validList = new ArrayList<>();
        List<InvalidRestaurantResponse> invalidList = new ArrayList<>();

        for (Map.Entry<Restaurant, List<CartItem>> entry : grouped.entrySet()) {

            Restaurant restaurant = entry.getKey();
            List<CartItem> items = entry.getValue();

            if (!restaurant.isApproved()) {
                invalidList.add(
                        InvalidRestaurantResponse.builder()
                                .restaurantId(restaurant.getId())
                                .restaurantName(restaurant.getName())
                                .reason("Restaurant is not approved")
                                .build()
                );
                continue;
            }

            if (!restaurant.isOpen()) {
                invalidList.add(
                        InvalidRestaurantResponse.builder()
                                .restaurantId(restaurant.getId())
                                .restaurantName(restaurant.getName())
                                .reason("Restaurant is closed")
                                .build()
                );
                continue;
            }

            Double total = items.stream()
                    .map(item -> item.getPriceAtAdd() * item.getQuantity())
                    .reduce(0.0, Double::sum);

            validList.add(
                    ValidRestaurantPreviewResponse.builder()
                            .restaurantId(restaurant.getId())
                            .restaurantName(restaurant.getName())
                            .totalAmount(total)
                            .build()
            );
        }

        return CheckoutPreviewResponse.builder()
                .validRestaurants(validList)
                .invalidRestaurants(invalidList)
                .build();
    }

    @Transactional
    @Override
    public List<Long> confirmCheckout(CheckoutConfirmRequest request) {

        Cart cart = cartRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new BadRequestException("Cart not found"));

        if (cart.getCartItems().isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }

        Map<Restaurant, List<CartItem>> grouped =
                cart.getCartItems()
                        .stream()
                        .collect(Collectors.groupingBy(
                                item -> item.getMenuItem().getRestaurant()
                        ));

        List<Long> createdOrderIds = new ArrayList<>();

        for (Map.Entry<Restaurant, List<CartItem>> entry : grouped.entrySet()) {

            Restaurant restaurant = entry.getKey();

            if (!request.getRestaurantIds().contains(restaurant.getId())) {
                continue;
            }

            // Revalidation
            if (!restaurant.isApproved()) {
                throw new BadRequestException(
                        "Restaurant " + restaurant.getName() + " is not approved");
            }

            if (!restaurant.isOpen()) {
                throw new BadRequestException(
                        "Restaurant " + restaurant.getName() + " is closed");
            }

            List<CartItem> items = entry.getValue();

            Double total = items.stream()
                    .map(item -> item.getPriceAtAdd() * item.getQuantity())
                    .reduce(0.0, Double::sum);

            Order order = Order.builder()
                    .user(cart.getUser())
                    .restaurant(restaurant)
                    .orderStatus("PLACED")
                    .totalAmount(total)
                    .build();

            orderRepository.save(order);

            Set<OrderItem> orderItems = items.stream()
                    .map(item -> OrderItem.builder()
                            .order(order)
                            .itemName(item.getMenuItem().getName())
                            .price(item.getPriceAtAdd())
                            .quantity(item.getQuantity())
                            .build())
                    .collect(Collectors.toSet());

            orderItemRepository.saveAll(orderItems);

            createdOrderIds.add(order.getId());

            // Remove processed items from cart
            cart.getCartItems().removeAll(items);
            cartItemRepository.deleteAll(items);
        }

        return createdOrderIds;
    }

}
