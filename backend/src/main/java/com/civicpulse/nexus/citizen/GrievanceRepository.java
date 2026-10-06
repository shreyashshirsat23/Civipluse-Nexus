package com.civicpulse.nexus.citizen;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface GrievanceRepository extends JpaRepository<Grievance, UUID> {
}
