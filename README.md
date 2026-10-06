# CivicPulse Nexus

CivicPulse Nexus is an individual final-milestone project for the Infosys Springboard 7.0 track. It demonstrates a practical smart-governance workflow rather than a static dashboard: citizen records feed grievances, certificate applications and welfare services; approvals produce audit events; analytics are calculated from PostgreSQL data.

## Milestones
- **M1 – Citizen Management:** validated citizen registry, departments, grievance submission, assignment, resolution and SLA deadline.
- **M2 – Certificate Management:** service application, workflow stage, officer/commissioner review and certificate issuance.
- **M3 – Welfare & Budget:** welfare schemes, beneficiary applications, approvals and budget utilisation.
- **M4 – Governance Analytics:** operational dashboard for resolution, SLA, certificates, beneficiaries, satisfaction and budget utilisation.

## Engineering features
- Java 21 + Spring Boot REST API
- PostgreSQL + Spring Data JPA
- Keycloak + JWT + RBAC (ADMIN, COMMISSIONER, OFFICER, CITIZEN)
- Kafka governance events keyed by entity ID
- Redis caching for dashboard summary
- Jakarta validation and centralized API error responses
- Append-only audit log
- React + JSX + Vite frontend
- Docker Compose for PostgreSQL, Redis, Kafka/Zookeeper and Keycloak

## Run locally
1. Install Java 21, Node.js 20+, Maven 3.9+ (or use the Maven wrapper), and Docker Desktop.
2. Start infrastructure: `docker compose up -d`.
3. Backend: `cd backend && ./mvnw spring-boot:run` (Windows: `mvnw.cmd spring-boot:run`).
4. Frontend: `cd frontend && npm install && npm run dev`.
5. Open `http://localhost:5173`.

### Demo users
All demo users use password `admin123`.
- `admin` — ADMIN
- `commissioner` — COMMISSIONER
- `officer` — OFFICER
- `citizen` — CITIZEN

The realm is imported automatically from `keycloak/civicpulse-realm.json` on the first Keycloak start.

## API overview
- `GET/POST /api/citizens`
- `GET/POST /api/grievances`, `PATCH /api/grievances/{id}/status`
- `GET/POST /api/applications`, `PATCH /api/applications/{id}/review`
- `GET /api/certificates`
- `GET/POST /api/welfare/schemes`
- `GET/POST /api/welfare/applications`, `PATCH /api/welfare/applications/{id}/approve`
- `GET/POST /api/budgets`
- `GET /api/dashboard/summary`
- `GET /api/audit`

##project description
**CivicPulse Nexus – Smart Governance Platform** — Built a role-secured Spring Boot and React platform that connects citizen grievances, certificate workflows and welfare/budget operations through PostgreSQL. Implemented Keycloak JWT/RBAC, SLA-aware workflows, append-only audit logging, Kafka governance events and Redis-backed dashboard caching, with a responsive React JSX operations console and Docker Compose development environment.
