package com.civicpulse.nexus.citizen;

import com.civicpulse.nexus.audit.AuditService;
import com.civicpulse.nexus.common.*;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.CacheEvict;
import java.time.Instant;
import java.util.*;

@Service
public class GrievanceService {
    private final GrievanceRepository repo;
    private final AuditService audit;

    public GrievanceService(GrievanceRepository repo, AuditService audit) {
        this.repo = repo;
        this.audit = audit;
    }

    public List<Grievance> all() {
        return repo.findAll();
    }

    @CacheEvict(value = "dashboard", allEntries = true)
    public Grievance create(GrievanceRequest r) {
        var g = new Grievance();
        g.setReferenceNo("GRV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        g.setCitizenId(r.citizenId());
        g.setDepartmentId(r.departmentId());
        g.setSubject(r.subject());
        g.setDescription(r.description());
        g.setCategory(r.category());
        g.setStatus(Status.PENDING);
        g.setAssignedOfficer("Unassigned");
        var saved = repo.save(g);
        audit.record("GRIEVANCE_SUBMITTED", "Grievance", saved.getId().toString(), saved.getReferenceNo());
        return saved;
    }

    @CacheEvict(value = "dashboard", allEntries = true)
    public Grievance updateStatus(UUID id, Status status, String officer) {
        var g = repo.findById(id).orElseThrow();
        g.setStatus(status);
        g.setAssignedOfficer(officer);
        if (status == Status.RESOLVED || status == Status.CLOSED)
            g.setResolvedAt(Instant.now());
        var saved = repo.save(g);
        audit.record("GRIEVANCE_" + status, "Grievance", id.toString(), "Officer=" + officer);
        return saved;
    }
}
