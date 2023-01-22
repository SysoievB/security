package com.security.entity.security;

import org.springframework.security.core.GrantedAuthority;

public enum AccountStatus implements GrantedAuthority {
    ACTIVE, DELETED;

    @Override
    public String getAuthority() {
        return name();
    }
}
