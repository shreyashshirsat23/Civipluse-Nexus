package com.civicpulse.nexus.certificate;

import com.civicpulse.nexus.audit.AuditService;
import com.civicpulse.nexus.common.*;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.CacheEvict;
import java.time.Instant;
import java.util.*;

@Service
public class ApplicationService {
    private final ServiceApplicationRepository repo;
    private final CertificateRepository certificates;
    private final AuditService audit;

    public ApplicationService(ServiceApplicationRepository repo, CertificateRepository certificates,
            AuditService audit) {
        this.repo = repo;
        this.certificates = certificates;
        this.audit = audit;
    }

    public List<ServiceApplication> all() {
        return repo.findAll();
    }

    @CacheEvict(value = "dashboard", allEntries = true)
    public ServiceApplication create(ApplicationRequest r) {
        var a = new ServiceApplication();
        a.setReferenceNo("APP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        a.setCitizenId(r.citizenId());
        a.setServiceType(r.serviceType());
        a.setPurpose(r.purpose());
        var saved = repo.save(a);
        audit.record("APPLICATION_SUBMITTED", "ServiceApplication", saved.getId().toString(), saved.getReferenceNo());
        return saved;
    }

    @CacheEvict(value = "dashboard", allEntries = true)
    public ServiceApplication review(UUID id, WorkflowStage stage, Status status, String reviewer) {
        var a = repo.findById(id).orElseThrow();
        a.setStage(stage);
        a.setStatus(status);
        a.setReviewedBy(reviewer);
        if (status == Status.APPROVED) {
            a.setCompletedAt(Instant.now());
            var c = new Certificate();
            c.setCertificateNo("CERT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            c.setApplicationId(a.getId());
            c.setCitizenId(a.getCitizenId());
            c.setType(a.getServiceType());
            c.setIssuedAt(Instant.now());
            c.setIssuedBy(reviewer);
            certificates.save(c);
        }
        var saved = repo.save(a);
        audit.record("APPLICATION_" + status, "ServiceApplication", id.toString(),
                "Stage=" + stage + ", reviewer=" + reviewer);
        return saved;
    }
}
