package com.project.sapaadu.service.impl;

import com.project.sapaadu.dto.request.CreateMenuItemRequest;
import com.project.sapaadu.dto.request.CreateRestaurantRequest;
import com.project.sapaadu.dto.response.MenuItemResponse;
import com.project.sapaadu.dto.response.RestaurantResponse;
import com.project.sapaadu.entity.MenuItem;
import com.project.sapaadu.entity.Restaurant;
import com.project.sapaadu.entity.User;
import com.project.sapaadu.exception.BadRequestException;
import com.project.sapaadu.repository.MenuItemRepository;
import com.project.sapaadu.repository.RestaurantRepository;
import com.project.sapaadu.repository.UserRepository;
import com.project.sapaadu.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final MenuItemRepository menuItemRepository;

    @Override
    public void createRestaurant(CreateRestaurantRequest request) {

        User owner = userRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new BadRequestException("Owner not found"));

        Restaurant restaurant = Restaurant.builder()
                .owner(owner)
                .name(request.getName())
                .description(request.getDescription())
                .build();

        restaurantRepository.save(restaurant);
    }

    @Override
    public void approveRestaurant(Long restaurantId) {

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new BadRequestException("Restaurant not found"));

        restaurant.setApproved(true);

        restaurant.setOpen(true);

        restaurantRepository.save(restaurant);
    }

    @Override
    public List<RestaurantResponse> getAvailableRestaurants() {

        return restaurantRepository.findByIsApprovedTrueAndIsOpenTrue()
                .stream()
                .map(restaurant -> RestaurantResponse.builder()
                        .id(restaurant.getId())
                        .name(restaurant.getName())
                        .description(restaurant.getDescription())
                        .build())
                .toList();
    }

    @Override
    public void addMenuItem(CreateMenuItemRequest request) {

        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                .orElseThrow(() -> new BadRequestException("Restaurant not found"));

        if (!restaurant.isApproved()) {
            throw new BadRequestException("Restaurant is not approved");
        }

        if (!restaurant.isOpen()) {
            throw new BadRequestException("Restaurant is currently closed");
        }

        MenuItem menuItem = MenuItem.builder()
                .restaurant(restaurant)
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .build();

        menuItemRepository.save(menuItem);
    }

    @Override
    public List<MenuItemResponse> getMenuItems(Long restaurantId) {

        return menuItemRepository.findByRestaurantIdAndIsAvailableTrue(restaurantId)
                .stream()
                .map(item -> MenuItemResponse.builder()
                        .id(item.getId())
                        .name(item.getName())
                        .description(item.getDescription())
                        .price(item.getPrice())
                        .build())
                .toList();
    }


}
