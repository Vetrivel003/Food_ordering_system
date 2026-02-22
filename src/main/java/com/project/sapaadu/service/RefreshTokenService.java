package com.project.sapaadu.service;

import com.project.sapaadu.entity.RefreshToken;
import com.project.sapaadu.entity.User;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(User user);

    RefreshToken verifyRefreshToken(String token);

    void revokeRefreshToken(String token);
}
