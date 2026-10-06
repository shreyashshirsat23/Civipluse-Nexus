# Suggested final demo flow

1. Login as `admin` to show the command center.
2. Open Citizen Services and create a citizen; point out backend validation.
3. Submit a grievance and resolve it as an officer.
4. Create a certificate application and approve it as commissioner. Show the generated certificate record.
5. Apply for welfare and approve the beneficiary as commissioner.
6. Open Governance Analytics and explain resolution, SLA and budget utilisation metrics.
7. Open Audit Trail and show the recorded governance actions.
8. Explain that Kafka carries governance events, Redis caches the dashboard summary, and Keycloak controls roles.

Avoid claiming that the demo numbers are national production statistics. They are seeded local demonstration data; the dashboard calculations are live against the local PostgreSQL database.
