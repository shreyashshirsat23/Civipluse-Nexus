package com.civicpulse.nexus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CivicPulseNexusApplication {
    public static void main(String[] args) {
        SpringApplication.run(CivicPulseNexusApplication.class, args);
    }
}
