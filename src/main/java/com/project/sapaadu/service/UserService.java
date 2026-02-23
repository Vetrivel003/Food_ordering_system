package com.project.sapaadu.service;

import com.project.sapaadu.dto.request.LoginRequest;
import com.project.sapaadu.dto.request.RefreshRequest;
import com.project.sapaadu.dto.request.RegisterRequest;
import com.project.sapaadu.dto.response.LoginResponse;
import com.project.sapaadu.dto.response.RefreshResponse;

public interface UserService {

    void registerUser(RegisterRequest request);

    LoginResponse loginUser(LoginRequest request);

    RefreshResponse refreshToken(RefreshRequest request);

    void logout(String refreshToken);
}