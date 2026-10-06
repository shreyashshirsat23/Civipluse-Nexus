package com.civicpulse.nexus.welfare;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface WelfareApplicationRepository extends JpaRepository<WelfareApplication, UUID> {
}
