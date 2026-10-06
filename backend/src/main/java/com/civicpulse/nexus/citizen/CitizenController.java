package com.civicpulse.nexus.citizen;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/citizens")
public class CitizenController {
    private final CitizenService service;

    public CitizenController(CitizenService service) {
        this.service = service;
    }

    @GetMapping
    public List<Citizen> all() {
        return service.all();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','OFFICER')")
    public Citizen create(@Valid @RequestBody CitizenRequest r) {
        return service.create(r);
    }
}
