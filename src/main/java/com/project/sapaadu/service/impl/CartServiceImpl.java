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
import com.project.sapaadu.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

        User user = userRepository.findById(request.getUserId())
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
    public CartResponse getCart(Long userId) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new BadRequestException("Cart not found"));

        Map<Long, List<CartItem>> grouped = cart.getCartItems()
                .stream()
                .collect(Collectors.groupingBy(
                        item -> item.getMenuItem().getRestaurant().getId()
                ));

        List<RestaurantCartGroupResponse> restaurantGroups = grouped.entrySet()
                .stream()
                .map(entry -> {

                    Long restaurantId = entry.getKey();
                    Restaurant restaurant = entry.getValue()
                            .get(0)
                            .getMenuItem()
                            .getRestaurant();

                    List<CartItemResponse> items = entry.getValue()
                            .stream()
                            .map(item -> CartItemResponse.builder()
                                    .menuItemId(item.getMenuItem().getId())
                                    .name(item.getMenuItem().getName())
                                    .quantity(item.getQuantity())
                                    .price(item.getPriceAtAdd())
                                    .build())
                            .toList();

                    return RestaurantCartGroupResponse.builder()
                            .restaurantId(restaurantId)
                            .restaurantName(restaurant.getName())
                            .items(items)
                            .build();
                })
                .toList();

        return CartResponse.builder()
                .userId(userId)
                .restaurants(restaurantGroups)
                .build();
    }

}