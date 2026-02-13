package com.project.sapaadu.controller;

import com.project.sapaadu.dto.request.AddToCartRequest;
import com.project.sapaadu.dto.response.CartResponse;
import com.project.sapaadu.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@Valid @RequestBody AddToCartRequest request) {

        cartService.addToCart(request);
        return ResponseEntity.ok("Item added to cart");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CartResponse> getCart(@PathVariable Long userId) {

        return ResponseEntity.ok(cartService.getCart(userId));
    }
}
