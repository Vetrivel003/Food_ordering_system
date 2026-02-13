package com.project.sapaadu.service.impl;

import com.project.sapaadu.dto.request.LoginRequest;
import com.project.sapaadu.dto.request.RegisterRequest;
import com.project.sapaadu.dto.response.LoginResponse;
import com.project.sapaadu.entity.Role;
import com.project.sapaadu.entity.User;
import com.project.sapaadu.entity.UserRole;
import com.project.sapaadu.exception.BadRequestException;
import com.project.sapaadu.repository.RoleRepository;
import com.project.sapaadu.repository.UserRepository;
import com.project.sapaadu.repository.UserRoleRepository;
import com.project.sapaadu.service.UserService;
import lombok.RequiredArgsConstructor;
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

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("Invalid credentials"));

        if (user.isBlocked()) {
            throw new BadRequestException("User is blocked");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new BadRequestException("Invalid credentials");
        }

        Set<String> roles = user.getUserRoles()
                .stream()
                .map(userRole -> userRole.getRole().getName())
                .collect(Collectors.toSet());

        return LoginResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .roles(roles)
                .build();
    }
}