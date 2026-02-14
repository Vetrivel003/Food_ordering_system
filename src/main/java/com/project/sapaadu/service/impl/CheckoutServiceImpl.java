package com.project.sapaadu.service.impl;

import com.project.sapaadu.dto.response.CheckoutPreviewResponse;
import com.project.sapaadu.dto.response.InvalidRestaurantResponse;
import com.project.sapaadu.dto.response.ValidRestaurantPreviewResponse;
import com.project.sapaadu.entity.Cart;
import com.project.sapaadu.entity.CartItem;
import com.project.sapaadu.entity.Restaurant;
import com.project.sapaadu.exception.BadRequestException;
import com.project.sapaadu.repository.CartRepository;
import com.project.sapaadu.service.CheckoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CheckoutServiceImpl implements CheckoutService {

    private final CartRepository cartRepository;

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
}
