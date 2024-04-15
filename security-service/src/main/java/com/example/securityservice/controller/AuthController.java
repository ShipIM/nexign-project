package com.example.securityservice.controller;

import com.example.securityservice.dto.user.AuthenticateUserRequest;
import com.example.securityservice.dto.user.AuthenticateUserResponse;
import com.example.securityservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/security")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/authentication")
    public AuthenticateUserResponse authenticateUser(
            @RequestBody @Valid
            AuthenticateUserRequest request) {
        return authService.authenticate(request);
    }

}
