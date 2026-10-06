package com.civicpulse.nexus.welfare;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "budgets")
@Getter
@Setter
@NoArgsConstructor
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String financialYear;
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal allocated;
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal utilized = BigDecimal.ZERO;
    @Column(nullable = false)
    private String department;
}
