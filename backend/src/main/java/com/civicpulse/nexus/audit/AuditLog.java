package com.civicpulse.nexus.audit;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
@NoArgsConstructor
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false, updatable = false)
    private Instant timestamp;
    @Column(nullable = false, updatable = false)
    private String actor;
    @Column(nullable = false, updatable = false)
    private String role;
    @Column(nullable = false, updatable = false)
    private String action;
    @Column(nullable = false, updatable = false)
    private String entityType;
    @Column(nullable = false, updatable = false)
    private String entityId;
    @Column(length = 1000, updatable = false)
    private String details;

    @PrePersist
    void onCreate() {
        timestamp = Instant.now();
    }
}
