package com.civicpulse.nexus.welfare;

import org.springframework.web.bind.annotation.*;
import org.springframework.cache.annotation.CacheEvict;
import java.util.*;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {
    private final BudgetRepository repo;

    public BudgetController(BudgetRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Budget> all() {
        return repo.findAll();
    }

    @PostMapping
    @CacheEvict(value = "dashboard", allEntries = true)
    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('ADMIN','COMMISSIONER')")
    public Budget create(@RequestBody Budget b) {
        return repo.save(b);
    }
}
