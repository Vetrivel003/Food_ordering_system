package com.project.sapaadu.service;

import com.project.sapaadu.dto.request.LoginRequest;
import com.project.sapaadu.dto.request.RegisterRequest;
import com.project.sapaadu.dto.response.LoginResponse;

public interface UserService {

    void registerUser(RegisterRequest request);

    LoginResponse loginUser(LoginRequest request);
}