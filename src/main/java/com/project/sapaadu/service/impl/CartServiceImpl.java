package com.project.sapaadu.service.impl;

import com.project.sapaadu.dto.request.AddToCartRequest;
import com.project.sapaadu.dto.response.CartItemResponse;
import com.project.sapaadu.dto.response.CartResponse;
import com.project.sapaadu.dto.response.RestaurantCartGroupResponse;
import com.project.sapaadu.entity.*;
import com.project.sapaadu.exception.BadRequestException;
import com.project.sapaadu.repository.CartItemRepository;
import com.project.sapaadu.repository.CartRepository;
import com.project.sapaadu.repository.MenuItemRepository;
import com.project.sapaadu.repository.UserRepository;
import com.project.sapaadu.security.SecurityUtils;
import com.project.sapaadu.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final MenuItemRepository menuItemRepository;

    @Override
    public void addToCart(AddToCartRequest request) {

        String email = SecurityUtils.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("User not found"));

        MenuItem menuItem = menuItemRepository.findById(request.getMenuItemId())
                .orElseThrow(() -> new BadRequestException("Menu item not found"));

        // Find or create cart
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart newCart = Cart.builder()
                            .user(user)
                            .build();
                    return cartRepository.save(newCart);
                });

        // Check if item already exists
        CartItem cartItem = cartItemRepository
                .findByCartIdAndMenuItemId(cart.getId(), menuItem.getId())
                .orElse(null);

        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
        } else {
            cartItem = CartItem.builder()
                    .cart(cart)
                    .menuItem(menuItem)
                    .quantity(request.getQuantity())
                    .priceAtAdd(menuItem.getPrice())
                    .build();
        }

        cartItemRepository.save(cartItem);
    }

    @Override
    public CartResponse getCartForLoggedInUser() {

        String email = SecurityUtils.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("User not found"));

        return getCart(user.getId());
    }

    public CartResponse getCart(Long userId) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new BadRequestException("Cart not found"));

        // Step 1: Group items by restaurantId
        Map<Long, List<CartItem>> grouped = new HashMap<>();

        for (CartItem item : cart.getCartItems()) {

            Long restaurantId = item.getMenuItem()
                    .getRestaurant()
                    .getId();

            if (!grouped.containsKey(restaurantId)) {
                grouped.put(restaurantId, new ArrayList<>());
            }

            grouped.get(restaurantId).add(item);
        }

        // Step 2: Build restaurant response list
        List<RestaurantCartGroupResponse> restaurantGroups = new ArrayList<>();

        for (Map.Entry<Long, List<CartItem>> entry : grouped.entrySet()) {

            Long restaurantId = entry.getKey();
            List<CartItem> cartItems = entry.getValue();

            // Get restaurant info from first item
            Restaurant restaurant = cartItems.get(0)
                    .getMenuItem()
                    .getRestaurant();

            // Build item response list
            List<CartItemResponse> items = new ArrayList<>();

            for (CartItem cartItem : cartItems) {

                CartItemResponse response = CartItemResponse.builder()
                        .menuItemId(cartItem.getMenuItem().getId())
                        .name(cartItem.getMenuItem().getName())
                        .quantity(cartItem.getQuantity())
                        .price(cartItem.getPriceAtAdd())
                        .build();

                items.add(response);
            }

            RestaurantCartGroupResponse groupResponse =
                    RestaurantCartGroupResponse.builder()
                            .restaurantId(restaurantId)
                            .restaurantName(restaurant.getName())
                            .items(items)
                            .build();

            restaurantGroups.add(groupResponse);
        }

        // Final return statement
        return CartResponse.builder()
                .userId(userId)
                .restaurants(restaurantGroups)
                .build();
    }


}