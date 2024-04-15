package com.example.securityservice.service;

import com.example.securityservice.dto.mapper.UserMapper;
import com.example.securityservice.dto.user.AuthenticateUserRequest;
import com.example.securityservice.dto.user.AuthenticateUserResponse;
import com.example.securityservice.model.entity.User;
import com.example.securityservice.util.JwtGenerationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtGenerationUtils jwtUtils;
    private final UserMapper userMapper;

    public AuthenticateUserResponse authenticate(AuthenticateUserRequest request) {
        var user = userMapper.mapToUser(request);
        user = (User) authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        ).getPrincipal();

        return userMapper.mapToResponse(user, jwtUtils.generateToken(Map.of("role", user.getRole()), user));
    }

}
