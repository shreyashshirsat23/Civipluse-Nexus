package com.civicpulse.nexus.welfare;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/welfare")
public class WelfareController {
    private final WelfareService service;

    public WelfareController(WelfareService service) {
        this.service = service;
    }

    @GetMapping("/schemes")
    public List<WelfareScheme> schemes() {
        return service.schemes();
    }

    @PostMapping("/schemes")
    @PreAuthorize("hasAnyRole('ADMIN','OFFICER')")
    public WelfareScheme create(@Valid @RequestBody WelfareSchemeRequest r) {
        return service.createScheme(r);
    }

    @GetMapping("/applications")
    public List<WelfareApplication> applications() {
        return service.applications();
    }

    @PostMapping("/applications")
    @PreAuthorize("hasAnyRole('ADMIN','OFFICER','CITIZEN')")
    public WelfareApplication apply(@Valid @RequestBody WelfareApplicationRequest r) {
        return service.apply(r);
    }

    @PatchMapping("/applications/{id}/approve")
    @PreAuthorize("hasAnyRole('ADMIN','COMMISSIONER')")
    public WelfareApplication approve(@PathVariable UUID id, @RequestParam java.math.BigDecimal amount) {
        return service.approve(id, amount);
    }
}
