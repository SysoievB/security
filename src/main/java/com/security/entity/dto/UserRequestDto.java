package com.security.entity.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record UserRequestDto(
        @NotBlank(message = "Username cannot be empty. Fill the username, please")
        @Size(min = 2, max = 20)
        String username,
        @NotBlank(message = "Password cannot be empty. Fill the password, please.")
        String password) {
}
