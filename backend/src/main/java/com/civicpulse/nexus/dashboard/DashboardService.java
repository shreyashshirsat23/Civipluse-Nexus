package com.civicpulse.nexus.dashboard;

import com.civicpulse.nexus.citizen.*;
import com.civicpulse.nexus.certificate.*;
import com.civicpulse.nexus.welfare.*;
import com.civicpulse.nexus.common.Status;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.math.*;

@Service
public class DashboardService {
    private final CitizenRepository citizens;
    private final GrievanceRepository grievances;
    private final ServiceApplicationRepository apps;
    private final CertificateRepository certs;
    private final WelfareApplicationRepository welfareApps;
    private final BudgetRepository budgets;

    public DashboardService(CitizenRepository c, GrievanceRepository g, ServiceApplicationRepository a,
            CertificateRepository ce, WelfareApplicationRepository wa, BudgetRepository b) {
        citizens = c;
        grievances = g;
        apps = a;
        certs = ce;
        welfareApps = wa;
        budgets = b;
    }

    @Cacheable("dashboard")
    public DashboardSummary summary() {
        long gc = grievances.count(), resolved = grievances.findAll().stream()
                .filter(g -> g.getStatus() == Status.RESOLVED || g.getStatus() == Status.CLOSED).count();
        BigDecimal alloc = budgets.findAll().stream().map(Budget::getAllocated).reduce(BigDecimal.ZERO,
                BigDecimal::add);
        BigDecimal util = budgets.findAll().stream().map(Budget::getUtilized).reduce(BigDecimal.ZERO, BigDecimal::add);
        double bu = alloc.signum() == 0 ? 0 : util.divide(alloc, 4, RoundingMode.HALF_UP).doubleValue() * 100;
        return new DashboardSummary(citizens.count(), gc, resolved, gc == 0 ? 0 : resolved * 100.0 / gc, apps.count(),
                certs.count(), welfareApps.findAll().stream().filter(a -> a.getStatus() == Status.APPROVED).count(),
                alloc, util, bu, 94.0, 4.7, util);
    }
}
