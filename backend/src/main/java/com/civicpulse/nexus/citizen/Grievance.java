package com.civicpulse.nexus.citizen;

import com.civicpulse.nexus.common.*;
import jakarta.persistence.*;
import lombok.*;
import java.time.*;
import java.util.UUID;

@Entity
@Table(name = "grievances")
@Getter
@Setter
@NoArgsConstructor
public class Grievance {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false, unique = true)
    private String referenceNo;
    @Column(nullable = false)
    private UUID citizenId;
    @Column(nullable = false)
    private UUID departmentId;
    @Column(nullable = false)
    private String subject;
    @Column(nullable = false, length = 2000)
    private String description;
    @Column(nullable = false)
    private String category;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.PENDING;
    @Column(nullable = false)
    private Instant createdAt;
    private Instant resolvedAt;
    private Instant slaDeadline;
    @Column(nullable = false)
    private String assignedOfficer;

    @PrePersist
    void onCreate() {
        createdAt = Instant.now();
        if (slaDeadline == null)
            slaDeadline = createdAt.plus(Duration.ofHours(168));
        if (assignedOfficer == null)
            assignedOfficer = "Unassigned";
    }
}
