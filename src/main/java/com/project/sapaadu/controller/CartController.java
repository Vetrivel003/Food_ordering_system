package com.project.sapaadu.controller;

import com.project.sapaadu.dto.request.AddToCartRequest;
import com.project.sapaadu.dto.response.CartResponse;
import com.project.sapaadu.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@Valid @RequestBody AddToCartRequest request) {

        cartService.addToCart(request);
        return ResponseEntity.ok("Item added to cart");
    }

    @GetMapping
    public ResponseEntity<CartResponse> getCart() {

        return ResponseEntity.ok(cartService.getCartForLoggedInUser());
    }
}
