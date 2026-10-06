package com.civicpulse.nexus.welfare;

import jakarta.validation.constraints.*;
import java.util.UUID;

public record WelfareApplicationRequest(@NotNull UUID citizenId, @NotNull UUID schemeId) {
}
