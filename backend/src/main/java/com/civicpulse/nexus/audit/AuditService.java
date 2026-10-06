package com.civicpulse.nexus.audit;

import com.civicpulse.nexus.events.GovernanceEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.Instant;

@Service
public class AuditService {
    private final AuditLogRepository repo;
    private final KafkaTemplate<String, GovernanceEvent> kafka;

    public AuditService(AuditLogRepository repo, KafkaTemplate<String, GovernanceEvent> kafka) {
        this.repo = repo;
        this.kafka = kafka;
    }

    public void record(String action, String entityType, String entityId, String details) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        String actor = auth != null ? auth.getName() : "system";
        String role = auth != null
                ? auth.getAuthorities().stream().findFirst().map(a -> a.getAuthority().replace("ROLE_", ""))
                        .orElse("SYSTEM")
                : "SYSTEM";
        var log = new AuditLog();
        log.setActor(actor);
        log.setRole(role);
        log.setAction(action);
        log.setEntityType(entityType);
        log.setEntityId(entityId);
        log.setDetails(details);
        repo.save(log);
        kafka.send("governance.events", entityId,
                new GovernanceEvent(action, entityType, entityId, actor, Instant.now()));
    }
}
