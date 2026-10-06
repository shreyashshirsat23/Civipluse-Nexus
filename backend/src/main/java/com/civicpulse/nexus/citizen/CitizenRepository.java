package com.civicpulse.nexus.citizen;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface CitizenRepository extends JpaRepository<Citizen, UUID> {
}
