package com.civicpulse.nexus.welfare;

import com.civicpulse.nexus.common.*;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "welfare_applications")
@Getter
@Setter
@NoArgsConstructor
public class WelfareApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false, unique = true)
    private String referenceNo;
    @Column(nullable = false)
    private UUID citizenId;
    @Column(nullable = false)
    private UUID schemeId;
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal approvedAmount = BigDecimal.ZERO;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkflowStage stage = WorkflowStage.SUBMITTED;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.PENDING;
    @Column(nullable = false)
    private Instant submittedAt;
    private Instant approvedAt;
    private Instant slaDeadline;

    @PrePersist
    void onCreate() {
        submittedAt = Instant.now();
        slaDeadline = submittedAt.plusSeconds(120 * 3600L);
    }
}
