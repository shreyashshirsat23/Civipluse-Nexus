package com.civicpulse.nexus.welfare;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record WelfareSchemeRequest(@NotBlank String name, @NotBlank String description,
        @NotNull @Positive BigDecimal allocatedAmount) {
}
