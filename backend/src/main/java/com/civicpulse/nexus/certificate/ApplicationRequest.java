package com.civicpulse.nexus.certificate;

import jakarta.validation.constraints.*;
import java.util.UUID;

public record ApplicationRequest(@NotNull UUID citizenId, @NotBlank String serviceType,
        @NotBlank @Size(max = 500) String purpose) {
}
