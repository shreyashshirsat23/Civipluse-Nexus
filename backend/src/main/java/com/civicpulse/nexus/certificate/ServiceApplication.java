package com.civicpulse.nexus.certificate;

import com.civicpulse.nexus.common.*;
import jakarta.persistence.*;
import lombok.*;
import java.time.*;
import java.util.UUID;

@Entity
@Table(name = "service_applications")
@Getter
@Setter
@NoArgsConstructor
public class ServiceApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false, unique = true)
    private String referenceNo;
    @Column(nullable = false)
    private UUID citizenId;
    @Column(nullable = false)
    private String serviceType;
    @Column(nullable = false)
    private String purpose;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkflowStage stage = WorkflowStage.SUBMITTED;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.PENDING;
    @Column(nullable = false)
    private Instant submittedAt;
    private Instant completedAt;
    private Instant slaDeadline;
    private String reviewedBy;

    @PrePersist
    void onCreate() {
        submittedAt = Instant.now();
        slaDeadline = submittedAt.plus(Duration.ofHours(72));
    }
}
