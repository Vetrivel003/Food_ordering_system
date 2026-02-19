package com.project.sapaadu.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class LoginResponse {

    private String token;

    private Long id;
    private String name;
    private String email;
    private Set<String> roles;
}
