package com.civicpulse.nexus.dashboard;

import java.math.BigDecimal;

public record DashboardSummary(long citizens, long grievances, long resolvedGrievances, double resolutionRate,
        long applications, long certificates, long beneficiaries, BigDecimal allocatedBudget, BigDecimal utilizedBudget,
        double budgetUtilization, double slaCompliance, double satisfaction, BigDecimal revenue) {
}
