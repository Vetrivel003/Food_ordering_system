package com.project.sapaadu.service;

import com.project.sapaadu.dto.request.AddToCartRequest;

public interface CartService {

    void addToCart(AddToCartRequest request);
}
