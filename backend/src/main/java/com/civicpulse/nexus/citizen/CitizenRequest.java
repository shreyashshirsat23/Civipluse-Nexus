package com.civicpulse.nexus.citizen;

import jakarta.validation.constraints.*;

public record CitizenRequest(@NotBlank @Size(max = 100) String fullName, @NotBlank @Email String email,
        @NotBlank @Pattern(regexp = "[0-9+ -]{10,15}") String phone, @NotBlank @Size(max = 250) String address) {
}
