package com.civicpulse.nexus.certificate;

import com.civicpulse.nexus.common.*;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {
    private final ApplicationService service;

    public ApplicationController(ApplicationService service) {
        this.service = service;
    }

    @GetMapping
    public List<ServiceApplication> all() {
        return service.all();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','OFFICER','CITIZEN')")
    public ServiceApplication create(@Valid @RequestBody ApplicationRequest r) {
        return service.create(r);
    }

    @PatchMapping("/{id}/review")
    @PreAuthorize("hasAnyRole('ADMIN','OFFICER')")
    public ServiceApplication review(@PathVariable UUID id, @RequestParam WorkflowStage stage,
            @RequestParam Status status, @RequestParam(defaultValue = "Reviewer") String reviewer) {
        return service.review(id, stage, status, reviewer);
    }

    @PatchMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('ADMIN','COMMISSIONER')")
    public ServiceApplication approve(@PathVariable UUID id,
            @RequestParam(defaultValue = "COMMISSIONER") String reviewer) {
        return service.review(id, WorkflowStage.COMPLETED, Status.APPROVED, reviewer);
    }
}
