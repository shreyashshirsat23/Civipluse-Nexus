# CivicPulse Nexus architecture

CivicPulse is intentionally implemented as a **modular monolith** for a final-year/internship-scale project. The package boundaries mirror the four milestones, while the platform services (security, audit, events and analytics) are shared. This keeps the project deployable as one application without pretending that four small CRUD services are a production microservice fleet.

```text
React + JSX
   | JWT
   v
Spring Boot REST API
   |-- citizen module
   |-- certificate module
   |-- welfare module
   |-- dashboard module
   |-- audit module
   |
   +--> PostgreSQL
   +--> Redis (dashboard cache)
   +--> Kafka (governance events)
   +--> Keycloak (OIDC / RBAC)
```

## Final workflow
1. Citizen submits a validated request.
2. The service creates a workflow record with a deadline.
3. Officers process operational work; commissioner/admin roles approve critical actions.
4. The action is written to the append-only audit table.
5. A Kafka event is published using the entity id as the message key.
6. Dashboard metrics are calculated from PostgreSQL and cached briefly in Redis.

## Why this is resume-worthy
The project demonstrates business workflow design, not only CRUD: security boundaries, SLA metadata, role-based approvals, event-driven audit/analytics hooks, caching and a real React operations console.
