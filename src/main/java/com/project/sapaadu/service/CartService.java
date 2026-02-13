package com.project.sapaadu.service;

import com.project.sapaadu.dto.request.AddToCartRequest;
import com.project.sapaadu.dto.response.CartResponse;

public interface CartService {

    void addToCart(AddToCartRequest request);

    CartResponse getCart(Long userId);
}
