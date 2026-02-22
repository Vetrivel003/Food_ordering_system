package com.project.sapaadu.controller;

import com.project.sapaadu.dto.request.CheckoutConfirmRequest;
import com.project.sapaadu.dto.response.CheckoutPreviewResponse;
import com.project.sapaadu.service.CheckoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/checkout")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class CheckoutController {

    private final CheckoutService checkoutService;

    @PostMapping("/preview/")
    public ResponseEntity<CheckoutPreviewResponse> preview() {

        return ResponseEntity.ok(checkoutService.previewCheckout());
    }

    @PostMapping("/confirm")
    public ResponseEntity<List<Long>> confirm(
            @RequestBody CheckoutConfirmRequest request) {

        return ResponseEntity.ok(
                checkoutService.confirmCheckout(request));
    }
}
