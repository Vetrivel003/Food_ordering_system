package com.project.sapaadu.service;


import com.project.sapaadu.dto.response.CheckoutPreviewResponse;

public interface CheckoutService {

    CheckoutPreviewResponse previewCheckout(Long userId);
}
