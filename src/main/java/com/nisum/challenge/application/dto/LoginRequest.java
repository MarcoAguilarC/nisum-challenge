package com.nisum.challenge.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @Schema(description = "User email address", example = "juan.perez@example.com")
    @NotBlank(message = "The email cannot be empty.")
    @Email
    private String email;

    @Schema(description = "User password", example = "Password123!")
    @NotBlank(message = "The password cannot be empty.")
    private String password;
}
