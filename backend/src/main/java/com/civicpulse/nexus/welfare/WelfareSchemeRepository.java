package com.civicpulse.nexus.welfare;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface WelfareSchemeRepository extends JpaRepository<WelfareScheme, UUID> {
}
