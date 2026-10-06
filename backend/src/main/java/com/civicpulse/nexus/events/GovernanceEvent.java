package com.civicpulse.nexus.events;

import java.time.Instant;

public record GovernanceEvent(String type, String entityType, String entityId, String actor, Instant occurredAt) {
}
