package com.example.securityservice.model.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {

    ROLE_MANAGER,
    ROLE_USER;

    @Override
    public String getAuthority() {
        return name();
    }

}
