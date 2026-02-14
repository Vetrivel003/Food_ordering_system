package com.project.sapaadu.controller;

import com.project.sapaadu.dto.response.CheckoutPreviewResponse;
import com.project.sapaadu.service.CheckoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/checkout")
@RequiredArgsConstructor
public class CheckoutController {

    private final CheckoutService checkoutService;

    @PostMapping("/preview/{userId}")
    public ResponseEntity<CheckoutPreviewResponse> preview(@PathVariable Long userId) {

        return ResponseEntity.ok(checkoutService.previewCheckout(userId));
    }
}
