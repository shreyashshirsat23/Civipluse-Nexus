package com.civicpulse.nexus.citizen;

import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {
    private final DepartmentRepository repo;

    public DepartmentController(DepartmentRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Department> all() {
        return repo.findAll();
    }

    @PostMapping
    public Department create(@RequestBody Department d) {
        return repo.save(d);
    }
}
