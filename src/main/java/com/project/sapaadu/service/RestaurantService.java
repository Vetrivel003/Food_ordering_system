package com.project.sapaadu.service;


import com.project.sapaadu.dto.request.CreateRestaurantRequest;
import com.project.sapaadu.dto.response.RestaurantResponse;

import java.util.List;

public interface RestaurantService {

    void createRestaurant(CreateRestaurantRequest request);

    void approveRestaurant(Long restaurantId);

    List<RestaurantResponse> getAvailableRestaurants();
}
