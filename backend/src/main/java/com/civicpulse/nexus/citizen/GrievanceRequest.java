package com.civicpulse.nexus.citizen;

import jakarta.validation.constraints.*;
import java.util.UUID;

public record GrievanceRequest(@NotNull UUID citizenId, @NotNull UUID departmentId,
        @NotBlank @Size(max = 120) String subject, @NotBlank @Size(max = 2000) String description,
        @NotBlank String category) {
}
