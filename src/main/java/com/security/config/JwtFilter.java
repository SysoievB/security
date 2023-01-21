package com.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtFilter {
    private final JwtTokenUtil jwtTokenUtil;
}
