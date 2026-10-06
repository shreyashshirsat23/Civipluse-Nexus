package com.civicpulse.nexus.certificate;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "certificates")
@Getter
@Setter
@NoArgsConstructor
public class Certificate {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false, unique = true)
    private String certificateNo;
    @Column(nullable = false)
    private UUID applicationId;
    @Column(nullable = false)
    private UUID citizenId;
    @Column(nullable = false)
    private String type;
    @Column(nullable = false)
    private Instant issuedAt;
    @Column(nullable = false)
    private String issuedBy;
}
