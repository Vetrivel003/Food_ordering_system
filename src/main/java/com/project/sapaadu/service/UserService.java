package com.project.sapaadu.service;

import com.project.sapaadu.dto.request.RegisterRequest;

public interface UserService {

    void registerUser(RegisterRequest request);
}