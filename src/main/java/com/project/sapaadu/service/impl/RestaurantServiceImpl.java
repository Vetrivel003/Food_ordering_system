package com.project.sapaadu.service.impl;

import com.project.sapaadu.dto.request.CreateRestaurantRequest;
import com.project.sapaadu.entity.Restaurant;
import com.project.sapaadu.entity.User;
import com.project.sapaadu.exception.BadRequestException;
import com.project.sapaadu.repository.RestaurantRepository;
import com.project.sapaadu.repository.UserRepository;
import com.project.sapaadu.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

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

        restaurantRepository.save(restaurant);
    }
}
