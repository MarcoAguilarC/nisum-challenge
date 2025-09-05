package com.nisum.challenge.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhoneDTO {
    @Schema(description = "Phone number including local digits", example = "987654321")
    @NotBlank(message = "The phone number cannot be empty.")
    @Pattern(regexp = "\\d+", message = "The phone number should only contain digits.")
    private String number;

    @Schema(description = "City/area code", example = "9")
    @NotBlank(message = "The city code cannot be empty.")
    @Pattern(regexp = "\\d+", message = "The city code should only contain digits.")
    private String cityCode;

    @Schema(description = "Country code (without +)", example = "56")
    @NotBlank(message = "The country code cannot be empty.")
    @Pattern(regexp = "\\d+", message = "The country code should only contain digits.")
    private String countryCode;
}