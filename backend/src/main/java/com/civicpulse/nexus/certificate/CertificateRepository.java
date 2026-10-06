package com.civicpulse.nexus.certificate;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface CertificateRepository extends JpaRepository<Certificate, UUID> {
}
