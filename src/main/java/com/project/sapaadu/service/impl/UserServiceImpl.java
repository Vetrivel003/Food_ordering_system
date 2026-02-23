package com.project.sapaadu.service.impl;

import com.project.sapaadu.dto.request.LoginRequest;
import com.project.sapaadu.dto.request.RefreshRequest;
import com.project.sapaadu.dto.request.RegisterRequest;
import com.project.sapaadu.dto.response.LoginResponse;
import com.project.sapaadu.dto.response.RefreshResponse;
import com.project.sapaadu.entity.RefreshToken;
import com.project.sapaadu.entity.Role;
import com.project.sapaadu.entity.User;
import com.project.sapaadu.entity.UserRole;
import com.project.sapaadu.exception.BadRequestException;
import com.project.sapaadu.repository.RoleRepository;
import com.project.sapaadu.repository.UserRepository;
import com.project.sapaadu.repository.UserRoleRepository;
import com.project.sapaadu.security.JwtUtil;
import com.project.sapaadu.service.RefreshTokenService;
import com.project.sapaadu.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    @Override
    public void registerUser(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        if (request.getPhone() != null && userRepository.existsByPhone(request.getPhone())) {
            throw new BadRequestException("Phone already exists");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .build();

        userRepository.save(user);

        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new BadRequestException("Default role not found"));

        UserRole mapping = UserRole.builder()
                .user(user)
                .role(userRole)
                .build();

        userRoleRepository.save(mapping);
    }

    @Override
    public LoginResponse loginUser(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmailWithRoles(request.getEmail())
                .orElseThrow(() -> new BadRequestException("User not found"));

        String accessToken = jwtUtil.generateToken(user.getEmail());

        RefreshToken refreshToken =
                refreshTokenService.createRefreshToken(user);

        Set<String> roles = user.getUserRoles()
                .stream()
                .map(userRole -> userRole.getRole().getName())
                .collect(Collectors.toSet());

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .roles(roles)
                .build();
    }

    @Override
    public RefreshResponse refreshToken(RefreshRequest request) {

        RefreshToken oldToken =
                refreshTokenService.verifyRefreshToken(request.getRefreshToken());

        User user = oldToken.getUser();

        // Rotate token (revoke old)
        refreshTokenService.revokeRefreshToken(oldToken.getToken());

        // Generate new tokens
        String newAccessToken = jwtUtil.generateToken(user.getEmail());

        RefreshToken newRefreshToken =
                refreshTokenService.createRefreshToken(user);

        return RefreshResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken.getToken())
                .build();
    }

}