package com.civicpulse.nexus.certificate;

import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/certificates")
public class CertificateController {
    private final CertificateRepository repo;

    public CertificateController(CertificateRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Certificate> all() {
        return repo.findAll();
    }
}
