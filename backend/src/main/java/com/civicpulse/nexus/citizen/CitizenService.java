package com.civicpulse.nexus.citizen;

import com.civicpulse.nexus.audit.AuditService;
import com.civicpulse.nexus.common.Status;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.CacheEvict;
import java.util.*;

@Service
public class CitizenService {
    private final CitizenRepository repo;
    private final AuditService audit;

    public CitizenService(CitizenRepository repo, AuditService audit) {
        this.repo = repo;
        this.audit = audit;
    }

    public List<Citizen> all() {
        return repo.findAll();
    }

    @CacheEvict(value = "dashboard", allEntries = true)
    public Citizen create(CitizenRequest r) {
        var c = new Citizen();
        c.setFullName(r.fullName());
        c.setEmail(r.email());
        c.setPhone(r.phone());
        c.setAddress(r.address());
        c.setStatus(Status.ACTIVE);
        var saved = repo.save(c);
        audit.record("CITIZEN_CREATED", "Citizen", saved.getId().toString(), saved.getEmail());
        return saved;
    }
}
