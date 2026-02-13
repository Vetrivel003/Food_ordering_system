package com.project.sapaadu.service;

import com.project.sapaadu.dto.request.LoginRequest;
import com.project.sapaadu.dto.request.RegisterRequest;

public interface UserService {

    void registerUser(RegisterRequest request);

    void loginUser(LoginRequest request);
}