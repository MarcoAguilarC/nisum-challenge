package com.nisum.challenge.application.dto;

import com.nisum.challenge.application.validation.annotation.UniquePhone;
import com.nisum.challenge.application.validation.annotation.ValidPassword;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class UserRequest {

    @Schema(description = "Full name of the user", example = "Juan Perez")
    @NotBlank(message = "The name cannot be empty.")
    private String name;

    @Schema(description = "User email address", example = "juan.perez@example.com")
    @NotBlank(message = "The email cannot be empty.")
    @Email(message = "The email format is not valid.")
    private String email;

    @Schema(description = "Password that meets policy requirements", example = "Password123!")
    @NotBlank(message = "The password cannot be empty.")
    @ValidPassword
    private String password;

    @Schema(description = "List of user phones")
    @UniquePhone
    @Valid
    private List<PhoneDTO> phones;
}