package com.security.controller;

import com.security.entity.User;
import com.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        var allUsers = service.getAllUsers();

        if (allUsers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(allUsers);
    }
}

