package com.example.securityservice.dto.user;

import com.example.securityutils.entity.enumeration.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticateUserResponse {

    private String username;

    private Role role;

    private String access;

}
