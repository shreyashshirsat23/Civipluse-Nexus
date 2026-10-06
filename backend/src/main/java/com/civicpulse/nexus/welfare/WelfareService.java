package com.civicpulse.nexus.welfare;

import com.civicpulse.nexus.audit.AuditService;
import com.civicpulse.nexus.common.*;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.CacheEvict;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

@Service
public class WelfareService {
    private final WelfareSchemeRepository schemes;
    private final WelfareApplicationRepository apps;
    private final AuditService audit;

    public WelfareService(WelfareSchemeRepository schemes, WelfareApplicationRepository apps, AuditService audit) {
        this.schemes = schemes;
        this.apps = apps;
        this.audit = audit;
    }

    public List<WelfareScheme> schemes() {
        return schemes.findAll();
    }

    @CacheEvict(value = "dashboard", allEntries = true)
    public WelfareScheme createScheme(WelfareSchemeRequest r) {
        var s = new WelfareScheme();
        s.setName(r.name());
        s.setDescription(r.description());
        s.setAllocatedAmount(r.allocatedAmount());
        var saved = schemes.save(s);
        audit.record("WELFARE_SCHEME_CREATED", "WelfareScheme", saved.getId().toString(), saved.getName());
        return saved;
    }

    public List<WelfareApplication> applications() {
        return apps.findAll();
    }

    @CacheEvict(value = "dashboard", allEntries = true)
    public WelfareApplication apply(WelfareApplicationRequest r) {
        schemes.findById(r.schemeId()).orElseThrow();
        var a = new WelfareApplication();
        a.setReferenceNo("WEL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        a.setCitizenId(r.citizenId());
        a.setSchemeId(r.schemeId());
        var saved = apps.save(a);
        audit.record("WELFARE_APPLIED", "WelfareApplication", saved.getId().toString(), saved.getReferenceNo());
        return saved;
    }

    @CacheEvict(value = "dashboard", allEntries = true)
    public WelfareApplication approve(UUID id, BigDecimal amount) {
        var a = apps.findById(id).orElseThrow();
        a.setApprovedAmount(amount);
        var scheme = schemes.findById(a.getSchemeId()).orElseThrow();
        scheme.setDisbursedAmount(scheme.getDisbursedAmount().add(amount));
        schemes.save(scheme);
        a.setStage(WorkflowStage.COMPLETED);
        a.setStatus(Status.APPROVED);
        a.setApprovedAt(Instant.now());
        var saved = apps.save(a);
        audit.record("WELFARE_APPROVED", "WelfareApplication", id.toString(), "Amount=" + amount);
        return saved;
    }
}
