package com.project.sapaadu.controller;

import com.project.sapaadu.dto.request.CreateRestaurantRequest;
import com.project.sapaadu.dto.response.RestaurantResponse;
import com.project.sapaadu.service.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PutMapping("/{id}/approve")
    public ResponseEntity<String> approveRestaurant(@PathVariable Long id) {

        restaurantService.approveRestaurant(id);
        return ResponseEntity.ok("Restaurant approved successfully");
    }

    @GetMapping
    public ResponseEntity<List<RestaurantResponse>> getRestaurants() {

        return ResponseEntity.ok(restaurantService.getAvailableRestaurants());
    }


}
