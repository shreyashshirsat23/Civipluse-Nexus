package com.civicpulse.nexus.welfare;

import com.civicpulse.nexus.common.Status;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "welfare_schemes")
@Getter
@Setter
@NoArgsConstructor
public class WelfareScheme {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false, length = 1000)
    private String description;
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal allocatedAmount;
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal disbursedAmount = BigDecimal.ZERO;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.ACTIVE;
}
