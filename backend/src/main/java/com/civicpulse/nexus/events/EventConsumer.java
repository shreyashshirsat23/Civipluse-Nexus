package com.civicpulse.nexus.events;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class EventConsumer {
    private static final Logger log = LoggerFactory.getLogger(EventConsumer.class);

    @KafkaListener(topics = "governance.events", groupId = "civicpulse-analytics")
    public void consume(GovernanceEvent event) {
        log.info("Governance event processed: type={}, entity={}, id={}", event.type(), event.entityType(),
                event.entityId());
    }
}
