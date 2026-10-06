package com.civicpulse.nexus.config;

import com.civicpulse.nexus.certificate.*;
import com.civicpulse.nexus.citizen.*;
import com.civicpulse.nexus.common.Status;
import com.civicpulse.nexus.welfare.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.Instant;

@Configuration
public class DataSeeder {
    @Bean
    CommandLineRunner seed(CitizenRepository citizens, DepartmentRepository departments, GrievanceRepository grievances,
            WelfareSchemeRepository schemes, WelfareApplicationRepository welfareApps, BudgetRepository budgets,
            ServiceApplicationRepository applications, CertificateRepository certificates) {
        return args -> {
            Department water;
            Department civic;
            if (departments.count() == 0) {
                water = new Department();
                water.setName("Water & Utilities");
                water.setCode("WTR");
                departments.save(water);
                civic = new Department();
                civic.setName("Civic Works");
                civic.setCode("CWK");
                departments.save(civic);
            } else {
                water = departments.findAll().get(0);
                civic = departments.findAll().get(Math.min(1, departments.findAll().size() - 1));
            }

            Citizen ramesh;
            Citizen priya;
            if (citizens.count() == 0) {
                ramesh = new Citizen();
                ramesh.setFullName("Ramesh Kumar");
                ramesh.setEmail("ramesh@example.com");
                ramesh.setPhone("9876543210");
                ramesh.setAddress("Nashik");
                ramesh = citizens.save(ramesh);
                priya = new Citizen();
                priya.setFullName("Priya Sharma");
                priya.setEmail("priya@example.com");
                priya.setPhone("9876543211");
                priya.setAddress("Pune");
                priya = citizens.save(priya);
            } else {
                ramesh = citizens.findAll().get(0);
                priya = citizens.findAll().get(Math.min(1, citizens.findAll().size() - 1));
            }

            if (grievances.count() == 0) {
                Grievance g = new Grievance();
                g.setReferenceNo("GRV-DEMO-1001");
                g.setCitizenId(ramesh.getId());
                g.setDepartmentId(water.getId());
                g.setSubject("Low water pressure");
                g.setDescription("Water pressure is low in the morning.");
                g.setCategory("Water");
                g.setStatus(Status.IN_PROGRESS);
                g.setAssignedOfficer("officer");
                grievances.save(g);
                Grievance g2 = new Grievance();
                g2.setReferenceNo("GRV-DEMO-1002");
                g2.setCitizenId(priya.getId());
                g2.setDepartmentId(civic.getId());
                g2.setSubject("Street light not working");
                g2.setDescription("Street light near the community hall is not working.");
                g2.setCategory("Street Lighting");
                g2.setStatus(Status.RESOLVED);
                g2.setAssignedOfficer("officer");
                g2.setResolvedAt(Instant.now());
                grievances.save(g2);
            }

            WelfareScheme housing;
            if (schemes.count() == 0) {
                housing = new WelfareScheme();
                housing.setName("Urban Housing Support");
                housing.setDescription("Housing assistance for eligible low-income families.");
                housing.setAllocatedAmount(new BigDecimal("2400000"));
                housing = schemes.save(housing);
                WelfareScheme scholarship = new WelfareScheme();
                scholarship.setName("Student Scholarship");
                scholarship.setDescription("Education support for eligible students.");
                scholarship.setAllocatedAmount(new BigDecimal("5000000"));
                schemes.save(scholarship);
            } else {
                housing = schemes.findAll().get(0);
            }

            if (welfareApps.count() == 0) {
                WelfareApplication wa = new WelfareApplication();
                wa.setReferenceNo("WEL-DEMO-1001");
                wa.setCitizenId(ramesh.getId());
                wa.setSchemeId(housing.getId());
                wa.setApprovedAmount(new BigDecimal("25000"));
                wa.setStatus(Status.APPROVED);
                wa.setStage(com.civicpulse.nexus.common.WorkflowStage.COMPLETED);
                wa.setApprovedAt(Instant.now());
                welfareApps.save(wa);
            }

            if (budgets.count() == 0) {
                Budget b = new Budget();
                b.setFinancialYear("2026-27");
                b.setDepartment("Citizen Services");
                b.setAllocated(new BigDecimal("30000000"));
                b.setUtilized(new BigDecimal("26100000"));
                budgets.save(b);
            }

            if (applications.count() == 0) {
                ServiceApplication a = new ServiceApplication();
                a.setReferenceNo("APP-DEMO-1001");
                a.setCitizenId(priya.getId());
                a.setServiceType("Birth Certificate");
                a.setPurpose("Official records");
                a.setStage(com.civicpulse.nexus.common.WorkflowStage.COMPLETED);
                a.setStatus(Status.APPROVED);
                a.setReviewedBy("commissioner");
                a.setCompletedAt(Instant.now());
                a = applications.save(a);
                Certificate c = new Certificate();
                c.setCertificateNo("CERT-DEMO-1001");
                c.setApplicationId(a.getId());
                c.setCitizenId(priya.getId());
                c.setType("Birth Certificate");
                c.setIssuedAt(Instant.now());
                c.setIssuedBy("commissioner");
                certificates.save(c);
            }
        };
    }
}
