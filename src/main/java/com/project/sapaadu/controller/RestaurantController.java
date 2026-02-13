package com.project.sapaadu.controller;

import com.project.sapaadu.dto.request.CreateRestaurantRequest;
import com.project.sapaadu.service.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping
    public ResponseEntity<String> createRestaurant(
            @Valid @RequestBody CreateRestaurantRequest request) {

        restaurantService.createRestaurant(request);
        return ResponseEntity.ok("Restaurant created. Awaiting approval.");
    }
}
