package com.project.sapaadu.service.impl;

import com.project.sapaadu.dto.request.AddToCartRequest;
import com.project.sapaadu.entity.Cart;
import com.project.sapaadu.entity.CartItem;
import com.project.sapaadu.entity.MenuItem;
import com.project.sapaadu.entity.User;
import com.project.sapaadu.exception.BadRequestException;
import com.project.sapaadu.repository.CartItemRepository;
import com.project.sapaadu.repository.CartRepository;
import com.project.sapaadu.repository.MenuItemRepository;
import com.project.sapaadu.repository.UserRepository;
import com.project.sapaadu.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}