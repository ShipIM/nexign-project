package com.example.securityservice.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticateUserRequest {

    @NotBlank(message = "username must not be empty")
    private String username;

    private String password;

}
