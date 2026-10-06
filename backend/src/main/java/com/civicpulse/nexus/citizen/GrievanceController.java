package com.civicpulse.nexus.citizen;

import com.civicpulse.nexus.common.Status;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/grievances")
public class GrievanceController {
    private final GrievanceService service;

    public GrievanceController(GrievanceService service) {
        this.service = service;
    }

    @GetMapping
    public List<Grievance> all() {
        return service.all();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','OFFICER','CITIZEN')")
    public Grievance create(@Valid @RequestBody GrievanceRequest r) {
        return service.create(r);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','OFFICER')")
    public Grievance status(@PathVariable UUID id, @RequestParam Status status,
            @RequestParam(defaultValue = "Officer") String officer) {
        return service.updateStatus(id, status, officer);
    }
}
