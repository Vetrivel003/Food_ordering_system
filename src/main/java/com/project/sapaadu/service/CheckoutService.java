package com.project.sapaadu.service;


import com.project.sapaadu.dto.request.CheckoutConfirmRequest;
import com.project.sapaadu.dto.response.CheckoutPreviewResponse;

import java.util.List;

public interface CheckoutService {

    CheckoutPreviewResponse previewCheckout(Long userId);

    List<Long> confirmCheckout(CheckoutConfirmRequest request);

}
