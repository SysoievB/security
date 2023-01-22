package com.security.entity.security;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMINISTRATOR, SUPPORT, USER;

    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }
}